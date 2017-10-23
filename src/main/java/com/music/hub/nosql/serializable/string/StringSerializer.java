package com.music.hub.nosql.serializable.string;

import java.nio.charset.Charset;

import org.apache.commons.lang3.SerializationException;

import com.music.hub.nosql.serializable.ISerializer;

public class StringSerializer implements ISerializer<String> {

	private final Charset charset;
	
	public StringSerializer() {
		this(Charset.forName("UTF8"));
	}
	
	public StringSerializer(Charset charset) {
		this.charset = charset;
	}

	
	@Override
	public byte[] serialize(String string) throws SerializationException {
		return string == null ? null : string.getBytes(this.charset);
	}

	@Override
	public String deserialize(byte[] bytes) throws SerializationException {
		return bytes == null ? null : new String(bytes, this.charset);
	}

}
