package com.music.hub.nosql.configuration.copy;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.music.hub.nosql.configuration.config.RedisConfig;
import com.music.hub.nosql.redis.core.RedisTemplate;
import com.music.hub.nosql.redis.pubsub.RedisPubSubListenerInit;
import com.music.hub.nosql.serializable.kryo.KryoSerializer;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfiguration {

	@Bean
	@ConfigurationProperties("redis")
	public RedisConfig redisConfig() {
		return new RedisConfig();
	}

	@Bean
	public JedisPoolConfig jedisPoolConfig(RedisConfig config) {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(config.getMaxIdle());
		poolConfig.setMaxIdle(config.getMaxIdle());
		poolConfig.setMaxWaitMillis(config.getMaxWaitMillis());
		return poolConfig;
	}

	@Bean
	public JedisPool jedisPool(JedisPoolConfig jedisPoolConfig, RedisConfig config) {
		if (StringUtils.isBlank(config.getPassword()))
			return new JedisPool(jedisPoolConfig, config.getHost(), config.getPort(), config.getTimeout());
		else
			return new JedisPool(jedisPoolConfig, config.getHost(), config.getPort(), config.getTimeout(),
					config.getPassword());
	}

	/**
	 * redis异步执行（和发布订阅）的线程池
	 * @return
	 */
	@Bean
	public ThreadPoolTaskExecutor redisThreadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// 核心线程数
		executor.setCorePoolSize(20);
		// 最大线程数
		executor.setMaxPoolSize(100);
		// 队列最大长度
		executor.setQueueCapacity(200);
		// 线程池维护线程所允许的空闲时间
		executor.setKeepAliveSeconds(300);
		// 线程池对拒绝任务(无线程可用)的处理策略 ThreadPoolExecutor.CallerRunsPolicy策略
		// ,调用者的线程会执行该任务,如果执行器已关闭,则丢弃.
		executor.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy());
		return executor;
	}

	@Bean
	public <T> RedisTemplate<T, T> redisTemplate(JedisPool jedisPool, ThreadPoolTaskExecutor executor,
			KryoSerializer<T> kryoSerializer) {
		RedisTemplate<T, T> template = new RedisTemplate<>();
		template.setJedisPool(jedisPool);
		template.setExecutor(executor);
		template.setValueSerializer(kryoSerializer);
		return template;
	}

	
	@Bean
	public RedisPubSubListenerInit redisPubSubListenerInit(JedisPool jedisPool,ThreadPoolTaskExecutor executor) {
		RedisPubSubListenerInit init = new RedisPubSubListenerInit();
		init.setExecutor(executor);
		init.setJedisPool(jedisPool);
		return init;
	}

}
