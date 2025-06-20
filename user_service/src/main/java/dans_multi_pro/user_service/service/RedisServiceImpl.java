package dans_multi_pro.user_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.Optional;

@Service
public class RedisServiceImpl implements RedisService{

    private final JedisPool jedisPool;
    private final ObjectMapper objectMapper;

    public RedisServiceImpl(
            JedisPool jedisPool,
            ObjectMapper objectMapper
    ) {
        this.jedisPool = jedisPool;
        this.objectMapper = objectMapper;
    }

    @Override
    public void put(String key, String value, int ttlSeconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.setex(key, ttlSeconds, value);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void hmset(String key, Map<String, String> value, int ttlSeconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hmset(key, value);
            jedis.expire(key,ttlSeconds );
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String hget(String key, String field) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hget(key, field);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(key);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean exists(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(key);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> Optional<T> getObject(String key, Class<T> clazz) {
        try (Jedis jedis = jedisPool.getResource()) {
            String value = jedis.get(key);
            if (value == null) return Optional.empty();
            return Optional.of(objectMapper.readValue(value, clazz));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}