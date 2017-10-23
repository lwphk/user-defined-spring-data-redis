package com.music.hub.nosql.redis.core;

import redis.clients.jedis.Jedis;

public interface RedisCallback<T> {

	public T redisAction(Jedis jedis);
}
