package com.mercadolibre.purchasecoupon.dtos.response;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

public record CouponResponse(@SerializedName("item_ids") List<String> itemIds, BigDecimal total) {
}
