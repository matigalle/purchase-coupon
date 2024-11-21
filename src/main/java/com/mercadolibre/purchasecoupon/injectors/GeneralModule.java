package com.mercadolibre.purchasecoupon.injectors;

import com.google.inject.AbstractModule;

import java.net.http.HttpClient;
import java.time.Duration;

public class GeneralModule extends AbstractModule {

    @Override
    protected void configure() {
        HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2)).build();
        bind(HttpClient.class).toInstance(httpClient);
    }

}
