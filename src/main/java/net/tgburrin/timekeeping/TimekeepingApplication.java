package net.tgburrin.timekeeping;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
@OpenAPIDefinition(info =
@Info(title = "Timekeeping API", version = "${springdoc.version}", description = "Documentation Timekeeping API v1.0")
)
public class TimekeepingApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TimekeepingApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		System.out.println("Application running...");
	}
	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		System.out.println("Spring boot application running in UTC timezone :"+new Date());
	}
}
