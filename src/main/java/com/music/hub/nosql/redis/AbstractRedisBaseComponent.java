package com.music.hub.nosql.redis;

import java.io.Serializable;

import javax.annotation.Resource;

import com.music.hub.nosql.redis.core.RedisTemplate;
import com.music.hub.nosql.serializable.ISerializer;

public class AbstractRedisBaseComponent <K extends Serializable, V> {

	@Resource(name="redisTemplate")  
	protected RedisTemplate<K, V> redisTemplate;
  
    /** 
     * 设置redisTemplate 
     * @param redisTemplate the redisTemplate to set 
     */  
    public void setRedisTemplate(RedisTemplate<K , V> redisTemplate) {  
        this.redisTemplate = redisTemplate;  
    }

	public RedisTemplate<K, V> getRedisTemplate() {
		return redisTemplate;
	} 
	
	public ISerializer<V> getRedisValueSerializer() {
		return redisTemplate.getValueSerializer();
	}
	
	public ISerializer<String> getRedisKeySerializer() {
		return redisTemplate.getKeySerializer();
	}
    
}
