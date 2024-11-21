package com.mercadolibre.purchasecoupon.repositories;

import com.google.gson.Gson;
import com.mercadolibre.purchasecoupon.dtos.Item;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static com.mercadolibre.purchasecoupon.repositories.DefaultItemRepository.BASE_URL;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DefaultItemRepositoryTest {

    private static final String TEST_ITEM_ID = "MLA1";
    private static HttpResponse<String> response;
    private static DefaultItemRepository defaultItemRepository;

    @BeforeAll
    static void setUp() {
        HttpClient httpClientMock = mock(HttpClient.class);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + TEST_ITEM_ID)).GET().build();
        response = mock(HttpResponse.class);
        CompletableFuture<HttpResponse<String>> completableFuture = CompletableFuture.completedFuture(response);

        Mockito.when(httpClientMock.sendAsync(request, HttpResponse.BodyHandlers.ofString()))
                .thenReturn(completableFuture);

        defaultItemRepository = new DefaultItemRepository(httpClientMock);
    }

    @Test
    void get_item_ok() {
        Mockito.when(response.statusCode()).thenReturn(SC_OK);
        Mockito.when(response.body()).thenReturn(getTestItemJson());

        Item item = defaultItemRepository.getItem(TEST_ITEM_ID);

        assertNotNull(item);
        assertEquals(TEST_ITEM_ID, item.id());
    }

    @Test
    void get_item_null() {
        Mockito.when(response.statusCode()).thenReturn(SC_OK);
        Mockito.when(response.body()).thenReturn(null);

        assertThrows(CompletionException.class, () -> defaultItemRepository.getItem(TEST_ITEM_ID));
    }

    @Test
    void get_item_error() {
        Mockito.when(response.statusCode()).thenReturn(SC_INTERNAL_SERVER_ERROR);

        assertThrows(CompletionException.class, () -> defaultItemRepository.getItem(TEST_ITEM_ID));
    }

    private String getTestItemJson() {
        Item item = new Item(TEST_ITEM_ID, BigDecimal.TEN);

        return new Gson().toJson(item);
    }

}