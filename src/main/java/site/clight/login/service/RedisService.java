package site.clight.login.service;

import java.util.concurrent.TimeUnit;

/**
 * Redis 接口操作，定义了一系列对 Redis 进行基本操作的方法。
 * 该接口提供了设置键值对、获取键值对、设置带过期时间的键值对、删除键以及检查键是否存在等功能。
 */
public interface RedisService {
    /**
     * 设置键值对到 Redis 中。
     * 使用泛型支持任意类型的值，在存储时会将值进行序列化处理。
     * 
     * @param key 键，用于唯一标识存储的值。
     * @param value 值，可以是任意类型的对象。
     * @param <T> 值的类型。
     */
    <T> void set(String key, T value);

    /**
     * 从 Redis 中获取指定键对应的值。
     * 根据传入的类型参数将存储的序列化数据反序列化为相应的对象。
     * 
     * @param key 键，用于查找存储的值。
     * @param type 值的类型，用于反序列化操作。
     * @param <T> 值的类型。
     * @return 键对应的值，如果键不存在则返回 null。
     */
    <T> T get(String key, Class<T> type);

    /**
     * 设置键值对到 Redis 中，并为该键设置过期时间。
     * 使用泛型支持任意类型的值，在存储时会将值进行序列化处理。
     * 
     * @param key 键，用于唯一标识存储的值。
     * @param value 值，可以是任意类型的对象。
     * @param timeout 过期时间，指定键在多长时间后过期。
     * @param unit 时间单位，用于指定过期时间的单位，如秒、分钟等。
     * @param <T> 值的类型。
     */
    <T> void setWithExpire(String key, T value, long timeout, TimeUnit unit);

    /**
     * 从 Redis 中删除指定的键及其对应的值。
     * 
     * @param key 要删除的键。
     * @return 如果删除成功返回 true，否则返回 false。
     */
    boolean delete(String key);

    /**
     * 检查指定的键是否存在于 Redis 中。
     * 
     * @param key 要检查的键。
     * @return 如果键存在返回 true，否则返回 false。
     */
    boolean hasKey(String key);
}
