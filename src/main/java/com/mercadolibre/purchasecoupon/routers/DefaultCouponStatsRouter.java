package com.mercadolibre.purchasecoupon.routers;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.mercadolibre.purchasecoupon.dtos.request.CouponStatsRequest;
import com.mercadolibre.purchasecoupon.dtos.response.CouponStatsResponse;
import com.mercadolibre.purchasecoupon.exceptions.BadRequestException;
import com.mercadolibre.purchasecoupon.usecases.GetTopFiveCouponItems;
import io.javalin.http.ContentType;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class DefaultCouponStatsRouter implements CouponStatsRouter {

    private final GetTopFiveCouponItems getTopFiveCouponItems;

    @Inject
    public DefaultCouponStatsRouter(GetTopFiveCouponItems getTopFiveCouponItems) {
        this.getTopFiveCouponItems = getTopFiveCouponItems;
    }

    @Override
    public void handle(@NotNull Context ctx) {
        Gson gson = new Gson();
        CouponStatsRequest couponStatsRequest = gson.fromJson(ctx.body(), CouponStatsRequest.class);
        validateRequest(couponStatsRequest);

        CouponStatsResponse response = getTopFiveCouponItems.apply(couponStatsRequest.itemIds());

        ctx.result(gson.toJson(response));
        ctx.contentType(ContentType.APPLICATION_JSON);
    }

    private void validateRequest(CouponStatsRequest request) {
        if (request == null || request.itemIds() == null) {
            throw new BadRequestException("Item ids can't be null");
        }
    }

}