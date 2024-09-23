package net.tgburrin.timekeeping.usergroups;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateGroupReq extends CreateGroupReq {
    @JsonProperty("group_id")
    public Long groupId;
}
