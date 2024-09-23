package net.tgburrin.timekeeping.repositories;

import net.tgburrin.timekeeping.usergroups.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.sql.SQLDataException;

public class UserRepositoryImpl implements SharedRepositoryInt<User, Long> {
    private NamedParameterJdbcTemplate queryTemplateExec;
    private static final String insertQuery = "insert into " +
            "timekeeping.users (name, password_data, status, group_id) " +
            "values (:name, :passwordHash, :status, :groupId) returning *";
    private static final String updateQuery = "update timekeeping.users set " +
            "name = :name, password_data = :passwordHash, status = :status, " +
            "group_id = :groupId " +
            "where user_id = :user_id";

    public UserRepositoryImpl(NamedParameterJdbcTemplate npt) {
        this.queryTemplateExec = npt;
    }
    private BeanPropertyRowMapper<User> rowMapper = BeanPropertyRowMapper.newInstance(User.class);

    @Override
    public User maintain(User objIn) throws SQLDataException {
        String query = insertQuery;
        if (objIn.getUserId() != null)
            query = updateQuery;
        SqlParameterSource params = new BeanPropertySqlParameterSource(objIn);
        return queryTemplateExec.queryForObject(query, params, rowMapper);
    }
}
