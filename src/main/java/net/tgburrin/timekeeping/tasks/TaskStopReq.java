package net.tgburrin.timekeeping.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TaskStopReq {
    @JsonProperty("user_id")
    public Long userId;
    @JsonProperty("task_ids")
    public List<Long> taskIds;
}
