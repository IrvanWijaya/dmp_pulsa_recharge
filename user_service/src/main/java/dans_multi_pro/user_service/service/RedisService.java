package dans_multi_pro.user_service.service;

import java.util.Map;
import java.util.Optional;

public interface RedisService {
    void put(String key, String value, int ttlSeconds);
    void hmset(String key, Map<String, String> value, int ttlSeconds);
    String get(String key);
    void delete(String key);
    boolean exists(String key);
    <T> Optional<T> getObject(String key, Class<T> clazz);

    String hget(String redisKey, String field);
}