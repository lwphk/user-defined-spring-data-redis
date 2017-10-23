package com.music.hub.nosql.serializable.kryo;

import org.apache.commons.lang3.SerializationException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.music.hub.nosql.serializable.ISerializer;

public class KryoSerializer<T>  implements ISerializer<T>{
	
	private  KryoFactory kryoFactory;
	
	public byte[] serialize(T t) throws SerializationException {
		Kryo kryo = kryoFactory.getKryo();
		Output output = new Output(1024,-1);
		try {
			kryo.writeClassAndObject(output, t);
			kryoFactory.returnKryo(kryo);
			return output.toBytes();
		} catch (Exception e) {
			
		}finally{
			output.close();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public T deserialize(byte[] bytes) throws SerializationException {
		Kryo kryo = kryoFactory.getKryo();
		Input input = new Input(bytes);
		try {
			Object result = kryo.readClassAndObject(input);
			kryoFactory.returnKryo(kryo);
			return (T) result;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			input.close();
		}
		return null;
	}

	public KryoFactory getKryoFactory() {
		return kryoFactory;
	}

	public void setKryoFactory(KryoFactory kryoFactory) {
		this.kryoFactory = kryoFactory;
	}
	

}
