package net.tgburrin.timekeeping.Tasks;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TaskStartReq {
    @JsonProperty("user_id")
    public Long userId;
    @JsonProperty("task_ids")
    public List<Long> taskIds;
}
