package net.tgburrin.timekeeping.UserGroups;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.tgburrin.timekeeping.InvalidDataException;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "groups")
public class Group extends UserGroup {
    @Id
    @Column("group_id")
    @JsonProperty("group_id")
    protected Long groupId;

    @Column("parent_group_id")
    @JsonIgnore
    protected Long parentGroupId;

    public Group() {
        super();
        this.parentGroupId = 0L;
    }
    public Group(String name) {
        super(name);
        this.groupId = null;
        this.parentGroupId = 0L;
    }

    public Group(Long id, String name) {
        this.groupId = id;
        this.name = name;
        this.parentGroupId = 0L;
    }

    public long getGroupId() {
        return this.groupId;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String n) {
        this.name = n;
    }

    @Override
    public String toString() {
        List<String> s = new ArrayList<String>();
        s.add("GroupId: "+this.groupId);
        s.add("Name: "+this.name);
        s.add("Status: "+this.status);
        s.add("ParentGroupId: "+this.parentGroupId);

        return String.join("\n", s);
    }

}