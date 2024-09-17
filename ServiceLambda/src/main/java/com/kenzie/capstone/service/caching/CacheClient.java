package com.kenzie.capstone.service.caching;

import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;

import javax.inject.Inject;
import java.util.Optional;

public class CacheClient {

    public CacheClient() {}

    public void setValue(String key, int seconds, String value) {
        try (Jedis jedis = DaggerServiceComponent.create().provideJedis()) {
            jedis.setex(key, seconds, value);
        }
    }

    public Optional<String> getValue(String key) {
        try (Jedis jedis = DaggerServiceComponent.create().provideJedis()) {
            return Optional.ofNullable(jedis.get(key));
        }
    }

    public void invalidate(String key) {
        try (Jedis jedis = DaggerServiceComponent.create().provideJedis()) {
            checkNonNullKey(key);
            jedis.del(key);
        }
    }

    private void checkNonNullKey(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null");
        }
    }

    public <T> String serialize(T object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Serialization failed", e);
        }
    }

    public <T> T deserialize(String json, Class<T> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Deserialization failed", e);
        }
    }
}
