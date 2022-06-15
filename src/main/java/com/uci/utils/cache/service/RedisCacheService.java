package com.uci.utils.cache.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.uci.utils.cache.RedisCachePrefix;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.TimeUnit;

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
    public Object getMinioCDNCache(String key) {
        return getCache(redisKeyWithPrefix(RedisCachePrefix.MinioCDN.name(), key));
    }
    
    /**
     * Set XMessageDao Object in cache by name
     * @param name
     * @param value
     */
    public void setMinioCDNCache(String key, Object value) {
        setCache(redisKeyWithPrefix(RedisCachePrefix.MinioCDN.name(), key), value);
    }
    
    /**
     * Get Fusion Auth User ID Object from cache by name
     * @param name
     * @return
     */
    public Object getFAUserIDForAppCache(String key) {
        return getCache(redisKeyWithPrefix(RedisCachePrefix.FAUserID.name(), key));
    }
    
    /**
     * Set Fusion Auth User ID Object in cache by name
     * @param name
     * @param value
     */
    public void setFAUserIDForAppCache(String key, Object value) {
        setCache(redisKeyWithPrefix(RedisCachePrefix.FAUserID.name(), key), value);
    }
    
    /**
     * Delete Fusion Auth User ID Object in cache by name
     * @param name
     */
    public void deleteFAUserIDForAppCache(String key) {
        deleteCache(redisKeyWithPrefix(RedisCachePrefix.FAUserID.name(), key));
    }

    /**
     * Get Language Object from cache by name
     * @param name
     * @return
     */
    public Object getLanguageCache(String name) {
        return getCache(redisKeyWithPrefix(RedisCachePrefix.Language.name(), name));
    }
    
    /**
     * Set Language Object in cache by name
     * @param name
     * @param value
     */
    public void setLanguageCache(String name, Object value) {
        setCache(redisKeyWithPrefix(RedisCachePrefix.Language.name(), name), value);
    }
    
    /**
     * Delete Language Object in cache by name
     * @param name
     */
    public void deleteLanguageCache(String name) {
        deleteCache(redisKeyWithPrefix(RedisCachePrefix.Language.name(), name));
    }
    
    /**
     * Get XMessageDao Object from cache by name
     * @param name
     * @return
     */
    public Object getXMessageDaoCache(String key) {
        return getCache(redisKeyWithPrefix(RedisCachePrefix.XMessageDao.name(), key));
    }
    
    /**
     * Set XMessageDao Object in cache by name
     * @param name
     * @param value
     */
    public void setXMessageDaoCache(String key, Object value) {
        setCache(redisKeyWithPrefix(RedisCachePrefix.XMessageDao.name(), key), value);
    }
    
    /**
     * Delete XMessageDao Object in cache by name
     * @param name
     */
    public void deleteXMessageDaoCache(String key) {
        deleteCache(redisKeyWithPrefix(RedisCachePrefix.XMessageDao.name(), key));
    }

    /**
     * Get all cache keys by prefix
     * @param prefix
     * @return
     */
    public ArrayList<String> getAllCacheKeys(String prefix) {
        if(enabledRedis()) {
            ArrayList<String> keysList = new ArrayList();
            Set<String> redisKeys = redisTemplate.keys(prefix);
            Iterator<String> redisIt = redisKeys.iterator();
            while(redisIt.hasNext()) {
                keysList.add(redisIt.next());
            }
            return keysList;
        }
        return null;
    }

    /**
<<<<<<< HEAD
     * Get all cache keys by prefix
     * @param prefix
     * @return
     */
    public ArrayList<Pair<String, Object>> getAllCacheKeyValuesByPattern(String prefix) {
        if(enabledRedis()) {
            ArrayList<Pair<String, Object>> list = new ArrayList();
            String pattern = redisKeyPatternWithPrefix(prefix);
            RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
            Set<byte[]> redisKeys = redisConnection.keys(pattern.getBytes());
            Iterator<byte[]> redisIt = redisKeys.iterator();
            ValueOperations<String, Object> valOperations = redisTemplate.opsForValue();
            while(redisIt.hasNext()) {
                byte[] data = redisIt.next();
                String key = new String(data, 0, data.length);
                Object result = valOperations.get(key);
                list.add(Pair.of(key, result));
            }
            return list;
        }
        return null;
    }

    /**
     * Get cache by key
     * @param redisKey
     * @return
     */
    public Object getCache(String redisKey) {
    	if(enabledRedis()) {
            ValueOperations<String, Object> valOperations = redisTemplate.opsForValue();
            Object result = valOperations.get(redisKey);

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String val = "";
            try {
                val = ow.writeValueAsString(result);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            log.info("Find redis cache by key: "+redisKey+", val: "+val);
            return result;
        }
        return null;
    }
    
    /**
     * Set cache value by redisKey
     * @param redisKey
     * @param value
     */
    public void setCache(String redisKey, Object value) {
        if (enabledRedis()) {
            ValueOperations<String, Object> valOperations = redisTemplate.opsForValue();
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            try {
                valOperations.set(redisKey, value);
                /* Set Expire time for key */
                redisTemplate.expire(redisKey, getExpireTimeInSeconds(), TimeUnit.SECONDS);

                String redisVal = "";
                try {
                    redisVal = ow.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                log.info("Set redis cache for key: " + redisKey+", val: "+redisVal);
            } catch (Exception e) {
                /* If redis cache not able to set, delete cache */
                redisTemplate.delete(redisKey);
                e.printStackTrace();

                log.info("Exception in redis setCache: " + e.getMessage());
            }
=======
     * Get cache by key & name
     * @param key
     * @param name
     * @return
     */
    private Object getCache(String prefix, String key) {
    	ValueOperations<String, Object> valOperations = redisTemplate.opsForValue();
        Object result = valOperations.get(redisKeyWithPrefix(prefix, key));
        log.info("Find redis cache by key: "+redisKeyWithPrefix(prefix, key));
    	return result;
    }
    
    /**
     * Set cache value by key & name
     * @param key
     * @param name
     * @param value
     */
    private void setCache(String prefix, String key, Object value) {
    	log.info("Set redis cache for key: "+redisKeyWithPrefix(prefix, key));
    	ValueOperations<String, Object> valOperations = redisTemplate.opsForValue();
        try {
        	valOperations.set(redisKeyWithPrefix(prefix, key), value);
        } catch (Exception e) {
        	/* If redis cache not able to set, delete cache */
        	redisTemplate.delete(redisKeyWithPrefix(prefix, key));
        	e.printStackTrace();
        	
        	log.info("Exception in redis setCache: "+e.getMessage());
>>>>>>> parent of a344bab... Merge branch 'release-4.9.0' of https://github.com/samagra-comms/utils into merged
        }
    }
    
    /**
<<<<<<< HEAD
     * Set cache value by redisKey
     * @param redisKey
     */
    public void deleteCache(String redisKey) {
        if (enabledRedis()) {
            log.info("Delete redis cache for key: " + redisKey);
            ValueOperations<String, Object> valOperations = redisTemplate.opsForValue();
            try {
                /* Set Expire time for key */
                redisTemplate.expire(redisKey, 1, TimeUnit.SECONDS);
//                redisTemplate.delete(redisKey);
            } catch (Exception e) {
                log.info("Exception in redis deleteCache: " + e.getMessage());
            }
=======
     * Set cache value by key & name
     * @param key
     * @param name
     * @param value
     */
    public void deleteCache(String prefix, String key) {
        log.info("Delete redis cache for key: "+redisKeyWithPrefix(prefix, key));
        ValueOperations<String, Object> valOperations = redisTemplate.opsForValue();
        try {
        	redisTemplate.delete(redisKeyWithPrefix(prefix, key));
        } catch (Exception e) {
            log.info("Exception in redis deleteCache: "+e.getMessage());
>>>>>>> parent of a344bab... Merge branch 'release-4.9.0' of https://github.com/samagra-comms/utils into merged
        }
    }
    
    /**
     * Add env prefix with cache key & name
     * @param key
     * @return
     */
    private String redisKeyWithPrefix(String prefix, String key) {
    	return System.getenv("ENV")+"-"+prefix+":"+key;
    }

    /**
     * Add env prefix
     * @param prefix
     * @return
     */
    private String redisKeyPatternWithPrefix(String prefix) {
        return System.getenv("ENV")+"-"+prefix+":*";
    }

    /**
     * Get Cache Expiry time in seconds
     * @return
     */
    private Integer getExpireTimeInSeconds() {
        return 3600; //60*60 = 1 Hour
    }
}
