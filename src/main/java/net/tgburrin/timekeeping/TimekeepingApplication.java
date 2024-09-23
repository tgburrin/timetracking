package net.tgburrin.timekeeping;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.annotation.PostConstruct;
import net.tgburrin.timekeeping.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.Date;
import java.util.TimeZone;


@SpringBootApplication(exclude = {
		SecurityAutoConfiguration.class,
		UserDetailsServiceAutoConfiguration.class,
		ManagementWebSecurityAutoConfiguration.class
	}
)
@OpenAPIDefinition(
		info = @Info(
				title = "Timekeeping API",
				description = "Documentation Timekeeping API v1.0"
		),
		security = { @SecurityRequirement(name = AuthenticationService.AUTH_TOKEN_HEADER_NAME) }
)
@SecurityScheme(
		type = SecuritySchemeType.APIKEY,
		name = AuthenticationService.AUTH_TOKEN_HEADER_NAME,
		in = SecuritySchemeIn.HEADER
)
@EnableJdbcRepositories
public class TimekeepingApplication implements CommandLineRunner {
	@Autowired
	private ApplicationContext ctx;

	public static void main(String[] args) {
		SpringApplication.run(TimekeepingApplication.class, args);
	}
	@Override
	public void run(String... args) {
		System.out.println("Application running...");
		System.out.println("API Token is "+AuthenticationService.getAuthToken());
		LettuceConnectionFactory lcf = ctx.getBean("redisConnectionFactory", LettuceConnectionFactory.class);
		System.out.println("Redis pass is "+lcf.getPassword());
	}
	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		System.out.println("Spring boot application running in UTC timezone :"+new Date());
	}
}
