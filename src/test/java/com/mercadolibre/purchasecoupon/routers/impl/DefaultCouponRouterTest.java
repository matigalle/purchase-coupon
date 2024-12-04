package com.mercadolibre.purchasecoupon.routers.impl;

import com.mercadolibre.purchasecoupon.dtos.response.CouponResponse;
import com.mercadolibre.purchasecoupon.exceptions.BadRequestException;
import com.mercadolibre.purchasecoupon.routers.impl.DefaultCouponRouter;
import com.mercadolibre.purchasecoupon.usecases.GetCouponItemsCombination;
import io.javalin.http.Context;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultCouponRouterTest {

    private static DefaultCouponRouter defaultCouponRouter;

    @BeforeAll
    static void setUp() {
        GetCouponItemsCombination getCouponItemsCombination = mock(GetCouponItemsCombination.class);
        CouponResponse couponResponse = new CouponResponse(List.of("MLA1", "MLA2"), BigDecimal.valueOf(10000));
        when(getCouponItemsCombination.apply(any())).thenReturn(couponResponse);

        defaultCouponRouter = new DefaultCouponRouter(getCouponItemsCombination);
    }

    @Test
    void response_ok() {
        Context context = mock(Context.class);
        String requestBody = """
                {
                    "item_ids": ["MLA1", "MLA2"],
                    "amount": 10000
                }
                """;

        when(context.body()).thenReturn(requestBody);

        assertDoesNotThrow(() -> defaultCouponRouter.handle(context));
    }

    @Test
    void bad_request() {
        Context context = mock(Context.class);
        when(context.body()).thenReturn("{}");

        assertThrows(BadRequestException.class, () -> defaultCouponRouter.handle(context));
    }

}
