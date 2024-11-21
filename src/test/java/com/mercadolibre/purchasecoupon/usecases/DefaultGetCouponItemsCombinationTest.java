package com.mercadolibre.purchasecoupon.usecases;

import com.mercadolibre.purchasecoupon.dtos.Item;
import com.mercadolibre.purchasecoupon.dtos.response.CouponResponse;
import com.mercadolibre.purchasecoupon.repositories.ItemRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultGetCouponItemsCombinationTest {

    private static ItemRepository itemRepository;
    private static DefaultGetCouponItemsCombination defaultGetCouponItemsCombination;

    @BeforeAll
    static void setUp() {
        itemRepository = mock(ItemRepository.class);

        defaultGetCouponItemsCombination = new DefaultGetCouponItemsCombination(itemRepository);
    }

    @Test
    void combination_with_all_prices_less_than_coupon_amount() {
        List<Item> items = getTestItems();

        items.forEach(item -> when(itemRepository.getItem(item.id())).thenReturn(item));

        GetCouponItemsCombination.Model model = GetCouponItemsCombination.Model.builder()
                .itemIds(items.stream().map(Item::id).toList())
                .amount(BigDecimal.valueOf(500))
                .build();

        CouponResponse couponResponse = defaultGetCouponItemsCombination.apply(model);

        assertNotNull(couponResponse);
        assertEquals(4, couponResponse.itemIds().size());
        assertTrue(couponResponse.itemIds().containsAll(List.of("MLA1", "MLA2", "MLA4", "MLA5")));
        assertEquals(BigDecimal.valueOf(480), couponResponse.total());
    }

    @Test
    void combination_with_all_prices_greater_than_coupon_amount() {
        List<Item> items = getTestItems();

        items.forEach(item -> when(itemRepository.getItem(item.id())).thenReturn(item));

        GetCouponItemsCombination.Model model = GetCouponItemsCombination.Model.builder()
                .itemIds(items.stream().map(Item::id).toList())
                .amount(BigDecimal.valueOf(70))
                .build();

        CouponResponse couponResponse = defaultGetCouponItemsCombination.apply(model);

        assertNotNull(couponResponse);
        assertTrue(couponResponse.itemIds().isEmpty());
        assertEquals(BigDecimal.valueOf(0), couponResponse.total());
    }

    @Test
    void combination_with_only_one_price_less_than_coupon_amount() {
        List<Item> items = getTestItems();

        items.forEach(item -> when(itemRepository.getItem(item.id())).thenReturn(item));

        GetCouponItemsCombination.Model model = GetCouponItemsCombination.Model.builder()
                .itemIds(items.stream().map(Item::id).toList())
                .amount(BigDecimal.valueOf(85))
                .build();

        CouponResponse couponResponse = defaultGetCouponItemsCombination.apply(model);

        Item affordableItem = items.get(3);

        assertNotNull(couponResponse);
        assertEquals(1, couponResponse.itemIds().size());
        assertTrue(couponResponse.itemIds().contains(affordableItem.id()));
        assertEquals(affordableItem.price(), couponResponse.total());
    }

    private List<Item> getTestItems() {
        return List.of(
                new Item("MLA1", BigDecimal.valueOf(100)),
                new Item("MLA2", BigDecimal.valueOf(210)),
                new Item("MLA3", BigDecimal.valueOf(260)),
                new Item("MLA4", BigDecimal.valueOf(80)),
                new Item("MLA5", BigDecimal.valueOf(90))
        );
    }

}