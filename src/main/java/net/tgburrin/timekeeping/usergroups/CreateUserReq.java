package net.tgburrin.timekeeping.usergroups;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateUserReq {
    public String name;
    public String password;
    @JsonProperty("group_name")
    public String groupName;
}
