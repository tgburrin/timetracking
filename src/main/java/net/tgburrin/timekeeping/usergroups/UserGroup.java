package net.tgburrin.timekeeping.usergroups;

import java.time.Instant;

import net.tgburrin.timekeeping.exceptions.InvalidDataException;

public abstract class UserGroup {
    protected Instant created;
    protected Instant updated;

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

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getUpdated() {
        return updated;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public void validate() throws InvalidDataException {
        if(this.name == null || this.name.isEmpty())
            throw new InvalidDataException("A valid name must be specified");
    }
}
