package net.tgburrin.timekeeping.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Table(name = "tasks")
public class Task {
    private Instant created;
    private Instant updated;
    @Column("task_id")
    @JsonProperty("task_id")
    @Id
    private Long taskId;
    private Character status;
    private String externalId;
    private String externalStatus;
    private String externalDescription;

    public Task() { this.status = 'O'; }

    /*
     This may, at some point, handle external details - a place for the user to overload with an
     undefined number of extra properties
     */
    public Task(TaskCreateReq req) {
        this.status = 'O';
        this.externalId = req.externalId;
        this.externalStatus = req.externalStatus;
        this.externalDescription = req.externalDescription;
    }
    public Task(String eId, String eStatus, String eDesc) {
        this.status = 'O';
        this.externalId = eId;
        this.externalStatus = eStatus;
        this.externalDescription = eDesc;
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

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Character getStatus() {
        return status;
    }

    public void setOpenedStatus() {
        this.status = 'O';
    }
    public void setClosedStatus() {
        this.status = 'C';
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getExternalStatus() {
        return externalStatus;
    }

    public void setExternalStatus(String externalStatus) {
        this.externalStatus = externalStatus;
    }

    public String getExternalDescription() {
        return externalDescription;
    }

    public void setExternalDescription(String externalDescription) {
        this.externalDescription = externalDescription;
    }

    @Override
    public String toString() {
        List<String> s = new ArrayList<String>();
        s.add("Id: "+(this.taskId == null ? "" : this.taskId.toString()));
        s.add("Status: "+this.status);
        s.add("External ID: "+this.externalId);
        s.add("External Status: "+this.externalStatus);
        s.add("External Description: "+this.externalDescription);
        return String.join("\n", s);
    }
}
