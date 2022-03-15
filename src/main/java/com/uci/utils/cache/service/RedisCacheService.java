package com.uci.utils.cache.service;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
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
     * Get XMessageDao Object from cache by name
     * @param name
     * @return
     */
    public Object getMinioCDNCache(String name) {
        return getCache("MinioCDN", name);
    }
    
    /**
     * Set XMessageDao Object in cache by name
     * @param name
     * @param value
     */
    public void setMinioCDNCache(String name, Object value) {
        setCache("MinioCDN", name, value);
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
     * Get XMessageDao Object from cache by name
     * @param name
     * @return
     */
    public Object getXMessageDaoCache(String name) {
        return getCache("XMessageDAO", name);
    }
    
    /**
     * Set XMessageDao Object in cache by name
     * @param name
     * @param value
     */
    public void setXMessageDaoCache(String name, Object value) {
        setCache("XMessageDAO", name, value);
    }
    
    /**
     * Delete XMessageDao Object in cache by name
     * @param name
     */
    public void deleteXMessageDaoCache(String name) {
        deleteCache("XMessageDAO", name);
    }
    

    /**
     * Get cache by key & name
     * @param key
     * @param name
     * @return
     */
    private Object getCache(String key, String name) {
    	log.info("Find redis cache by key: "+redisKeyWithPrefix(key)+", name: "+redisKeyWithPrefix(name));
    	HashOperations hashOperations = redisTemplate.opsForHash();
        return hashOperations.get(redisKeyWithPrefix(key), redisKeyWithPrefix(name));
    }
    
    /**
     * Set cache value by key & name
     * @param key
     * @param name
     * @param value
     */
    private void setCache(String key, String name, Object value) {
    	log.info("Set redis cache for key: "+redisKeyWithPrefix(key)+", name: "+redisKeyWithPrefix(name));
    	HashOperations hashOperations = redisTemplate.opsForHash();
    	try {
            hashOperations.put(redisKeyWithPrefix(key), redisKeyWithPrefix(name), value);
        } catch (Exception e) {
        	/* If redis cache not able to set, delete cache */
        	hashOperations.delete(redisKeyWithPrefix(key), redisKeyWithPrefix(name));
        	
        	log.info("Exception in redis setCache: "+e.getMessage());
        }
    }
    
    /**
     * Set cache value by key & name
     * @param key
     * @param name
     * @param value
     */
    private void deleteCache(String key, String name) {
        log.info("Delete redis cache for key: "+redisKeyWithPrefix(key)+", name: "+redisKeyWithPrefix(name));
        HashOperations hashOperations = redisTemplate.opsForHash();
        try {
            hashOperations.delete(redisKeyWithPrefix(key), redisKeyWithPrefix(name));
        } catch (Exception e) {
            log.info("Exception in redis deleteCache: "+e.getMessage());
        }
    }
    
    /**
     * Add env prefix with cache key & name
     * @param key
     * @return
     */
    private String redisKeyWithPrefix(String key) {
    	return System.getenv("ENV")+"-"+key;
    }
}
