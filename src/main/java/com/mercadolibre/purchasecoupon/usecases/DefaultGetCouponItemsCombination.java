package com.mercadolibre.purchasecoupon.usecases;

import com.google.inject.Inject;
import com.mercadolibre.purchasecoupon.dtos.Item;
import com.mercadolibre.purchasecoupon.dtos.response.CouponResponse;
import com.mercadolibre.purchasecoupon.exceptions.GetCouponItemsCombinationException;
import com.mercadolibre.purchasecoupon.repositories.ItemRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Given a list of items and an amount, get their prices and return the combination that maximizes the use of the amount
 * and the total price for this combination.
 */
public class DefaultGetCouponItemsCombination implements GetCouponItemsCombination {

    private final ItemRepository itemRepository;

    @Inject
    public DefaultGetCouponItemsCombination(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public CouponResponse apply(Model model) {
        // Filter repeated item ids
        List<String> itemIds = model.getItemIds().stream().distinct().toList();

        List<Item> items = getItems(model.getItemIds());
        BigDecimal couponAmount = model.getAmount();

        // Filter items with a price greater than coupon amount
        List<Item> affordableItems = items.stream().sorted(Comparator.comparing(Item::price).reversed())
                .dropWhile(item -> item.price().compareTo(couponAmount) > 0)
                .toList();

        List<String> itemIdsCombination = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        if (!affordableItems.isEmpty()) {
            Item firstItem = affordableItems.get(0);
            BigDecimal maxPrice = firstItem.price();

            if (maxPrice.equals(couponAmount) || affordableItems.size() == 1) {
                itemIdsCombination.add(firstItem.id());
                total = firstItem.price();
            } else {
                return getOptimalCouponItemsCombination(affordableItems, couponAmount);
            }
        }

        return new CouponResponse(itemIdsCombination, total);
    }

    /**
     * Perform asynchronous requests to items API for each item id to get their prices.
     *
     * @param itemIds item ids
     * @return a list of type Item
     */
    private List<Item> getItems(List<String> itemIds) {
        List<CompletableFuture<Item>> futures = new ArrayList<>();

        itemIds.forEach(itemId -> {
            CompletableFuture<Item> future = CompletableFuture.supplyAsync(() -> itemRepository.getItem(itemId));
            futures.add(future);
        });

        try {
            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .thenApply(v ->
                            futures.stream()
                                    .map(CompletableFuture::join)
                                    .toList())
                    .get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Optional<Throwable> cause = Optional.ofNullable(e.getCause());

            throw new GetCouponItemsCombinationException(cause.orElse(e));
        }
    }

    private CouponResponse getOptimalCouponItemsCombination(List<Item> affordableItems, BigDecimal couponAmount) {
        BigDecimal total = BigDecimal.ZERO;
        List<String> itemIdsCombination = new ArrayList<>();
        int lastIndex = affordableItems.size() - 1;

        // Compare max values with min values to determine the max posible sum for each index
        for (int i = 0; i < lastIndex; i++) {
            Item leftItem = affordableItems.get(i);
            BigDecimal max = leftItem.price();
            List<String> combinations = new ArrayList<>();
            combinations.add(leftItem.id());

            for (int j = lastIndex; i < j; j--) {
                Item rightItem = affordableItems.get(j);
                BigDecimal sum = max.add(rightItem.price());

                if (sum.compareTo(couponAmount) > 0) {
                    // Next sums would also be greater than couponAmount, variable max has the max posible for index i
                    break;
                } else {
                    max = sum;
                    combinations.add(rightItem.id());

                    if (max.equals(couponAmount)) {
                        // Variable max has the optimal sum
                        i = lastIndex;

                        break;
                    }
                }
            }

            if (max.compareTo(total) > 0) {
                total = max;
                itemIdsCombination = combinations;
            }
        }

        return new CouponResponse(itemIdsCombination, total);
    }

}
