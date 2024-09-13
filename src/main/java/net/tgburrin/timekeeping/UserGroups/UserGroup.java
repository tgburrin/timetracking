package net.tgburrin.timekeeping.UserGroups;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(value = "usergroups")
public abstract class UserGroup {
    @Id
    @Column("ug_id")
    @JsonProperty("id")
    protected Long id;

    protected String name;
    protected Character type;
    protected Character status;
    @Column("group_id")
    @JsonProperty("group_id")
    protected Long groupId;

    public UserGroup() {}
    public UserGroup(Character t) {
        type = t;
        status = 'A';
    }
    public UserGroup (String n, Character t) {
        name = n;
        type = t;
        status = 'A';
    }
}
