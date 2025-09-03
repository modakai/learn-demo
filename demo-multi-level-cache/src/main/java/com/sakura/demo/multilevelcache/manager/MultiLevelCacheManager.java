package com.sakura.demo.multilevelcache.manager;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class MultiLevelCacheManager implements CacheManager {

    private final RedisTemplate<String, Object> redisTemplate;
    private final com.github.benmanes.caffeine.cache.Cache<String, Object> caffeineCache;
    private final Map<String, MultiLevelCache> cacheMap = new ConcurrentHashMap<>();

    public MultiLevelCacheManager(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.caffeineCache = Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public Cache getCache(String name) {
        return cacheMap.computeIfAbsent(name,
                cacheName -> new MultiLevelCache(cacheName, caffeineCache, redisTemplate));
    }

    @Override
    public Collection<String> getCacheNames() {
        return cacheMap.keySet();
    }

    public static class MultiLevelCache implements Cache {
        private final String name;
        private final com.github.benmanes.caffeine.cache.Cache<String, Object> l1Cache;
        private final RedisTemplate<String, Object> redisTemplate;

        public MultiLevelCache(String name,
                               com.github.benmanes.caffeine.cache.Cache<String, Object> l1Cache,
                               RedisTemplate<String, Object> redisTemplate) {
            this.name = name;
            this.l1Cache = l1Cache;
            this.redisTemplate = redisTemplate;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Object getNativeCache() {
            return l1Cache;
        }

        @Override
        public ValueWrapper get(Object key) {
            String cacheKey = generateKey(key);
            // l1 拿
            Object value = l1Cache.getIfPresent(cacheKey);
            if (value != null) {
                return new SimpleValueWrapper(value);
            }
            // l1 没有就从redis中拿
            // L2 Cache (Redis) - 分布式缓存
            value = redisTemplate.opsForValue().get(cacheKey);
            if (value != null) {
                // 回写到L1缓存
                l1Cache.put(cacheKey, value);
                return new SimpleValueWrapper(value);
            }
            return null;
        }

        @Override
        public <T> T get(Object key, Class<T> type) {
            ValueWrapper wrapper = get(key);
            return wrapper != null ? (T) wrapper.get() : null;
        }

        @Override
        public <T> T get(Object key, Callable<T> valueLoader) {
            ValueWrapper wrapper = get(key);
            if (wrapper != null) {
                return (T) wrapper.get();
            }

            try {
                T value = valueLoader.call();
                put(key, value);
                return value;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void put(Object key, Object value) {
            String cacheKey = generateKey(key);
            // 如何返回的是空，会报错
            // 同时写入L1和L2缓存
            l1Cache.put(cacheKey, value);
            redisTemplate.opsForValue().set(cacheKey, value, Duration.ofMinutes(30));
        }

        @Override
        public void evict(Object key) {
            String cacheKey = generateKey(key);

            // 同时清除L1和L2缓存
            l1Cache.invalidate(cacheKey);
            redisTemplate.delete(cacheKey);
        }

        @Override
        public void clear() {
            l1Cache.invalidateAll();
            // 清除Redis中该缓存名称下的所有key
            Set<String> keys = redisTemplate.keys(name + ":*");
            if (!keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        }

        private String generateKey(Object key) {
            return name + ":" + key.toString();
        }
    }
}
