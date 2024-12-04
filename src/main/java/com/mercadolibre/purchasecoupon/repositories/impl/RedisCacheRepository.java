package com.mercadolibre.purchasecoupon.repositories.impl;

import com.mercadolibre.purchasecoupon.exceptions.CacheRepositoryException;
import com.mercadolibre.purchasecoupon.repositories.CacheRepository;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class RedisCacheRepository implements CacheRepository {

    private static final String ITEMS_COUNTER_KEY = "items_counter";
    private static final Logger log = LoggerFactory.getLogger(RedisCacheRepository.class);

    @Override
    public void incrementCounters(List<String> itemIds) {
        RedisClient client = RedisClient.create(getRedisURI());
        StatefulRedisConnection<String, String> connection = getConnection(client);
        RedisCommands<String, String> commands = connection.sync();

        itemIds.forEach(id -> commands.hincrby(ITEMS_COUNTER_KEY, id, 1));

        connection.close();
        client.shutdown();
    }

    @Override
    public Map<String, String> getItemsFrequency() {
        RedisClient client = RedisClient.create(getRedisURI());
        StatefulRedisConnection<String, String> connection = getConnection(client);
        RedisCommands<String, String> commands = connection.sync();

        Map<String, String> frequencyMap = commands.hgetall(ITEMS_COUNTER_KEY);

        connection.close();
        client.shutdown();

        return frequencyMap;
    }

    private RedisURI getRedisURI() {
        return RedisURI.Builder
                .redis("ec2-18-231-242-241.sa-east-1.compute.amazonaws.com", 6379)
                .withPassword("purchase-coupon-cache".toCharArray())
                .withTimeout(Duration.ofSeconds(10))
                .build();
    }

    private StatefulRedisConnection<String, String> getConnection(RedisClient client) {
        StatefulRedisConnection<String, String> connection;

        try {
            connection = client.connect();
        } catch (Exception e) {
            log.error(e.toString());
            client.shutdown();

            throw new CacheRepositoryException("Unable to connect to the database");
        }

        return connection;
    }

}
