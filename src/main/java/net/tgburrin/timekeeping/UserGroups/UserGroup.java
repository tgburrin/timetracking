package net.tgburrin.timekeeping.UserGroups;

import java.time.Instant;

import net.tgburrin.timekeeping.InvalidDataException;

public abstract class UserGroup {
    private Instant created;
    private Instant updated;

    protected String name;
    protected Character status;
    public UserGroup() {status = 'A';}
    public UserGroup (String n) {
        name = n;
        status = 'A';
    }

    public Instant getCreated() {
        return created;
    }

    public Instant getUpdated() {
        return updated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void validateRecord() throws InvalidDataException {
        if(this.name == null || this.name.equals(""))
            throw new InvalidDataException("A valid name must be specified");
    }
}
