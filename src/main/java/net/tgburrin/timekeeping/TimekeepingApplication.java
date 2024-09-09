package net.tgburrin.timekeeping;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
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
