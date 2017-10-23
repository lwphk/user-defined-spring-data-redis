package com.music.hub.nosql.serializable;

public class SerializationUtils {

	static final byte[] EMPTY_ARRAY = new byte[0];

	static boolean isEmpty(byte[] data) {
		return data == null || data.length == 0;
	}
}
