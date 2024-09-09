package net.tgburrin.timekeeping.UserGroups;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import net.tgburrin.timekeeping.InvalidDataException;

public class Group extends UserGroup {
    public Group() {
        this.id = null;
        this.type = 'G';
    }

    public Group(Long id, String name, Long parentGroupId) {
        this.id = id;
        this.name = name;
        this.type = 'G';
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
        //s.add("Status: "+this.status);

        return String.join("\n", s);
    }

    public void validateRecord() throws InvalidDataException {
        if(this.name == null || this.name.equals(""))
            throw new InvalidDataException("A valid group name must be specified");
    }
}