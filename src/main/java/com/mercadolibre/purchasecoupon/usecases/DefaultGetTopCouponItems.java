package com.mercadolibre.purchasecoupon.usecases;

import com.google.inject.Inject;
import com.mercadolibre.purchasecoupon.repositories.CacheRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Gets the top 5 most repeated items along with their frequency.
 */
public class DefaultGetTopCouponItems implements GetTopCouponItems {

    private final CacheRepository cacheRepository;

    @Inject
    public DefaultGetTopCouponItems(CacheRepository cacheRepository) {
        this.cacheRepository = cacheRepository;
    }

    @Override
    public List<Map<String, Long>> get() {
        Map<String, String> frequencyMap = cacheRepository.getItemsFrequency();

        return frequencyMap.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(5)
                    .map(entry -> Map.of(entry.getKey(), Long.valueOf(entry.getValue())))
                    .toList();
    }

}
