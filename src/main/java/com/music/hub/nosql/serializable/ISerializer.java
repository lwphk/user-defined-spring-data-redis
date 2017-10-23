package com.music.hub.nosql.serializable;

import org.apache.commons.lang3.SerializationException;

public interface ISerializer<T> {
	
	byte[] serialize(T obj) throws SerializationException;
	
	public T deserialize(byte[] bytes) throws SerializationException;
}
