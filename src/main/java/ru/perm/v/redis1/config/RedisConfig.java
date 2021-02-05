package ru.perm.v.redis1.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import ru.perm.v.redis1.repository.StudentRepository;
import ru.perm.v.redis1.service.StudentService;

@Configuration
public class RedisConfig {

    private static final Logger LOG = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${redis.host}")
    String redisHost;
    @Value("${redis.port}")
    Integer redisPort;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        LOG.info("Redis host={} port={}", redisHost, redisPort);
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(redisHost, redisPort));
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    public StudentService studentService(StudentRepository studentRepository) {
        return new StudentService(studentRepository);
    }
}
