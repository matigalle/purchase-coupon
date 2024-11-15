package com.mercadolibre.purchasecoupon.injectors;

import com.google.inject.AbstractModule;
import com.mercadolibre.purchasecoupon.repositories.DefaultItemRepository;
import com.mercadolibre.purchasecoupon.repositories.ItemRepository;

public class RepositoryModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ItemRepository.class).to(DefaultItemRepository.class);
    }

}
