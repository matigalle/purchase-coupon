package com.mercadolibre.purchasecoupon.routers;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.mercadolibre.purchasecoupon.dtos.request.CouponRequest;
import com.mercadolibre.purchasecoupon.dtos.response.CouponResponse;
import com.mercadolibre.purchasecoupon.exceptions.BadRequestException;
import com.mercadolibre.purchasecoupon.usecases.GetCouponItemsCombination;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.isNull;

public class DefaultCouponRouter implements CouponRouter {

    private final GetCouponItemsCombination getCouponItemsCombination;

    @Inject
    public DefaultCouponRouter(GetCouponItemsCombination getCouponItemsCombination) {
        this.getCouponItemsCombination = getCouponItemsCombination;
    }

    @Override
    public void handle(@NotNull Context ctx) {
        Gson gson = new Gson();
        CouponRequest couponRequest = gson.fromJson(ctx.body(), CouponRequest.class);

        CouponResponse response = getCouponItemsCombination.apply(GetCouponItemsCombination.Model.builder()
                .itemIds(couponRequest.itemIds())
                .amount(couponRequest.amount())
                .build());

        ctx.result(gson.toJson(response));
    }

    private void validateRequest(CouponRequest couponRequest) {
        if (isNull(couponRequest) || isNull(couponRequest.itemIds()) || isNull(couponRequest.amount())) {
            throw new BadRequestException("Item ids and amount can't be null");
        }
    }

}
