package com.mercadolibre.purchasecoupon.usecases;

import com.mercadolibre.purchasecoupon.dtos.response.CouponStatsResponse;

import java.util.List;
import java.util.function.Function;

public interface GetTopFiveCouponItems extends Function<List<String>, CouponStatsResponse> {
}
