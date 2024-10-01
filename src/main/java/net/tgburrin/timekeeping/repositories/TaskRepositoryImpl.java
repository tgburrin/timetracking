package net.tgburrin.timekeeping.repositories;

import net.tgburrin.timekeeping.tasks.Task;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.sql.SQLDataException;

public class TaskRepositoryImpl implements SharedRepositoryInt<Task, Long> {
    private static final String insertQuery = "insert into " +
            "timekeeping.tasks (status, external_id, external_status, external_description) " +
            "values (:status, :externalId, :externalStatus, :externalDescription) returning *";
    private static final String updateQuery = "update timekeeping.tasks t set " +
            "t.status = :status, " +
            "t.external_id = :externalId, " +
            "t.external_status = :externalStatus, " +
            "t.external_description = :externalDescription " +
            "where t.task_id = :taskId returning *";

    private NamedParameterJdbcTemplate queryTemplateExec;
    private BeanPropertyRowMapper<Task> rowMapper = BeanPropertyRowMapper.newInstance(Task.class);
    public TaskRepositoryImpl (NamedParameterJdbcTemplate npt) {
        this.queryTemplateExec = npt;
    }
    @Override
    public Task maintain(Task objIn) throws SQLDataException {
        String query = insertQuery;
        if (objIn.getTaskId() != null)
            query = updateQuery;
        SqlParameterSource params = new BeanPropertySqlParameterSource(objIn);
        return queryTemplateExec.queryForObject(query, params, rowMapper);
    }
}
