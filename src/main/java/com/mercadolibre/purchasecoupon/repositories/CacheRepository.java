package com.mercadolibre.purchasecoupon.repositories;

import java.util.List;
import java.util.Map;

public interface CacheRepository {

    void incrementCounters(List<String> itemIds);

    Map<String, String> getItemsFrequency();

}
