package com.mercadolibre.purchasecoupon.usecases;

import com.google.inject.Inject;
import com.mercadolibre.purchasecoupon.dtos.response.CouponResponse;
import com.mercadolibre.purchasecoupon.repositories.ItemRepository;

import java.math.BigDecimal;
import java.util.List;

public class DefaultGetCouponItemsCombination implements GetCouponItemsCombination {

    private final ItemRepository itemRepository;

    @Inject
    public DefaultGetCouponItemsCombination(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public CouponResponse apply(Model model) {
        List<String> itemsCombination = List.of("MLA1", "MLA2", "MLA4", "MLA5");
        BigDecimal total = BigDecimal.valueOf(480);

        return new CouponResponse(itemsCombination, total);
    }

}
