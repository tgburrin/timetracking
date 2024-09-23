package net.tgburrin.timekeeping;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
class JdbcConfig extends AbstractJdbcConfiguration {
    /*
    @Bean
    NamedParameterJdbcOperations namedParameterJdbcOperations(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
    @Bean
    TransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    */
    @Override
    protected List<?> userConverters() {
        return Arrays.asList(new InstantToTstzConverter(), new TstzToInstantConverter());
    }
}