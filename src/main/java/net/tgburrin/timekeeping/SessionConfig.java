package net.tgburrin.timekeeping;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

// https://docs.spring.io/spring-session/reference/guides/java-rest.html
// https://docs.spring.io/spring-session/docs/1.0.1.RELEASE/reference/html5/guides/users.html

@Configuration(proxyBeanMethods = false)
@EnableRedisHttpSession
public class SessionConfig {
    private static String redisHost;
    private static Integer redisPort;
    private static String redisPassword;
    private static Integer redisDatabase;

    @Value("${spring.data.redis.host:localhost}")
    public void setRedisHost(String host) { redisHost = host; }
    @Value("${spring.data.redis.port:6379}")
    public void setRedisPort(Integer port) { redisPort = port; }
    @Value("${spring.data.redis.password:}")
    public void setRedisPassword(String password) { redisPassword = password; }
    @Value("${spring.data.redis.database:0}")
    public void setRedisDatabase(Integer db) { redisDatabase = db; }
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration cfg = new RedisStandaloneConfiguration();
        cfg.setPassword(redisPassword);
        cfg.setHostName(redisHost);
        cfg.setPort(redisPort);
        cfg.setDatabase(redisDatabase);
        return new LettuceConnectionFactory(cfg);
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("TK_SESSION");
        //serializer.setCookiePath("/");
        //serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
        return serializer;
    }
}
