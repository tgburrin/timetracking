package net.tgburrin.timekeeping.repositories;

import net.tgburrin.timekeeping.usergroups.Group;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.sql.SQLDataException;

public class GroupRepositoryImpl implements SharedRepositoryInt<Group, Long> {
    private NamedParameterJdbcTemplate queryTemplateExec;
    private BeanPropertyRowMapper<Group> rowMapper = BeanPropertyRowMapper.newInstance(Group.class);
    private static final String insertQuery = "insert into " +
            "timekeeping.groups (name, status, parent_group_id) " +
            "values (:name, :status, :parentGroupId) returning *";
    private static final String updateQuery = "update timekeeping.groups t set " +
            "t.name = :name, " +
            "t.status = :status, " +
            "t.parent_group_id = :parentGroupId " +
            "where t.group_id = :group_id returning *";

    public GroupRepositoryImpl(NamedParameterJdbcTemplate npt) {
        this.queryTemplateExec = npt;
    }

    public Group maintain(Group group) throws SQLDataException {
        String query = insertQuery;
        if (group.getGroupId() != null)
            query = updateQuery;
        SqlParameterSource params = new BeanPropertySqlParameterSource(group);
        return queryTemplateExec.queryForObject(query, params, rowMapper);
    }
}
