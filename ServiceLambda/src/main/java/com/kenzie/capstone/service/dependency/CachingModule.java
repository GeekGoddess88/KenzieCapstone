package com.kenzie.capstone.service.dependency;

import com.kenzie.capstone.service.caching.CacheClient;
import dagger.Module;
import dagger.Provides;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Module
public class CachingModule {

    @Provides
    public static Jedis provideJedisConfig() {
        String redisUrl = System.getenv("JEDIS_URL");

        if (redisUrl == null && !redisUrl.isEmpty()) {
            System.out.println("Providing redis " + redisUrl);
            return new Jedis(redisUrl, 6379, 20000);
        } else if ("true".equals(System.getenv("AWS_SAM_LOCAL"))) {
            JedisPool pool = new JedisPool(new JedisPoolConfig(), "redis-stack", 6379, 20000);
            try {
                return pool.getResource();
            } catch (Exception e) {
                throw new IllegalStateException("Could not connect to redis container.");
            }
        } else {
            System.out.println("Providing redis " + redisUrl);
            return new JedisPool(new JedisPoolConfig(), "localhost", 6379, 20000).getResource();
        }
    }

    @Provides
    public CacheClient provideCacheClient(Jedis jedis) {
        return new CacheClient(jedis);
    }
}
