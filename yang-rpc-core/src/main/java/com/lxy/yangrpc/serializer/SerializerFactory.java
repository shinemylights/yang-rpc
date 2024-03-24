package com.lxy.yangrpc.serializer;


import com.lxy.yangrpc.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * 序列化器工厂（工厂模式，用于获取序列化器对象）
 */
public class SerializerFactory {

    //region 维护了4种序列化器
    // /**
    //  *序列化映射(用于实现单例)
    //  */
    // private static final Map<String,Serializer> KEY_SERIALIZER_MAP = new
    // HashMap<String,Serializer>() {{
    //     put (SerializerKeys. JDK,new JdkSerializer());
    //     put (SerializerKeys. JSON,new JsonSerializer()) ;
    //     put (SerializerKeys. KRYO,new KryoSerializer());
    //     put (SerializerKeys. HESSIAN, new HessianSerializer());
    // }};
    //
    // /**
    //  *默认序列化器
    //  */
    // private static final Serializer DEFAULT_SERIALIZER = KEY_SERIALIZER_MAP.get("jdk");
    //
    // /**
    //  *获取实例
    //  *
    //  * @param key
    //  * @return
    //  */
    // public static Serializer getInstance (String key) {
    //     return KEY_SERIALIZER_MAP.getOrDefault(key, DEFAULT_SERIALIZER);
    // }

    //endregion

    //spi扩展
    //使用静态代码块，在工厂首次加载时，就会调用Spiloader的load方法加载序列化器接口的所有
    //实现类，之后就可以通过调用getnstance方法获取指定的实现类对象了。
    static {
        SpiLoader.load(Serializer.class);
    }

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    /**
     * 获取实例
     *
     * @param key
     * @return
     */
    public static Serializer getInstance(String key) {
        return SpiLoader.getInstance(Serializer.class, key);
    }

}
