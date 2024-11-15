package com.mercadolibre.purchasecoupon;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mercadolibre.purchasecoupon.injectors.RepositoryModule;
import com.mercadolibre.purchasecoupon.injectors.RouterModule;
import com.mercadolibre.purchasecoupon.injectors.UseCaseModule;
import com.mercadolibre.purchasecoupon.routers.CouponRouter;
import io.javalin.Javalin;

public class Main {

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(8080);

        addRoutes(app);
    }

    private static void addRoutes(Javalin app) {
        Injector injector = Guice.createInjector(
                new RouterModule(),
                new UseCaseModule(),
                new RepositoryModule());

        app.post("/coupon", injector.getInstance(CouponRouter.class));
    }

}
