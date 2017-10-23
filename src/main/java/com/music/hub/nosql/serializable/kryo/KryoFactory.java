package com.music.hub.nosql.serializable.kryo;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.esotericsoftware.kryo.Kryo;

public final class KryoFactory {
	
	private final GenericObjectPool<Kryo> kryoPool;
	
	public KryoFactory() {
		kryoPool = new GenericObjectPool<>(new PooledKryoFactory());
	}
	
	public KryoFactory(final int maxTotal, final int minIdle, final long maxWaitMillis, final long minEvictableIdleTimeMillis) {
		kryoPool = new GenericObjectPool<>(new PooledKryoFactory());
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxTotal(maxTotal);
		config.setMinIdle(minIdle);
		config.setMaxWaitMillis(maxWaitMillis);
		config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		kryoPool.setConfig(config);
	}
	
	public Kryo getKryo() {
		try {
			return kryoPool.borrowObject();
		} catch (final Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public void returnKryo(final Kryo kryo) {
		kryoPool.returnObject(kryo);
	}
}
