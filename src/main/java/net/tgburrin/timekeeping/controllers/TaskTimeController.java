package net.tgburrin.timekeeping.controllers;

import net.tgburrin.timekeeping.InvalidRecordException;
import net.tgburrin.timekeeping.Tasks.Task;
import net.tgburrin.timekeeping.Tasks.TaskTime;
import net.tgburrin.timekeeping.UserGroups.Group;
import net.tgburrin.timekeeping.services.TaskTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/${apiPrefix}/${apiVersion}/task_time")
public class TaskTimeController {
    @Autowired
    TaskTimeService tsService;

    @RequestMapping(value="/read/id/{id}", method= RequestMethod.GET)
    public TaskTime findTaskTime(@PathVariable("id") UUID id) throws Exception {
        TaskTime t = tsService.findTaskTimeById(id);
        if(t == null)
            throw new InvalidRecordException("Task time id "+id.toString()  +" could not be found");
        return t;
    }
}
