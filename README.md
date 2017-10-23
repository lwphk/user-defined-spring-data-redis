# user-defined-spring-data-redis
原生jedis结合spring，支持同步异步操作
# RedisCallback 同步有返回值

  final String key = "";
  
	String result = redisTemplate.execute(new RedisCallback<String>() {
	
			@Override
			
			public String redisAction(redis.clients.jedis.Jedis jedis) {
			
				return jedis.get(key);
				
			}
			
	});
	
# RedisCallbackAsync 异步无返回值
  
  redisTemplate.execute(new RedisCallbackAsync() {
  
			@Override
			
			public void redisAction(redis.clients.jedis.Jedis jedis) {
			
              //
	      
			}
			
	});
	
  
# RedisCallbackFuture 


  java.util.concurrent.Future<User> future = redisTemplate.execute(new RedisCallbackFuture<User>() {
	
			@Override
			
			public User redisAction(Jedis jedis) {
			
				ISerializer<User> serializer = redisTemplate.getValueSerializer();
				
				byte[] bytes = jedis.get(key.getBytes());
				
				if(bytes != null && bytes.length > 0) {
				
					return serializer.deserialize(bytes);
					
				}
				
				return null;
				
			}
			
		});
		
	User user = future.get();
	
