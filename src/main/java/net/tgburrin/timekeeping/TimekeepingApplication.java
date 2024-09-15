package net.tgburrin.timekeeping;

import java.util.Date;
import java.util.TimeZone;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.annotation.PostConstruct;
import net.tgburrin.timekeeping.services.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {
		SecurityAutoConfiguration.class,
		UserDetailsServiceAutoConfiguration.class
})
@OpenAPIDefinition(
		info = @Info(
				title = "Timekeeping API",
				version = "${springdoc.version}",
				description = "Documentation Timekeeping API v1.0"
		),
		security = { @SecurityRequirement(name = AuthenticationService.AUTH_TOKEN_HEADER_NAME) }
)
@SecurityScheme(
		type = SecuritySchemeType.APIKEY,
		name = AuthenticationService.AUTH_TOKEN_HEADER_NAME,
		in = SecuritySchemeIn.HEADER
)
public class TimekeepingApplication implements CommandLineRunner {
	public static final String dbSchema = "timekeeping";

	public static void main(String[] args) {
		SpringApplication.run(TimekeepingApplication.class, args);
	}
	@Override
	public void run(String... args) {
		System.out.println("Application running...");
		System.out.println(("API token is "+ AuthenticationService.getAuthToken()));
	}
	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		System.out.println("Spring boot application running in UTC timezone :"+new Date());
	}
}
