package com.mercadolibre.purchasecoupon.repositories;

import com.google.gson.Gson;
import com.mercadolibre.purchasecoupon.dtos.Item;
import com.mercadolibre.purchasecoupon.exceptions.HttpClientException;
import com.mercadolibre.purchasecoupon.exceptions.NotFoundException;

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

        Optional<Item> itemOptional = resultFuture.thenApply(response -> {
            int statusCode = response.statusCode();

            if (statusCode == SC_OK) {
                Item item = new Gson().fromJson(response.body(), Item.class);

                return Optional.ofNullable(item);
            }

            if (statusCode == SC_NOT_FOUND) {
                throw new NotFoundException("Item id " + id + "not found");
            }

            throw new HttpClientException("Error getting item id " + id + " from items API");
        })
        .join();

        Item item = itemOptional.orElseThrow();
        BigDecimal price = item.price().setScale(2, RoundingMode.DOWN);

        return new Item(item.id(), price);
    }

}
