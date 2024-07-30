package com.cloth.business.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/cache/")
public class CacheController {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


	@DeleteMapping("/evict/{key}")
    public String evictCache(@PathVariable String key) {
//        this.evictCacheService(key);
        return "Cache evicted for key: " + key;
    }

    @DeleteMapping("/evictAll")
    public String evictAllCache() {
    	redisTemplate.getConnectionFactory().getConnection().flushDb();
        return "All cache evicted";
    }
    
    

}
