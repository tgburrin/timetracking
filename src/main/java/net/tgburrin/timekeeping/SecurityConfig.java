package net.tgburrin.timekeeping;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/*
https://medium.com/@sallu-salman/implementing-token-based-authentication-in-a-spring-boot-project-dba7811ffcee
 */

@Configuration
@EnableWebSecurity(debug = false)
public class SecurityConfig {
}
