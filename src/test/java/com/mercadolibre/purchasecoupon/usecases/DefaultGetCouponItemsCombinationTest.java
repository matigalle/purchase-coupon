package com.mercadolibre.purchasecoupon.usecases;

import com.mercadolibre.purchasecoupon.dtos.Item;
import com.mercadolibre.purchasecoupon.dtos.response.CouponResponse;
import com.mercadolibre.purchasecoupon.repositories.CacheRepository;
import com.mercadolibre.purchasecoupon.repositories.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultGetCouponItemsCombinationTest {

    private ItemRepository itemRepository;
    private DefaultGetCouponItemsCombination defaultGetCouponItemsCombination;

    @BeforeEach
    void setUp() {
        itemRepository = mock(ItemRepository.class);
        CacheRepository cacheRepository = mock(CacheRepository.class);

        defaultGetCouponItemsCombination = new DefaultGetCouponItemsCombination(itemRepository, cacheRepository);
    }

    @Test
    void combination_with_all_prices_less_than_coupon_amount() {
        List<Item> items = getTestItems();

        when(itemRepository.getItems(any())).thenReturn(items);

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

        when(itemRepository.getItems(any())).thenReturn(items);

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

        when(itemRepository.getItems(any())).thenReturn(items);

        GetCouponItemsCombination.Model model = GetCouponItemsCombination.Model.builder()
                .itemIds(items.stream().map(Item::id).toList())
                .amount(BigDecimal.valueOf(85))
                .build();

        CouponResponse couponResponse = defaultGetCouponItemsCombination.apply(model);

        Item affordableItem = items.get(3); // The only item with price less than amount

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
