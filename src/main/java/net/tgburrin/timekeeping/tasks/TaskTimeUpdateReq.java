package net.tgburrin.timekeeping.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.UUID;

public class TaskTimeUpdateReq {
    @JsonProperty("task_time_id")
    public UUID taskTimeId;
    @JsonProperty("start_dt")
    public Instant startDt;
    @JsonProperty("end_dt")
    public Instant endDt;
}
