package com.mercadolibre.purchasecoupon.repositories;

import java.util.List;

public interface CacheRepository {

    void incrementCounters(List<String> itemIds);

}
