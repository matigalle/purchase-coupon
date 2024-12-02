package com.mercadolibre.purchasecoupon.repositories;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisConnectionException;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class RedisCacheRepository implements CacheRepository {

    private static final Logger log = LoggerFactory.getLogger(RedisCacheRepository.class);

    private final RedisURI uri;

    public RedisCacheRepository() {
        uri = getRedisURI();
    }

    @Override
    public void incrementCounters(List<String> itemIds) {
        RedisClient client = RedisClient.create(uri);
        StatefulRedisConnection<String, String> connection;

        try {
            connection = client.connect();
        } catch (RedisConnectionException e) {
            log.error(e.toString());
            client.shutdown();

            throw e;
        }

        RedisCommands<String, String> commands = connection.sync();

        itemIds.forEach(commands::incr);

        connection.close();
        client.shutdown();
    }

    private RedisURI getRedisURI() {
        return RedisURI.Builder
                .redis("redis-cache-ndoafj.serverless.sae1.cache.amazonaws.com", 6379)
                .withTimeout(Duration.ofSeconds(10))
                .build();
    }

}
