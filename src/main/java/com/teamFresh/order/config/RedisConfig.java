package com.teamFresh.order.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

    @Bean
    RedissonClient redissonClient() {
        Config config = new Config();
        String address = "redis://" + "localhost" + ":" + 6380;
        config.useSingleServer().setAddress(address);
        return Redisson.create(config);
    }
}
