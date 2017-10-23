# user-defined-spring-data-redis
原生jedis结合spring，支持同步，异步操作。 异步操作由ThreadPoolTaskExecutor线程池执行。

序列化使用kryo，kryo是非线程安全的并且对象创建成本高，这里使用Apache Common-pool2对象池进行管理

# properties配置（默认单机版，哨兵模式需修改）
	redis.host=r-wz95338595754734.redis.rds.aliyuncs.com
	redis.port=6379
	redis.password=xxx
	redis.maxTotal=100
	redis.maxIdle=100
	redis.maxWaitMillis=20000
	redis.timeout=5000

# 继承 AbstractRedisBaseComponent 
	public class AbstractRedisBaseComponent <K extends Serializable, V> {

		@Resource(name="redisTemplate")  
		protected RedisTemplate<K, V> redisTemplate;
		
		//.....
		
	}
	
	@Component
	public class StringRedisComponent extends  AbstractRedisBaseComponent<String, String>{

		public String get(final String key) {
			return redisTemplate.execute(new RedisCallback<String>() {
				@Override
				public String redisAction(Jedis jedis) {
					return new String(jedis.get(key));
				}

			});
		}
	}

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
			
			public User redisAction(redis.clients.jedis.Jedis jedis) {
			
				ISerializer<User> serializer = redisTemplate.getValueSerializer();
				
				byte[] bytes = jedis.get(key.getBytes());
				
				if(bytes != null && bytes.length > 0) {
				
					return serializer.deserialize(bytes);
					
				}
				
				return null;
				
			}
			
		});
		
	User user = future.get();
	
