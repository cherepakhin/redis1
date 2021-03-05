package ru.perm.v.redis1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.GenericContainer;

import java.util.Optional;

@SpringBootTest
@ContextConfiguration(initializers = ru.perm.v.redis1.service.AbstractContainerBaseTest.Initializer.class)
public abstract class AbstractContainerBaseTest {

    private static final int REDIS_PORT = 6379;

    // Optional
    @Autowired
    private RedisTemplate redisTemplate;

    // Optional
    protected void cleanCache() {
        Optional.of(redisTemplate.getConnectionFactory().getConnection()).ifPresent(RedisConnection::flushAll);
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static GenericContainer redis = new GenericContainer<>("redis:6-alpine")
                .withExposedPorts(REDIS_PORT)
                .withReuse(true);

        @Override
        public void initialize(ConfigurableApplicationContext context) {
            // Start container
            redis.start();

            // Override Redis configuration
            String redisContainerIP = "redis.host=" + redis.getContainerIpAddress();
            String redisContainerPort = "redis.port=" + redis.getMappedPort(REDIS_PORT); // <- This is how you get the random port.
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(context, redisContainerIP, redisContainerPort); // <- This is how you override the configuration in runtime.
        }
    }
}