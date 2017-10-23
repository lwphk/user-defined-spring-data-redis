package com.music.hub.nosql.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.music.hub.nosql.serializable.DefaultSerializer;
import com.music.hub.nosql.serializable.kryo.KryoFactory;
import com.music.hub.nosql.serializable.kryo.KryoSerializer;


@Configuration
public class KryoConfiguration {

	@Bean
	public KryoFactory kryoFactory() {
		return new KryoFactory(10,2,10000,20000);
	}
	
	@Bean
	public DefaultSerializer defaultSerializer() {
		return new DefaultSerializer();
	}
	
	@Bean
	public <T> KryoSerializer<T> kryoSerializer(KryoFactory factory) {
		KryoSerializer<T> kryoSerializer = new KryoSerializer<>();
		kryoSerializer.setKryoFactory(factory);
		return kryoSerializer;
	}
}
