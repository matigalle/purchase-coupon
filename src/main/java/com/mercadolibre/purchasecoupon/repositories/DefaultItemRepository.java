package com.mercadolibre.purchasecoupon.repositories;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.mercadolibre.purchasecoupon.dtos.Item;
import com.mercadolibre.purchasecoupon.exceptions.ItemRepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;

public class DefaultItemRepository implements ItemRepository {

    public static final String BASE_URL = "https://api.mercadolibre.com/items/";
    private static final Logger log = LoggerFactory.getLogger(DefaultItemRepository.class);

    private final HttpClient httpClient;

    @Inject
    public DefaultItemRepository(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public List<Item> getItems(List<String> ids) {
        List<CompletableFuture<Item>> futures = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(getThreadPoolSize());

        ids.forEach(id -> futures.add(getItemFuture(id, executor)));

        executor.shutdown();

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        try {
            return allOf.thenApply(v -> futures.stream()
                                               .map(CompletableFuture::join)
                                               .filter(Objects::nonNull)
                                               .toList())
                        .get(10, TimeUnit.SECONDS);

        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new ItemRepositoryException(e.toString());
        }
    }

    private int getThreadPoolSize() {
        int poolSize = Runtime.getRuntime().availableProcessors() * 4;
        int max_concurrent_streams = 64;

        if (poolSize > max_concurrent_streams) {
            poolSize = max_concurrent_streams;
        }

        return poolSize;
    }

    private CompletableFuture<Item> getItemFuture(String id, ExecutorService executor) {
        return CompletableFuture.supplyAsync(() -> {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + id)).GET().build();
            HttpResponse<String> response;

            try {
                response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                throw new ItemRepositoryException(e.getMessage());
            }

            if (response.statusCode() != SC_OK) {
                log.error("Error getting item id " + id + ". Status code: " + response.statusCode());

                return null;
            }

            Item item = new Gson().fromJson(response.body(), Item.class);
            BigDecimal price = item.price().setScale(2, RoundingMode.DOWN);

            return new Item(item.id(), price);
        }, executor);
    }

}
