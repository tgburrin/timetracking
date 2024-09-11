package net.tgburrin.timekeeping.UserGroups;

import java.util.ArrayList;
import java.util.List;

import net.tgburrin.timekeeping.InvalidDataException;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

@Table(value = "timekeeping.usergroups")
public class Group extends UserGroup {
    @Transient
    private Group parentGroup;

    public Group(String name, Group parentGroup) {
        super(name, 'G');
        this.id = null;
    }

    public Group(Long id, String name, Long parentGroupId) {
        this.id = id;
        this.name = name;
        this.type = 'G';
        this.groupId = parentGroupId;
    }

    public long readId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String n) {
        this.name = n;
    }

    public Group getParentGroup() {
        return this.parentGroup;
    }
    public void setParentGroup(Group g) {
        this.parentGroup = g;
        this.groupId = g != null ? g.readId() : 0L;
    }

    @Override
    public String toString() {
        List<String> s = new ArrayList<String>();
        s.add("Id: "+this.id);
        s.add("Name: "+this.name);
        s.add("Type: "+this.type);
        s.add("Status: "+this.status);
        s.add("Group: "+this.groupId);

        return String.join("\n", s);
    }

    public void validateRecord() throws InvalidDataException {
        if(this.name == null || this.name.equals(""))
            throw new InvalidDataException("A valid group name must be specified");
    }
}