package com.music.hub.nosql.redis.core;

import redis.clients.jedis.Jedis;

public interface RedisCallbackAsync {

	void redisAction(Jedis jedis);
}
