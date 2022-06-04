package xin.cosmos.basic.base;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.*;

/**
 * Redis服务类
 *
 * @author geng
 */
@Component
public class RedisService {
    private final RedisTemplate<Object, Object> redisTemplate;

    public RedisService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, Object value, long timeoutSeconds) {
        redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(timeoutSeconds));
    }

    public <T> T get(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    public Boolean setIfAbsent(String key, Object value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    public Boolean setIfAbsent(String key, Object value, long timeoutSeconds) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, Duration.ofSeconds(timeoutSeconds));
    }

    public Long deleteLike(String key) {
        Set<Object> keys = Optional.ofNullable(redisTemplate.keys(key)).orElseThrow(NullPointerException::new);
        return redisTemplate.delete(keys);
    }

    public void addList(String key, List value) {
        redisTemplate.opsForList().leftPushAll(key, value);
    }

    public void setList(String key, List value) {
        delete(key);
        addList(key, value);
    }

    public <T> List<T> getList(String key) {
        return (List<T>) Optional.ofNullable(redisTemplate.opsForList().range(key, 0, -1)).orElse(Collections.emptyList());
    }

    public void setMap(String key, Map value) {
        delete(key);
        redisTemplate.opsForHash().putAll(key, value);
    }

    public <K, V> Map<K, V> getMap(String key) {
        return (Map<K, V>) redisTemplate.opsForHash().entries(key);
    }

    public <V> List<V> getMapValues(String key) {
        return (List<V>) redisTemplate.opsForHash().values(key);
    }

    public Boolean expired(String key, long timeoutSeconds) {
        return redisTemplate.expire(key, Duration.ofSeconds(timeoutSeconds));
    }

    public Long incr(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    public Long incr(String key, long timeoutSeconds) {
        Long value = incr(key);
        this.expired(key, timeoutSeconds);
        return value;
    }

    /**
     * 是否过期
     *
     * @param key
     * @return
     */
    public boolean isExpired(String key) {
        Long r = Optional.ofNullable(redisTemplate.opsForValue().getOperations().getExpire(key)).orElse(0L);
        return r > 0;
    }

    /**
     * 判断某个key是否存在
     *
     * @param key
     * @return
     */
    public boolean exists(String key) {
        return Optional.ofNullable(redisTemplate.hasKey(key)).orElse(false);
    }

    /**
     * 执行Redis Lua脚本
     *
     * e.g:
     * luaScript ==>  redis.call('set',KEYS[1],ARGV[1])
     *
     * @param luaScript Lua脚本
     * @param key 键
     * @param args 参数
     */
    public Object execute(String luaScript, String key, Object... args) {
        return redisTemplate.execute(RedisScript.of(luaScript), Collections.singletonList(key), args);
    }

    /**
     * 执行Redis Lua脚本
     * @param luaScript Lua脚本
     * @param key 键
     */
    public Object execute(String luaScript, String key) {
        return redisTemplate.execute(RedisScript.of(luaScript), Collections.singletonList(key));
    }

    /**
     * 获取Reds信息
     */
    public Map<Object,Object> getRedisInfo() {
        return getConnection().info();
    }

    /**
     * 备份Redis数据
     */
    public void doSave() {
        getConnection().save();
    }

    /**
     * 备份Redis数据(异步)
     */
    public void syncSave() {
        getConnection().bgSave();
    }

    /**
     * 清除当前数据库的所有数据
     */
    public void deleteCurrentDb() {
        getConnection().flushDb();
    }

    /**
     * 清除当前数据库的所有数据
     */
    public void deleteAllDb() {
        getConnection().flushAll();
    }

    private RedisConnection getConnection() {
        RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
        Assert.notNull(factory, "获取Redis连接失败");
        return RedisConnectionUtils.getConnection(factory);
    }
}