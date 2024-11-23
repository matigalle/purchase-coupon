package com.mercadolibre.purchasecoupon.routers;

import com.mercadolibre.purchasecoupon.dtos.response.CouponStatsResponse;
import com.mercadolibre.purchasecoupon.exceptions.BadRequestException;
import com.mercadolibre.purchasecoupon.usecases.GetTopCouponItems;
import io.javalin.http.Context;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultCouponStatsRouterTest {

    private static DefaultCouponStatsRouter defaultCouponStatsRouter;

    @BeforeAll
    static void setUp() {
        GetTopCouponItems getTopCouponItems = mock(GetTopCouponItems.class);
        CouponStatsResponse couponStatsResponse = new CouponStatsResponse(Map.of("MLA1", 2, "MLA2", 1));
        when(getTopCouponItems.apply(any())).thenReturn(couponStatsResponse);

        defaultCouponStatsRouter = new DefaultCouponStatsRouter(getTopCouponItems);
    }

    @Test
    void response_ok() {
        Context context = mock(Context.class);
        String requestBody = """
                {
                    "item_ids": ["MLA1", "MLA1", "MLA2"]
                }
                """;

        when(context.body()).thenReturn(requestBody);

        assertDoesNotThrow(() -> defaultCouponStatsRouter.handle(context));
    }

    @Test
    void bad_request() {
        Context context = mock(Context.class);
        when(context.body()).thenReturn("{}");

        assertThrows(BadRequestException.class, () -> defaultCouponStatsRouter.handle(context));
    }

}
