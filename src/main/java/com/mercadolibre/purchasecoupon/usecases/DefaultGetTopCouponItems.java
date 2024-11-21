package com.mercadolibre.purchasecoupon.usecases;

import com.mercadolibre.purchasecoupon.dtos.response.CouponStatsResponse;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Given a list of item ids, return the top "n" most repeated items along with their frequency, where "n" is determined
 * by the param top.
 */
public class DefaultGetTopCouponItems implements GetTopCouponItems {

    @Override
    public CouponStatsResponse apply(Model model) {
        Map<String, Integer> frequencyMap = new HashMap<>();

        model.getItemIds().forEach(id -> {
            if (frequencyMap.containsKey(id)) {
                Integer frequency = frequencyMap.get(id);
                frequencyMap.put(id, frequency + 1);
            } else {
                frequencyMap.put(id, 1);
            }
        });

        if (frequencyMap.size() <= model.getTop()) {
            return new CouponStatsResponse(frequencyMap);
        }

        Map<String, Integer> topItemsMap = new HashMap<>();

        frequencyMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(model.getTop())
                .forEach(entry -> topItemsMap.put(entry.getKey(), entry.getValue()));

        return new CouponStatsResponse(topItemsMap);
    }

}
