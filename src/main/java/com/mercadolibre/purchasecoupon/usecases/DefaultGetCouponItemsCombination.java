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
 * Given an amount and a list of items, gets their prices and return the combination that maximizes the use of the
 * amount and the total price for that combination.
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

        List<Item> items = getItems(itemIds);
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
     * For each item id, perform asynchronous calls to ItemRepository to get items prices.
     *
     * @param itemIds list of item ids
     * @return list of type Item
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

    /**
     * In each iteration, sums price in index i with prices in index j (from the end of the list, with lower price
     * values), until reaching the max posible sum < or = than couponAmount. Then determines the max between the values
     * obtained in each iteration and returns its combination.
     * If a sum is equal to couponAmount, returns that combination.
     *
     * @param affordableItems list of items ordered by price (descending)
     * @param couponAmount the coupon amount
     * @return a CouponResponse object
     */
    private CouponResponse getOptimalCouponItemsCombination(List<Item> affordableItems, BigDecimal couponAmount) {
        BigDecimal total = BigDecimal.ZERO;
        List<String> itemIdsCombination = new ArrayList<>();
        int lastIndex = affordableItems.size() - 1;

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
                        return new CouponResponse(combinations, max);
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
