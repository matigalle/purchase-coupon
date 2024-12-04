package com.mercadolibre.purchasecoupon.usecases;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public interface GetTopCouponItems extends Supplier<List<Map<String, Long>>> {
}
