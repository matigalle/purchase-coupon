package com.mercadolibre.purchasecoupon.injectors;

import com.google.inject.AbstractModule;
import com.mercadolibre.purchasecoupon.routers.CouponRouter;
import com.mercadolibre.purchasecoupon.routers.CouponStatsRouter;
import com.mercadolibre.purchasecoupon.routers.impl.DefaultCouponRouter;
import com.mercadolibre.purchasecoupon.routers.impl.DefaultCouponStatsRouter;

public class RouterModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CouponRouter.class).to(DefaultCouponRouter.class);
        bind(CouponStatsRouter.class).to(DefaultCouponStatsRouter.class);
    }

}
