package net.tgburrin.timekeeping.UserGroups;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(value = "users")
public class User extends UserGroup {
    @Id
    @Column("user_id")
    @JsonProperty("user_id")
    protected Long userId;

    @Column("group_id")
    @JsonProperty("group_id")
    protected Long groupId;


    @Transient
    private Group userGroup;

    public User() {
        super();
        this.userId = null;
        this.groupId = 0L;
    }
    public User (String n) {
        super(n);
        this.userId = null;
        this.groupId = 0L;
    }

    public Long getUserId() {return userId;}

    public void setGroupId(Long g) {
        groupId = g;
    }

    public Long getGroupId() {
        return groupId;
    }
}
