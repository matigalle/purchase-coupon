package com.mercadolibre.purchasecoupon.repositories.impl;

import com.google.gson.Gson;
import com.mercadolibre.purchasecoupon.dtos.Item;
import com.mercadolibre.purchasecoupon.exceptions.ItemRepositoryException;
import com.mercadolibre.purchasecoupon.repositories.impl.DefaultItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static com.mercadolibre.purchasecoupon.repositories.impl.DefaultItemRepository.BASE_URL;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultItemRepositoryTest {

    private static final String TEST_ITEM_ID = "MLA1";

    private HttpClient httpClientMock;
    private HttpRequest request;
    private HttpResponse<String> response;
    private DefaultItemRepository defaultItemRepository;

    @BeforeEach
    void setUp() {
        httpClientMock = mock(HttpClient.class);
        request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + TEST_ITEM_ID)).GET().build();
        response = mock(HttpResponse.class);

        defaultItemRepository = new DefaultItemRepository(httpClientMock);
    }

    @Test
    void get_items_ok() throws IOException, InterruptedException {
        when(httpClientMock.send(request, HttpResponse.BodyHandlers.ofString())).thenReturn(response);
        when(response.statusCode()).thenReturn(SC_OK);
        when(response.body()).thenReturn(getTestItemJson());

        List<Item> items = defaultItemRepository.getItems(List.of(TEST_ITEM_ID));

        assertNotNull(items);
        assertEquals(TEST_ITEM_ID, items.get(0).id());
    }

    @Test
    void item_not_found() throws IOException, InterruptedException {
        when(httpClientMock.send(request, HttpResponse.BodyHandlers.ofString())).thenReturn(response);
        when(response.statusCode()).thenReturn(SC_NOT_FOUND);

        List<Item> items = defaultItemRepository.getItems(List.of(TEST_ITEM_ID));

        assertNotNull(items);
        assertTrue(items.isEmpty());
    }

    @Test
    void client_error() throws IOException, InterruptedException {
        when(httpClientMock.send(request, HttpResponse.BodyHandlers.ofString())).thenThrow(IOException.class);

        assertThrows(ItemRepositoryException.class, () -> defaultItemRepository.getItems(List.of(TEST_ITEM_ID)));
    }

    private String getTestItemJson() {
        Item item = new Item(TEST_ITEM_ID, BigDecimal.TEN);

        return new Gson().toJson(item);
    }

}