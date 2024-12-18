package com.mercadolibre.purchasecoupon;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mercadolibre.purchasecoupon.exceptions.BadRequestException;
import com.mercadolibre.purchasecoupon.injectors.GeneralModule;
import com.mercadolibre.purchasecoupon.injectors.RepositoryModule;
import com.mercadolibre.purchasecoupon.injectors.RouterModule;
import com.mercadolibre.purchasecoupon.injectors.UseCaseModule;
import com.mercadolibre.purchasecoupon.routers.CouponRouter;
import com.mercadolibre.purchasecoupon.routers.CouponStatsRouter;
import io.javalin.Javalin;
import io.javalin.http.ContentType;

import static jakarta.servlet.http.HttpServletResponse.*;

public class Main {

    public static void main(String[] args) {
        Javalin app = Javalin.create();

        addRoutes(app);
        addExceptionsHandling(app);
        app.start(8080);
    }

    private static void addRoutes(Javalin app) {
        Injector injector = Guice.createInjector(
                new RouterModule(),
                new UseCaseModule(),
                new RepositoryModule(),
                new GeneralModule());

        app.get("/", ctx -> ctx.status(SC_OK));
        app.post("/coupon", injector.getInstance(CouponRouter.class));
        app.get("/coupon/stats", injector.getInstance(CouponStatsRouter.class));
    }

    private static void addExceptionsHandling(Javalin app) {
        app.exception(RuntimeException.class, (exception, ctx) -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("error", exception.getMessage());

            ctx.result(new Gson().toJson(jsonObject));
            ctx.contentType(ContentType.APPLICATION_JSON);
            ctx.status(getStatusCode(exception));
        });
    }

    private static int getStatusCode(Exception e) {
        if (e instanceof BadRequestException || e instanceof JsonSyntaxException) {
            return SC_BAD_REQUEST;
        }

        return SC_INTERNAL_SERVER_ERROR;
    }

}
