package com.mercadolibre.purchasecoupon.usecases;

import com.mercadolibre.purchasecoupon.dtos.response.CouponStatsResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.function.Function;

public interface GetTopCouponItems extends Function<GetTopCouponItems.Model, CouponStatsResponse> {

    @Builder
    @Getter
    class Model {
        private final List<String> itemIds;
        private final int top;
    }

}
