package com.kenzie.appserver.config;




import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.annotation.EnableCaching;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;



@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public JedisPool jedisPool() {
        return new JedisPool(new JedisPoolConfig(), "localhost", 6379);
    }

    @Bean
    public CacheClient cacheClient(JedisPool jedisPool) {
        return new CacheClient();
    }
}
