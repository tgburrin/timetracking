package net.tgburrin.timekeeping.usergroups;

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
    protected Long userId = null;

    @Column("password_data")
    protected String passwordHash = "";

    @Column("group_id")
    @JsonProperty("group_id")
    protected Long groupId = 0L;

    @Transient
    private Group userGroup;

    public User() {
        super();
    }
    public User (String n) {
        super(n);
    }
    public User (CreateUserReq u, Group g) {
        super(u.name);
        this.passwordHash = u.password;
        this.groupId = g.getGroupId();
    }

    public Long getUserId() {return userId;}

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Group getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(Group userGroup) {
        this.userGroup = userGroup;
    }

    public void setGroupId(Long g) {
        groupId = g;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setPasswordHash(String pwh) {
        this.passwordHash = pwh;
    }

    public String readPasswordHash() {
        return this.passwordHash;
    }
}
