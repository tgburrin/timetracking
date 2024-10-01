package net.tgburrin.timekeeping.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import net.tgburrin.timekeeping.exceptions.InvalidDataException;

import java.util.ArrayList;

public class TaskCreateReq {
    @NotBlank(message = "External ID is required")
    @JsonProperty("external_id")
    public String externalId;
    @NotBlank(message = "External status is required")
    @JsonProperty("external_status")
    public String externalStatus;
    @NotBlank(message = "External description is required")
    @JsonProperty("external_description")
    public String externalDescription;

    public void validate() {
        ArrayList<String> msgs = new ArrayList<>();
        if ( externalId == null || externalId.isEmpty() )
            msgs.add("External ID must be populated");
        if ( externalStatus == null || externalStatus.isEmpty() )
            msgs.add("External status must be populated");
        if ( externalDescription == null || externalDescription.isEmpty() )
            msgs.add("External description must be populated");
        if ( !msgs.isEmpty() )
            throw new InvalidDataException(msgs);
    }
}
