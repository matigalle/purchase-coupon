package com.mercadolibre.purchasecoupon.dtos.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public record CouponStatsRequest(@SerializedName("item_ids") List<String> itemIds) {
}
