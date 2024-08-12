package com.sparta.msa_exam.order.core.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;


import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class RedisCacheConfig {

        @Bean
        public RedisCacheManager cacheManager(
                RedisConnectionFactory redisConnectionFactory
        ) {
            // 설정 구성을 먼저 진행한다.
            // Redis를 이용해서 Spring Cache를 사용할때
            // Redis 관련 설정을 모아두는 클래스
            RedisCacheConfiguration configuration = RedisCacheConfiguration
                    .defaultCacheConfig()
                    .disableCachingNullValues() // null을 캐싱하는지
                    .entryTtl(Duration.ofSeconds(60)) // 기본캐시유지시간 (Time To Live (Ttl)
                    .computePrefixWith(CacheKeyPrefix.simple()) // 캐시를 구분하는 접두사 설정
                    .serializeValuesWith( // 캐시에 저장할 값을 어떻게 직렬화 / 역직렬화 할 것인지
                            SerializationPair.fromSerializer(RedisSerializer.java())
                    );

            return RedisCacheManager
                    .builder(redisConnectionFactory)
                    .cacheDefaults(configuration)
                    .build();
        }


        @Bean
        public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
            RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
            redisTemplate.setConnectionFactory(connectionFactory);
            return redisTemplate;
        }
    }
