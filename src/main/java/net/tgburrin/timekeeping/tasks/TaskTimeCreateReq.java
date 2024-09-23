package net.tgburrin.timekeeping.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class TaskTimeCreateReq {
    @JsonProperty("user_id")
    public Long userId;
    @JsonProperty("task_id")
    public Long taskId;
    @JsonProperty("start_dt")
    public Instant startDt;
    @JsonProperty("end_dt")
    public Instant endDt;
}
