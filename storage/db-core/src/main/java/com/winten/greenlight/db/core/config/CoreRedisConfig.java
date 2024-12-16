package com.winten.greenlight.db.core.config;

import com.winten.greenlight.db.core.RedisDataStore;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class CoreRedisConfig {

    @Bean
    public RedisDataStore redisDataStore(RedisProperties properties) {
        var factory = createReactiveRedisConnectionFactory(properties);
        var template = createReactiveRedisTemplate(factory);
        return new RedisDataStore(template);
    }

    private ReactiveRedisConnectionFactory createReactiveRedisConnectionFactory(RedisProperties properties) {
        var config = new RedisStandaloneConfiguration(properties.getHost(), properties.getPort());
        config.setPassword(properties.getPassword());
        var factory = new LettuceConnectionFactory(config);
        factory.start();
        return factory;
    }

    private ReactiveRedisTemplate<String, String> createReactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        StringRedisSerializer serializer = new StringRedisSerializer();
        RedisSerializationContext<String, String> serializationContext = RedisSerializationContext
                .<String, String>newSerializationContext()
                .key(serializer)
                .value(serializer)
                .hashKey(serializer)
                .hashValue(serializer)
                .build();
        return new ReactiveRedisTemplate<>(factory, serializationContext);
    }


}
