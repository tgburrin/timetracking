package net.tgburrin.timekeeping.UserGroups;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(value = "timekeeping.usergroups")
public abstract class UserGroup {
    @Id
    protected Long id;

    protected String name;
    protected Character type;
    protected Character status;
}
