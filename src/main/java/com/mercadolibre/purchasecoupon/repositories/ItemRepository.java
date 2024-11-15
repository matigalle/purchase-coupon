package com.mercadolibre.purchasecoupon.repositories;

import com.mercadolibre.purchasecoupon.dtos.Item;

public interface ItemRepository {

    Item getItem(String id);

}
