package com.music.hub.nosql.redis.pubsub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import redis.clients.jedis.JedisPubSub;
/**
 * redis 发布订阅,将子类放入spirng 容器实现onRedisMessage  
 * @author admin
 *
 */
public abstract class AbstractRedisPubSubListener extends JedisPubSub{
	
	private static final Logger log = LoggerFactory.getLogger(AbstractRedisPubSubListener.class);

	private ThreadPoolTaskExecutor executor;
	
	/**
	 * 订阅的渠道名称
	 * 例:
	 * notify-keyspace-events"Ex" 启用 ”keyspace events notification” 监听key过期事件 渠道名称__keyevent@0__:expired
	 * 
	 * @return
	 */
	public abstract String getChannel();
	
	public abstract void onRedisMessage(String channel, String message);

	@Override
	public void onMessage(String channel, String message) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				onRedisMessage(channel, message);
			}
		});
	}
	
	@Override  
    public void onSubscribe(String channel, int subscribedChannels) {  
        log.debug("redis onSubscribe channel "+ channel);
    }
	
	@Override  
    public void unsubscribe() {  
        super.unsubscribe();  
    }  
  
    @Override  
    public void unsubscribe(String... channels) {  
        super.unsubscribe(channels);  
    }  
  
    @Override  
    public void subscribe(String... channels) {  
        super.subscribe(channels);  
    }

	public ThreadPoolTaskExecutor getExecutor() {
		return executor;
	}

	public void setExecutor(ThreadPoolTaskExecutor executor) {
		this.executor = executor;
	}

}
