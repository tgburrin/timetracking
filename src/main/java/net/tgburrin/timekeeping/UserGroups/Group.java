package net.tgburrin.timekeeping.UserGroups;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.tgburrin.timekeeping.InvalidDataException;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "usergroups")
public class Group extends UserGroup {
    @Column("group_id")
    @JsonIgnore
    protected Long groupId;

    public Group() {
        super('G');
        this.groupId = 0L;
    }
    public Group(String name) {
        super(name, 'G');
        this.id = null;
        this.groupId = 0L;
    }

    public Group(Long id, String name) {
        this.id = id;
        this.name = name;
        this.type = 'G';
        this.groupId = 0L;
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

    @Override
    public String toString() {
        List<String> s = new ArrayList<String>();
        s.add("Id: "+this.id);
        s.add("Name: "+this.name);
        s.add("Type: "+this.type);
        s.add("Status: "+this.status);

        return String.join("\n", s);
    }

    public void validateRecord() throws InvalidDataException {
        if(this.name == null || this.name.equals(""))
            throw new InvalidDataException("A valid group name must be specified");
    }
}