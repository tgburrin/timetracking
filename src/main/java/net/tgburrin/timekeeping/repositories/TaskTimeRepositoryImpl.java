package net.tgburrin.timekeeping.repositories;

import net.tgburrin.timekeeping.tasks.TaskTime;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.sql.SQLDataException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/*
https://stackoverflow.com/questions/43066254/how-to-handle-jdbc-queryforobject-if-it-doesnt-return-a-row
*/

public class TaskTimeRepositoryImpl implements SharedRepositoryInt<TaskTime, UUID> {
    private static final String insertQuery = "insert into " +
            "timekeeping.task_time_instances (id, task_id, user_id, task_time) " +
            "values (" +
            "public.uuid_generate_v4(), " +
            ":taskId, " +
            ":userId, " +
            "tstzrange(coalesce(:startDt,  clock_timestamp()), coalesce(:endDt, 'Infinity'), '[)')) " +
            "returning *";
    private static final String updateQuery = "update timekeeping.task_time_instances t set " +
            "task_id = :taskId, " +
            "user_id = :userId, " +
            // the start date may not be null, ever.  the end date if null will be set to 'Infinity'
            "task_time = tstzrange(" +
                "coalesce(:startDt,  lower(t.task_time))::timestamptz," +
                "coalesce(:endDt, 'Infinity')::timestamptz," +
                "'[)'" +
            ") " +
            "where t.id = :id " +
            "returning *";

    private NamedParameterJdbcTemplate queryTemplateExec;
    private BeanPropertyRowMapper<TaskTime> rowMapper = BeanPropertyRowMapper.newInstance(TaskTime.class);
    public TaskTimeRepositoryImpl (NamedParameterJdbcTemplate npt) {
        this.queryTemplateExec = npt;
    }
    @Override
    public TaskTime maintain(TaskTime objIn) throws SQLDataException {
        String query = insertQuery;
        if (objIn.getTaskId() != null)
            query = updateQuery;
        SqlParameterSource params = new BeanPropertySqlParameterSource(objIn) {
            @Override
            public Object getValue(String paramName) throws IllegalArgumentException {
                Object value = super.getValue(paramName);
                if (value instanceof Instant) {
                    return Timestamp.from((Instant)value);
                }
                return value;
            }
        };
        // throws org.springframework.dao.EmptyResultDataAccessException for queryForObject if no rows
        List<TaskTime> rv = queryTemplateExec.query(query, params, rowMapper);
        if ( rv.size() > 1 )
            throw new SQLDataException(rv.size()+" rows returned where 0 or 1 was expected");
        return rv.isEmpty() ? null : rv.get(0);
    }
}
