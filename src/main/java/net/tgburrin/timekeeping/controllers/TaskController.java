package net.tgburrin.timekeeping.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.tgburrin.timekeeping.InvalidDataException;
import net.tgburrin.timekeeping.Tasks.Task;
import net.tgburrin.timekeeping.Tasks.TaskCreateReq;
import net.tgburrin.timekeeping.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${apiPrefix}/${apiVersion}/task")
public class TaskController {
    @Autowired
    TaskService tsService;

    @RequestMapping(value="/list/status/open", method=RequestMethod.GET)
    public List<Task> findOpenTasks() {
        return tsService.findOpenTasks();
    }

    @PostMapping(value="/maintain/", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@RequestBody TaskCreateReq req) {
        req.validate();
        Task nt = new Task(req);
        try {
            return tsService.createTask(nt);
        } catch (JsonProcessingException e) {
            throw new InvalidDataException(e.getMessage());
        }
    }
}
