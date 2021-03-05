package ru.perm.v.redis1.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CacheTest extends AbstractContainerBaseTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @AfterEach
    void tearDown() {
        cleanCache();
    }

    @Test
    public void testSomeMethodUsingRedis() {
        String KEY ="KEY";
        String VALUE ="VALUE";
        redisTemplate.opsForValue().set(KEY,VALUE);
        assertEquals(VALUE,redisTemplate.opsForValue().get(KEY));
    }
}