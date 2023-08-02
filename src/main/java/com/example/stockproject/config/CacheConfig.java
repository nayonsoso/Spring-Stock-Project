package com.example.stockproject.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class CacheConfig {
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    // 레디스 연결 설정 정보를 담는 객체를 반환하는 함수
    @Bean
    public RedisConnectionFactory redisConnectionFactory (){
        // single, cluster, 등 레디스의 속성을 설정하는 부분
        RedisStandaloneConfiguration conf = new RedisStandaloneConfiguration();
        conf.setHostName(host);
        conf.setPort(port);
        // conf.setPassword(); // 레디스 서버에 패스워드가 걸려있는 경우
        return new LettuceConnectionFactory(conf);
    }

    // 레디스를 캐시와 연결시키기 위한 함수
    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory){
        // 레디스 서버는 외부 서버이므로 여기에 데이터를 저장할 때는
        // 우리 데이터 형식이면서, 외부에서도 저장될 수 있도록 byte 로 직렬화(serialize) 해야한다.
        RedisCacheConfiguration conf = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(conf)
                .build();
    }
}
