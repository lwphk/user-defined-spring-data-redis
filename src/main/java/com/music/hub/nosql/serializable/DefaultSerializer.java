package com.music.hub.nosql.serializable;

import org.apache.commons.lang3.SerializationException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;

public class DefaultSerializer implements ISerializer<Object>{

	private final Converter<Object, byte[]> serializer;
	
	private final Converter<byte[], Object> deserializer;
	
	public DefaultSerializer() {
		this(new SerializingConverter(), new DeserializingConverter());
	}
	
	public DefaultSerializer(Converter<Object, byte[]> serializer,Converter<byte[], Object> deserializer) {
		this.serializer = serializer;
		this.deserializer = deserializer;
	}
	
	@Override
	public byte[] serialize(Object obj) {
		if(obj == null) {
			return SerializationUtils.EMPTY_ARRAY;
		}else {
			try {
				return this.serializer.convert(obj);
			} catch (Exception arg2) {
				throw new SerializationException("Cannot deserialize", arg2);
			}
		}
	}

	@Override
	public Object deserialize(byte[] bytes){
		if(SerializationUtils.isEmpty(bytes)) {
			return null;
		} else {
			try {
				return this.deserializer.convert(bytes);
			} catch (Exception arg2) {
				throw new SerializationException("Cannot deserialize", arg2);
			}
		}
	}
}
