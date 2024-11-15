package com.mercadolibre.purchasecoupon.injectors;

import com.google.inject.AbstractModule;
import com.mercadolibre.purchasecoupon.routers.CouponRouter;
import com.mercadolibre.purchasecoupon.routers.DefaultCouponRouter;

public class RouterModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CouponRouter.class).to(DefaultCouponRouter.class);
    }

}
