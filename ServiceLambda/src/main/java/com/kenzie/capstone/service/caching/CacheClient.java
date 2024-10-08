package com.kenzie.capstone.service.caching;

import com.kenzie.capstone.service.LambdaService.*;

import redis.clients.jedis.Jedis;

import javax.inject.Inject;
import java.util.Optional;

public class CacheClient {

    @Inject
    public CacheClient() {}

    @Inject
    public void setValue(String key, int seconds, String value) {
        checkNonNullKey(key);
        Jedis jedis = null;
        try {
            jedis = DaggerServiceComponent.provideJedis();
            jedis.setex(key, seconds, value);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Inject
    public Optional<String> getValue(String key) {
        checkNonNullKey(key);
        Jedis jedis = null;
        try {
            jedis = DaggerServiceComponent.provideJedis();
            String value = jedis.get(key);
            return Optional.ofNullable(value);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Inject
    public void invalidate(String key) {
        checkNonNullKey(key);

        Jedis jedis = null;
        try {
            jedis = DaggerServiceComponent.provideJedis();
            jedis.del(key);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Inject
    public void close() {
        Jedis jedis = DaggerServiceComponent.provideJedis();
        if (jedis != null) {
            try {
                System.out.println("Closing Redis connection...");
                jedis.close();
            } catch (Exception e) {
                System.out.println("Error while closing Redis connection: " + e.getMessage());
            }
        }
    }

    private void checkNonNullKey(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null");
        }
    }
}
