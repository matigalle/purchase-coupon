package com.mercadolibre.purchasecoupon.dtos.response;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public record CouponStatsResponse(@SerializedName("top_items") Map<String, Integer> topItems) {
}
