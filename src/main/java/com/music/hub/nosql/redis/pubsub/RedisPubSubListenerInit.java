package com.music.hub.nosql.redis.pubsub;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * redis 发布订阅
 * @author admin
 *
 */
public class RedisPubSubListenerInit implements ApplicationListener<ContextRefreshedEvent>, InitializingBean {

	private static final Logger log = LoggerFactory.getLogger(AbstractRedisPubSubListener.class);

	private JedisPool jedisPool;

	private ThreadPoolTaskExecutor executor;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
			//获取AbstractRedisPubSubListener的所有子类
			Map<String, AbstractRedisPubSubListener> map = event.getApplicationContext()
					.getBeansOfType(AbstractRedisPubSubListener.class);
			Set<String> keys = map.keySet();
			if (keys.size() != 0) {
				for (String key : keys) {
					AbstractRedisPubSubListener jedisPubSub = map.get(key);
					String channel = jedisPubSub.getChannel();
					jedisPubSub.setExecutor(executor);
					log.debug(jedisPubSub.getClass() + " subscriber redis " + channel);
					new Thread(new Runnable() {
						@Override
						public void run() {
							Jedis jedis = null;
							try {
								jedis = jedisPool.getResource();
								// 这里会阻塞,需要单独线程启动,只有调用unsubscribe才会释放
								jedis.subscribe(jedisPubSub, channel);
							} catch (Exception ex) {
								log.error(ex.getLocalizedMessage());
								ex.printStackTrace();
							} finally {
								// unsubscribe 调用后才能释放这个jedis链接
								if (jedis != null)
									jedis.close();
							}
						}
					}).start();
				}

			}
		}
	}

	public ThreadPoolTaskExecutor getExecutor() {
		return executor;
	}

	public void setExecutor(ThreadPoolTaskExecutor executor) {
		this.executor = executor;
	}

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(executor == null) {
			executor = new ThreadPoolTaskExecutor();
		}

	}

}
