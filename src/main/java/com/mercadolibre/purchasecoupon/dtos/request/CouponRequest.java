package com.mercadolibre.purchasecoupon.dtos.request;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

public record CouponRequest(@SerializedName("item_ids") List<String> itemIds, BigDecimal amount) {
}
