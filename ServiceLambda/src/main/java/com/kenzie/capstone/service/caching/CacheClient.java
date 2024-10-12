package com.kenzie.capstone.service.caching;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PreDestroy;

@Component
public class CacheClient {

    private final JedisPool jedisPool;

    @Autowired
    public CacheClient(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }


    public void setValue(String key, int seconds, String value) {
        checkNonNullKey(key);
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.setex(key, seconds, value);
        } catch (Exception e) {
            System.err.println("Error setting value to Redis: " + e.getMessage());
            throw e;
        }
    }

    public String getValue(String key) {
        checkNonNullKey(key);
        try (Jedis jedis = jedisPool.getResource()) {
            String value = jedis.get(key);
        return value != null && !value.isEmpty() ? value : null;
        } catch (Exception e) {
            System.err.println("Error getting value from Redis: " + e.getMessage());
            throw e;
        }
    }


    public void invalidate(String key) {
        checkNonNullKey(key);
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(key);
        } catch (Exception e) {
            System.err.println("Error invalidating key: " + e.getMessage());
            throw e;
        }
    }

    @PreDestroy
    public void closePool() {
        if (jedisPool != null) {
            try {
                System.out.println("Closing Jedis pool");
                jedisPool.close();
            } catch (Exception e) {
                System.err.println("Error closing Jedis pool: " + e.getMessage());
            }
        }
    }

    private void checkNonNullKey(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null");
        }
    }
}
