package net.tgburrin.timekeeping.usergroups;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "groups")
public class Group extends UserGroup {
    @Id
    @Column("group_id")
    @JsonProperty("group_id")
    private Long groupId = null;

    @Column("parent_group_id")
    @JsonIgnore
    private Long parentGroupId = 0L;

    public Group() {
        super();
    }

    public Group(CreateGroupReq c) {
        super(c.groupName);
    }

    public Long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getParentGroupId() {
        return parentGroupId;
    }
    public void setParentGroupId(Long gid) {this.parentGroupId = gid;}
    public void setParentGroup(Group parentGroup) {
        this.parentGroupId = parentGroup.getGroupId();
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