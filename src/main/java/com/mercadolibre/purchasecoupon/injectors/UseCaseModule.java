package com.mercadolibre.purchasecoupon.injectors;

import com.google.inject.AbstractModule;
import com.mercadolibre.purchasecoupon.usecases.impl.DefaultGetCouponItemsCombination;
import com.mercadolibre.purchasecoupon.usecases.impl.DefaultGetTopCouponItems;
import com.mercadolibre.purchasecoupon.usecases.GetCouponItemsCombination;
import com.mercadolibre.purchasecoupon.usecases.GetTopCouponItems;

public class UseCaseModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(GetCouponItemsCombination.class).to(DefaultGetCouponItemsCombination.class);
        bind(GetTopCouponItems.class).to(DefaultGetTopCouponItems.class);
    }

}
