package com.music.hub.nosql.serializable.kryo;

import java.lang.reflect.InvocationHandler;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.esotericsoftware.kryo.serializers.FieldSerializer;

import de.javakaffee.kryoserializers.ArraysAsListSerializer;
import de.javakaffee.kryoserializers.BitSetSerializer;
import de.javakaffee.kryoserializers.CollectionsEmptyListSerializer;
import de.javakaffee.kryoserializers.CollectionsEmptyMapSerializer;
import de.javakaffee.kryoserializers.CollectionsEmptySetSerializer;
import de.javakaffee.kryoserializers.CollectionsSingletonListSerializer;
import de.javakaffee.kryoserializers.CollectionsSingletonMapSerializer;
import de.javakaffee.kryoserializers.CollectionsSingletonSetSerializer;
import de.javakaffee.kryoserializers.GregorianCalendarSerializer;
import de.javakaffee.kryoserializers.JdkProxySerializer;
import de.javakaffee.kryoserializers.RegexSerializer;
import de.javakaffee.kryoserializers.SynchronizedCollectionsSerializer;
import de.javakaffee.kryoserializers.URISerializer;
import de.javakaffee.kryoserializers.UUIDSerializer;
import de.javakaffee.kryoserializers.UnmodifiableCollectionsSerializer;

final class PooledKryoFactory extends BasePooledObjectFactory<Kryo> {
	
	@Override
	public Kryo create() throws Exception {
		return createKryo();
	}
	
	@Override
	public PooledObject<Kryo> wrap(Kryo kryo) {
		return new DefaultPooledObject<Kryo>(kryo);
	}
	
	private Kryo createKryo() {
		Kryo kryo = new Kryo();
//			new KryoReflectionFactorySupport() {
//
//			@Override
//			@SuppressWarnings({ "rawtypes", "unchecked" })
//			public Serializer<?> getDefaultSerializer(final Class type) {
//				if (EnumSet.class.isAssignableFrom(type)) {
//					return new EnumSetSerializer();
//				}
//				if (EnumMap.class.isAssignableFrom(type)) {
//					return new EnumMapSerializer();
//				}
//				if (Collection.class.isAssignableFrom(type)) {
//					return new CopyForIterateCollectionSerializer();
//				}
//				if (Map.class.isAssignableFrom(type)) {
//					return new CopyForIterateMapSerializer();
//				}
//				if (Date.class.isAssignableFrom(type)) {
//					return new DateSerializer(type);
//				}
//
//				// EnhancerByCGLIB
//				try {
//					if (type.getName().indexOf("$$EnhancerByCGLIB$$") > 0) {
//						Method method = Class
//								.forName("de.javakaffee.kryoserializers.cglib.CGLibProxySerializer")
//								.getDeclaredMethod("canSerialize", Class.class);
//						if ((Boolean) method.invoke(null, type)) {
//							return getSerializer(Class.forName("de.javakaffee.kryoserializers.cglib.CGLibProxySerializer$CGLibProxyMarker"));
//						}
//					}
//				} catch (Throwable thex) {
//				}
//
//				return super.getDefaultSerializer(type);
//			}
//		};
		
//		VersionFieldSerializer 扩展了 FieldSerializer，并允许字段具有 @Since(int) 注解来指示它们被添加的版本。
//		TaggedFieldSerializer 将 FieldSerializer 扩展为仅序列化具有 @Tag(int) 注解的字段，提供向后兼容性，从而可以添加新字段。
//		CompatibleFieldSerializer 扩展了 FieldSerializer 以提供向前和向后兼容性，这意味着可以添加或删除字段，而不会使先前的序列化字节无效  ,适合长期缓存数据序列化方式
//		在序列化中第一次遇到某个类时，会写入一个包含字段名称字符串的简单 scheme
		kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
		kryo.getFieldSerializerConfig().setCachedFieldNameStrategy(FieldSerializer.CachedFieldNameStrategy.EXTENDED);
		kryo.setReferences(true);
		kryo.setRegistrationRequired(false);
		kryo.setClassLoader(Thread.currentThread().getContextClassLoader());
		
		kryo.register(Arrays.asList("").getClass(), new ArraysAsListSerializer());
		kryo.register(Collections.EMPTY_LIST.getClass(), new CollectionsEmptyListSerializer());
		kryo.register(Collections.EMPTY_MAP.getClass(), new CollectionsEmptyMapSerializer());
		kryo.register(Collections.EMPTY_SET.getClass(), new CollectionsEmptySetSerializer());
		kryo.register(Collections.singletonList("").getClass(), new CollectionsSingletonListSerializer());
		kryo.register(Collections.singleton("").getClass(), new CollectionsSingletonSetSerializer());
		kryo.register(Collections.singletonMap("", "").getClass(), new CollectionsSingletonMapSerializer());
		kryo.register(BigDecimal.class, new DefaultSerializers.BigDecimalSerializer());
		kryo.register(BigInteger.class, new DefaultSerializers.BigIntegerSerializer());
		kryo.register(Pattern.class, new RegexSerializer());
		kryo.register(BitSet.class, new BitSetSerializer());
		kryo.register(URI.class, new URISerializer());
		kryo.register(UUID.class, new UUIDSerializer());
		kryo.register(GregorianCalendar.class, new GregorianCalendarSerializer());
		kryo.register(InvocationHandler.class, new JdkProxySerializer());

		UnmodifiableCollectionsSerializer.registerSerializers(kryo);
		SynchronizedCollectionsSerializer.registerSerializers(kryo);


		// CGLibProxySerializer
		try {
			Class<?> clazz = Class.forName("de.javakaffee.kryoserializers.cglib.CGLibProxySerializer$CGLibProxyMarker");
			Serializer<?> serializer = (Serializer<?>) Class
					.forName("de.javakaffee.kryoserializers.cglib.CGLibProxySerializer")
					.newInstance();
			kryo.register(clazz, serializer);
		} catch (Throwable thex) {
		}

		return kryo;
	}
}
