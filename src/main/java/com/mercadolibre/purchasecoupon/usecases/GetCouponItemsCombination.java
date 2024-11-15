package com.mercadolibre.purchasecoupon.usecases;

import com.mercadolibre.purchasecoupon.dtos.response.CouponResponse;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

public interface GetCouponItemsCombination extends Function<GetCouponItemsCombination.Model, CouponResponse> {

    @Builder
    @Getter
    class Model {
        private List<String> itemIds;
        private BigDecimal amount;
    }

}
