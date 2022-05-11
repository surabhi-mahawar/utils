package com.uci.utils.cache.service;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class RedisCacheService {
	private RedisTemplate<String, Object> redisTemplate;

    /**
     * Get is redis cache enabled or disabled
     * @return
     */
    private Boolean enabledRedis() {
        if(System.getenv("REDIS_ENABLED") != null && System.getenv("REDIS_ENABLED").equalsIgnoreCase("false")) {
            return false;
        }
        return true;
    }
	
	/**
     * Get XMessageDao Object from cache by key
     * @param key
     * @return
     */
    public Object getMinioCDNCache(String key) {
        return getCache("MinioCDN", key);
    }
    
    /**
     * Set XMessageDao Object in cache by key
     * @param key
     * @param value
     */
    public void setMinioCDNCache(String key, Object value) {
        setCache("MinioCDN", key, value);
    }
    
    /**
     * Get Fusion Auth User ID Object from cache by key
     * @param key
     * @return
     */
    public Object getFAUserIDForAppCache(String key) {
        return getCache("FAUserID", key);
    }
    
    /**
     * Set Fusion Auth User ID Object in cache by key
     * @param key
     * @param value
     */
    public void setFAUserIDForAppCache(String key, Object value) {
        setCache("FAUserID", key, value);
    }
    
    /**
     * Delete Fusion Auth User ID Object in cache by key
     * @param key
     */
    public void deleteFAUserIDForAppCache(String key) {
        deleteCache("FAUserID", key);
    }

    /**
     * Get Language Object from cache by name
     * @param name
     * @return
     */
    public Object getLanguageCache(String name) {
        return getCache("Language", name);
    }
    
    /**
     * Set Language Object in cache by name
     * @param name
     * @param value
     */
    public void setLanguageCache(String name, Object value) {
        setCache("Language", name, value);
    }
    
    /**
     * Delete Language Object in cache by name
     * @param name
     */
    public void deleteLanguageCache(String name) {
        deleteCache("Language", name);
    }
    
    /**
     * Get XMessageDao Object from cache by key
     * @param key
     * @return
     */
    public Object getXMessageDaoCache(String key) {
        return getCache("XMessageDAO", key);
    }
    
    /**
     * Set XMessageDao Object in cache by key
     * @param key
     * @param value
     */
    public void setXMessageDaoCache(String key, Object value) {
        setCache("XMessageDAO", key, value);
    }
    
    /**
     * Delete XMessageDao Object in cache by key
     * @param key
     */
    public void deleteXMessageDaoCache(String key) {
        deleteCache("XMessageDAO", key);
    }
    
    /**
     * Get all caches
     * @param key
     * @param name
     * @return
     */
//    private Object getAllCache() {
//    	ListOperations<String, Object> listOperations = redisTemplate.opsForList();
////        log.info("Find redis cache by key: "+redisKeyWithPrefix(prefix, key));
//    	return result;
//    }
    

    /**
     * Get cache by prefix & key
     * @param prefix
     * @param key
     * @return
     */
    private Object getCache(String prefix, String key) {
    	if(enabledRedis()) {
            ValueOperations<String, Object> valOperations = redisTemplate.opsForValue();
            Object result = valOperations.get(redisKeyWithPrefix(prefix, key));
            log.info("Find redis cache by key: "+redisKeyWithPrefix(prefix, key));
            return result;
        }
        return null;
    }
    
    /**
     * Set cache value by prefix & key
     * @param prefix
     * @param key
     * @param value
     */
    private void setCache(String prefix, String key, Object value) {
        if (enabledRedis()) {
            log.info("Set redis cache for key: " + redisKeyWithPrefix(prefix, key));
            ValueOperations<String, Object> valOperations = redisTemplate.opsForValue();
            try {
                valOperations.set(redisKeyWithPrefix(prefix, key), value);
            } catch (Exception e) {
                /* If redis cache not able to set, delete cache */
                redisTemplate.delete(redisKeyWithPrefix(prefix, key));
                e.printStackTrace();

                log.info("Exception in redis setCache: " + e.getMessage());
            }
        }
    }

    /**
     * Set cache value by prefix & key
     * @param prefix
     * @param key
     */
    public void deleteCache(String prefix, String key) {
        if (enabledRedis()) {
            log.info("Delete redis cache for key: " + redisKeyWithPrefix(prefix, key));
            ValueOperations<String, Object> valOperations = redisTemplate.opsForValue();
            try {
                redisTemplate.delete(redisKeyWithPrefix(prefix, key));
            } catch (Exception e) {
                log.info("Exception in redis deleteCache: " + e.getMessage());
            }
        }
    }
    
    /**
     * Add env prefix with cache key
     * @param key
     * @return
     */
    private String redisKeyWithPrefix(String prefix, String key) {
    	return System.getenv("ENV")+"-"+prefix+": "+key;
    }
}
