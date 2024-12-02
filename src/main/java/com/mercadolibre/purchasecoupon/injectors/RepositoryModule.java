package com.mercadolibre.purchasecoupon.injectors;

import com.google.inject.AbstractModule;
import com.mercadolibre.purchasecoupon.repositories.CacheRepository;
import com.mercadolibre.purchasecoupon.repositories.DefaultItemRepository;
import com.mercadolibre.purchasecoupon.repositories.ItemRepository;
import com.mercadolibre.purchasecoupon.repositories.RedisCacheRepository;

public class RepositoryModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ItemRepository.class).to(DefaultItemRepository.class);
        bind(CacheRepository.class).to(RedisCacheRepository.class);
    }

}
