package com.mercadolibre.purchasecoupon.usecases;

import com.mercadolibre.purchasecoupon.dtos.response.CouponStatsResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultGetTopFiveCouponItems implements GetTopFiveCouponItems {

    @Override
    public CouponStatsResponse apply(List<String> itemIds) {
        Map<String, Integer> result = new HashMap<>();

        return new CouponStatsResponse(result);
    }

}
