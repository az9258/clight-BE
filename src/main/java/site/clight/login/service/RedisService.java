package site.clight.login.service;

import java.util.concurrent.TimeUnit;

/**
 * Redis 接口操作
 */
public interface RedisService {
    /**
     * 设置键值对
     * @param key 键
     * @param value 值 - 任意类型
     */
    <T> void set(String key, T value);

    /**
     * 获取键对应的值
     * @param key 键
     * @param type 值类型
     * @return 值
     */
    <T> T get(String key, Class<T> type);

    /**
     * 设置键值对并设置过期时间
     * @param key 键
     * @param value 值 - 任意类型
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    <T> void setWithExpire(String key, T value, long timeout, TimeUnit unit);

    /**
     * 删除键
     * @param key 键
     * @return 是否删除成功
     */
    boolean delete(String key);

    /**
     * 检查键是否存在
     * @param key 键
     * @return 是否存在
     */
    boolean hasKey(String key);
}
