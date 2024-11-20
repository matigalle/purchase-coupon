package com.mercadolibre.purchasecoupon.repositories;

import com.google.gson.Gson;
import com.mercadolibre.purchasecoupon.dtos.Item;
import com.mercadolibre.purchasecoupon.exceptions.ItemRepositoryException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

public class DefaultItemRepository implements ItemRepository {

    private static final String BASE_URL = "https://api.mercadolibre.com/items/";

    @Override
    public Item getItem(String id) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(2))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + id))
                .GET()
                .build();

        CompletableFuture<HttpResponse<String>> resultFuture =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        Item item = resultFuture.thenApply(response -> {
            int statusCode = response.statusCode();
            Optional<Item> itemOptional = Optional.ofNullable(new Gson().fromJson(response.body(), Item.class));

            if (statusCode == SC_OK) {
                if (itemOptional.isPresent()) {
                    return itemOptional.get();
                }

                statusCode = SC_NOT_FOUND;
            }

            throw new ItemRepositoryException("Error getting item id " + id, statusCode);
        })
        .join();

        BigDecimal price = item.price().setScale(2, RoundingMode.DOWN);

        return new Item(item.id(), price);
    }

}
