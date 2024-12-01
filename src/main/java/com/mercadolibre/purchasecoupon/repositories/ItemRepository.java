package com.mercadolibre.purchasecoupon.repositories;

import com.mercadolibre.purchasecoupon.dtos.Item;

import java.util.List;

public interface ItemRepository {

    List<Item> getItems(List<String> ids);

}
