package com.mercadolibre.purchasecoupon.routers;

import com.mercadolibre.purchasecoupon.usecases.GetTopCouponItems;
import io.javalin.http.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultCouponStatsRouterTest {

    private DefaultCouponStatsRouter defaultCouponStatsRouter;

    @BeforeEach
    void setUp() {
        GetTopCouponItems getTopCouponItems = mock(GetTopCouponItems.class);
        when(getTopCouponItems.get()).thenReturn(getTestTopItems());

        defaultCouponStatsRouter = new DefaultCouponStatsRouter(getTopCouponItems);
    }

    @Test
    void ok_response() {
        Context context = mock(Context.class);

        assertDoesNotThrow(() -> defaultCouponStatsRouter.handle(context));
    }

    private List<Map<String, Long>> getTestTopItems() {
        return List.of(Map.of("MLA1", 2L),
                       Map.of("MLA2", 1L));
    }

}
