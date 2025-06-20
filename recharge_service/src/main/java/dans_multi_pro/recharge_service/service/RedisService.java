package dans_multi_pro.recharge_service.service;

import java.util.Optional;

public interface RedisService {
    void put(String key, String value, int ttlSeconds);
    String get(String key);
    void delete(String key);
    boolean exists(String key);
    <T> Optional<T> getObject(String key, Class<T> clazz);
}