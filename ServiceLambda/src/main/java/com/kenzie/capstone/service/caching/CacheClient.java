package com.kenzie.capstone.service.caching;


import com.kenzie.capstone.service.dependency.DaggerServiceComponent;
import redis.clients.jedis.Jedis;


import java.util.Optional;

public class CacheClient {

    public CacheClient() {}


    public void setValue(String key, int seconds, String value) {
        checkNonNullKey(key);
        Jedis cache = null;
        try {
            cache = DaggerServiceComponent.create().provideJedis();
            cache.setex(key, seconds, value);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (cache != null) {
                cache.close();
            }
        }
    }

    public Optional<String> getValue(String key) {
        checkNonNullKey(key);
        Jedis cache = null;
        try {
            cache = DaggerServiceComponent.create().provideJedis();
            String value = cache.get(key);
            return Optional.ofNullable(value);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        } finally {
            if (cache != null) {
                cache.close();
            }
        }
    }

    public void invalidate(String key) {
        checkNonNullKey(key);

        Jedis cache = null;
        try {
            cache = DaggerServiceComponent.create().provideJedis();
            cache.del(key);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (cache != null) {
                cache.close();
            }
        }
    }

    private void checkNonNullKey(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null");
        }
    }
}
