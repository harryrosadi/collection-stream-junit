package org.example.kerjaSchedulerRedis.redisConfig;

/**
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {
	@Value("${redis.host}")
	private String redisHost;

	@Value("${redis.port}")
	private Integer redisPort;

	@Value("${redis.readTimeout}")
	private Integer readTimeout;

	@Value("${redis.connectTimeout}")
	private Integer connectTimeout;

	@Value("${redis.database}")
	private String redisDatabase;

	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		JedisClientConfiguration clientConfiguration = JedisClientConfiguration.builder()
				.readTimeout(Duration.ofMillis(
						readTimeout)).
				connectTimeout(Duration.ofMillis(
						connectTimeout)).usePooling()
				.build();

		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(
				redisHost, redisPort);
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(
				redisStandaloneConfiguration,
				clientConfiguration
		);

		jedisConnectionFactory.setDatabase(Integer.parseInt(redisDatabase));

		return jedisConnectionFactory;
	}

	@Bean
	public RedisTemplate<String, Integer> redisTemplate() {
		RedisTemplate<String, Integer> template = new RedisTemplate<>();
		template.setConnectionFactory(jedisConnectionFactory());
		template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		return template;
	}
}
 */

