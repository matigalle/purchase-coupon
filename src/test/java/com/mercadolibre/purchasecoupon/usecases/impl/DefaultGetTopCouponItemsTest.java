package com.mercadolibre.purchasecoupon.usecases.impl;

import com.mercadolibre.purchasecoupon.repositories.CacheRepository;
import com.mercadolibre.purchasecoupon.usecases.impl.DefaultGetTopCouponItems;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultGetTopCouponItemsTest {

    private CacheRepository cacheRepository;
    private DefaultGetTopCouponItems defaultGetTopCouponItems;

    @BeforeEach
    void setUp() {
        cacheRepository = mock(CacheRepository.class);

        defaultGetTopCouponItems = new DefaultGetTopCouponItems(cacheRepository);
    }

    @Test
    void filter_five_top_items() {
        Map<String, String> itemsFrequency = getTestItemsFrequency();
        when(cacheRepository.getItemsFrequency()).thenReturn(itemsFrequency);
        List<Map<String, Long>> response = defaultGetTopCouponItems.get();

        List<Map<String, Long>> expected = new ArrayList<>();
        expected.add(Map.of("MLA6", 6L));
        expected.add(Map.of("MLA5", 5L));
        expected.add(Map.of("MLA4", 4L));
        expected.add(Map.of("MLA3", 3L));
        expected.add(Map.of("MLA2", 2L));

        assertNotNull(response);
        assertEquals(5, response.size());
        assertEquals(expected, response);
    }

    @Test
    void no_items_saved() {
        when(cacheRepository.getItemsFrequency()).thenReturn(new HashMap<>());
        List<Map<String, Long>> response = defaultGetTopCouponItems.get();

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    private Map<String, String> getTestItemsFrequency() {
        return Map.of("MLA1", "1",
                      "MLA2", "2",
                      "MLA3", "3",
                      "MLA4", "4",
                      "MLA5", "5",
                      "MLA6", "6");
    }

}