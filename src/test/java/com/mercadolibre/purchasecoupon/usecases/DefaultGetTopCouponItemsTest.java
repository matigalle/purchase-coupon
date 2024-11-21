package com.mercadolibre.purchasecoupon.usecases;

import com.mercadolibre.purchasecoupon.dtos.response.CouponStatsResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DefaultGetTopCouponItemsTest {

    private static DefaultGetTopCouponItems defaultGetTopCouponItems;

    @BeforeAll
    static void setUp() {
        defaultGetTopCouponItems = new DefaultGetTopCouponItems();
    }

    @Test
    void five_items_repeated_twice() {
        GetTopCouponItems.Model model = GetTopCouponItems.Model.builder()
                .itemIds(getTestItemIds())
                .top(5)
                .build();

        CouponStatsResponse couponStatsResponse = defaultGetTopCouponItems.apply(model);

        Map<String, Integer> expected = Map.of("MLA1", 2, "MLA2", 2, "MLA3", 2, "MLA4", 2, "MLA5", 2);

        assertNotNull(couponStatsResponse);
        assertEquals(5, couponStatsResponse.topItems().size());
        assertEquals(expected, couponStatsResponse.topItems());
    }

    @Test
    void empty_items() {
        GetTopCouponItems.Model model = GetTopCouponItems.Model.builder()
                .itemIds(Collections.emptyList())
                .top(5)
                .build();

        CouponStatsResponse couponStatsResponse = defaultGetTopCouponItems.apply(model);

        assertNotNull(couponStatsResponse);
        assertTrue(couponStatsResponse.topItems().isEmpty());
    }

    private List<String> getTestItemIds() {
        return List.of("MLA1", "MLA1", "MLA2", "MLA2", "MLA3", "MLA3", "MLA4", "MLA4", "MLA5", "MLA5", "MLA6");
    }

}