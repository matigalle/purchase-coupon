package com.mercadolibre.purchasecoupon;

import io.javalin.Javalin;

public class Main {

    public static void main(String[] args) {
        Javalin.create().start(8080);
    }

}
