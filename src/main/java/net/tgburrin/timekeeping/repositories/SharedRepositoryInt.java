package net.tgburrin.timekeeping.repositories;

import java.sql.SQLDataException;

public interface SharedRepositoryInt<T, ID> {
    /*
    https://www.baeldung.com/spring-jdbc-jdbctemplate
    The nearest possibility to overwrite save() and return * from an insert/update

    https://springframework.guru/spring-jdbctemplate-crud-operations/
    https://docs.spring.io/spring-data/jpa/reference/repositories/custom-implementations.html
    */
    T maintain(T objIn) throws SQLDataException;
    //<S extends T> S save(S entity);
}
