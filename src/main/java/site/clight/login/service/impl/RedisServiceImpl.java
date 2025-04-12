package site.clight.login.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import site.clight.login.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Redis服务实现类，提供Redis相关操作
 */
@Service
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * 构造方法，自动注入RedisTemplate和ObjectMapper
     * @param redisTemplate Redis操作模板
     * @param objectMapper JSON序列化工具
     */
    @Autowired
    public RedisServiceImpl(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * 设置键值对到Redis
     * @param key 键
     * @param value 值
     * @param <T> 值的类型
     */
    @Override
    public <T> void set(String key, T value) {
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonValue);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize value", e);
        }
    }

    /**
     * 从Redis获取指定类型的值
     * @param key 键
     * @param type 值类型
     * @param <T> 返回类型
     * @return 获取到的值，如果不存在返回null
     */
    @Override
    public <T> T get(String key, Class<T> type) {
        String jsonValue = redisTemplate.opsForValue().get(key);
        if (jsonValue == null) {
            return null;
        }
        try {
            return objectMapper.readValue(jsonValue, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize value", e);
        }
    }

    /**
     * 设置带过期时间的键值对
     * @param key 键
     * @param value 值
     * @param timeout 过期时间数值
     * @param unit 过期时间单位
     * @param <T> 值的类型
     */
    @Override
    public <T> void setWithExpire(String key, T value, long timeout, TimeUnit unit) {
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonValue, timeout, unit);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize value", e);
        }
    }

    /**
     * 删除指定键
     * @param key 要删除的键
     * @return 是否删除成功
     */
    @Override
    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    /**
     * 检查键是否存在
     * @param key 要检查的键
     * @return 键是否存在
     */
    @Override
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
