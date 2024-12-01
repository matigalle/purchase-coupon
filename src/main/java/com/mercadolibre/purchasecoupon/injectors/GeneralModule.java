package com.mercadolibre.purchasecoupon.injectors;

import com.google.inject.AbstractModule;

import java.net.http.HttpClient;

public class GeneralModule extends AbstractModule {

    @Override
    protected void configure() {
        HttpClient httpClient = HttpClient.newBuilder().build();
        bind(HttpClient.class).toInstance(httpClient);
    }

}
