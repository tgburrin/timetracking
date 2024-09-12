package net.tgburrin.timekeeping.UserGroups;

import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

@Table(value = "usergroups")
public class User extends UserGroup {
    @Transient
    private Group userGroup;
    public User (String n) {
        super(n, 'U');
    }
}
