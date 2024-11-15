package com.mercadolibre.purchasecoupon.dtos.request;

import java.math.BigDecimal;
import java.util.List;

public record CouponRequest(List<String> itemIds, BigDecimal amount) {
}
