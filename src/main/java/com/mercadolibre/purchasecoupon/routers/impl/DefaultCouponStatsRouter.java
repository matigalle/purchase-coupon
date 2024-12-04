package com.mercadolibre.purchasecoupon.routers.impl;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.mercadolibre.purchasecoupon.routers.CouponStatsRouter;
import com.mercadolibre.purchasecoupon.usecases.GetTopCouponItems;
import io.javalin.http.ContentType;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class DefaultCouponStatsRouter implements CouponStatsRouter {

    private final GetTopCouponItems getTopCouponItems;

    @Inject
    public DefaultCouponStatsRouter(GetTopCouponItems getTopCouponItems) {
        this.getTopCouponItems = getTopCouponItems;
    }

    @Override
    public void handle(@NotNull Context ctx) {
        List<Map<String, Long>> response = getTopCouponItems.get();

        ctx.result(new Gson().toJson(response));
        ctx.contentType(ContentType.APPLICATION_JSON);
    }

}
