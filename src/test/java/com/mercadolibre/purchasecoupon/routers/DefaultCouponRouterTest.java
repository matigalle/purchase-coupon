package com.mercadolibre.purchasecoupon.routers;

import com.google.gson.Gson;
import com.mercadolibre.purchasecoupon.dtos.request.CouponRequest;
import com.mercadolibre.purchasecoupon.dtos.response.CouponResponse;
import com.mercadolibre.purchasecoupon.exceptions.BadRequestException;
import com.mercadolibre.purchasecoupon.usecases.GetCouponItemsCombination;
import io.javalin.http.Context;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultCouponRouterTest {

    private static GetCouponItemsCombination getCouponItemsCombination;
    private static CouponResponse couponResponse;
    private static DefaultCouponRouter defaultCouponRouter;

    @BeforeAll
    static void setUp() {
        getCouponItemsCombination = mock(GetCouponItemsCombination.class);

        when(getCouponItemsCombination.apply(any())).thenReturn(couponResponse);

        defaultCouponRouter = new DefaultCouponRouter(getCouponItemsCombination);
    }

    @Test
    void response_ok() {
        Context context = mock(Context.class);
        CouponRequest couponRequest = new CouponRequest(getTestItemIds(), BigDecimal.TEN);
        Gson gson = new Gson();
        String requestBody = gson.toJson(couponRequest);
        couponResponse = new CouponResponse(getTestItemIds(), BigDecimal.TEN);

        when(context.body()).thenReturn(requestBody);
        when(getCouponItemsCombination.apply(any())).thenReturn(couponResponse);

        defaultCouponRouter.handle(context);
    }

    @Test
    void bad_request() {
        Context context = mock(Context.class);
        CouponRequest couponRequest = new CouponRequest(null, null);
        String requestBody = new Gson().toJson(couponRequest);

        when(context.body()).thenReturn(requestBody);

        assertThrows(BadRequestException.class, () -> defaultCouponRouter.handle(context));
    }

    private static List<String> getTestItemIds() {
        return List.of("MLA1", "MLA2");
    }

}