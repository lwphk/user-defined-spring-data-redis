package com.music.hub.nosql.redis.core;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.music.hub.nosql.serializable.ISerializer;
import com.music.hub.nosql.serializable.string.StringSerializer;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisTemplate<K,V> implements InitializingBean{

	private JedisPool jedisPool;
	
	private ThreadPoolTaskExecutor executor;
	
	private ISerializer<String> keySerializer = new StringSerializer();
	
	private ISerializer<V> valueSerializer;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(executor == null) {
			executor = new ThreadPoolTaskExecutor();
		}
	}
	
	public <T> T execute(RedisCallback<T> redisCallback){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			T t = redisCallback.redisAction(jedis);
			return postProcessResult(t, jedis);
		}finally{
			if(jedis != null) 
				jedis.close();
		}
	}
	
	public void execute(final RedisCallbackAsync redisCallbackAsync){
		executor.execute(new Runnable() {
			@Override
			public void run() {
				Jedis jedis = null;
				try {
					jedis = jedisPool.getResource();
					redisCallbackAsync.redisAction(jedis);
				}finally{
					if(jedis != null) 
						jedis.close();
				}
			}
		});
	}
	
	public <T> Future<T> execute(final RedisCallbackFuture<T>  redisCallbackFuture){
		return executor.submit(new Callable<T>() {
			@Override
			public T call() throws Exception {
				Jedis jedis = null;
				try {
					jedis = jedisPool.getResource();
					return postProcessResult(redisCallbackFuture.redisAction(jedis),jedis);
				}finally{
					if(jedis != null) 
						jedis.close();
				}
			}
		});
	}
	
	private <T> T postProcessResult(T t,Jedis jedis){
		return t;
	}

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	public ISerializer<String> getKeySerializer() {
		return keySerializer;
	}

	public void setKeySerializer(ISerializer<String> keySerializer) {
		this.keySerializer = keySerializer;
	}

	public ISerializer<V> getValueSerializer() {
		return valueSerializer;
	}

	public void setValueSerializer(ISerializer<V> valueSerializer) {
		this.valueSerializer = valueSerializer;
	}

	public ThreadPoolTaskExecutor getExecutor() {
		return executor;
	}

	public void setExecutor(ThreadPoolTaskExecutor executor) {
		this.executor = executor;
	}

	
	
}
