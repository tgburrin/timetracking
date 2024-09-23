package net.tgburrin.timekeeping.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.tgburrin.timekeeping.Tasks.Task;
import net.tgburrin.timekeeping.Tasks.TaskTime;
import net.tgburrin.timekeeping.repositories.TaskRepository;
import net.tgburrin.timekeeping.repositories.TaskTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskService {
    @Autowired
    TaskTimeRepository tskTimeRepo;

    @Autowired
    TaskRepository tskRepo;

    private final ObjectMapper objectMapper = new ObjectMapper();

    //@Transactional
    public List<TaskTime> startTasks(Long userId, List<Long> taskIds) {
        return tskTimeRepo.startTasks(userId, taskIds);
    }

    //@Transactional
    public List<TaskTime> stopTasks(Long userId, List<Long> taskIds) {
        return tskTimeRepo.stopTasks(userId, taskIds);
    }

    public List<TaskTime> findAllActiveUserTasks(Long userId) {
        return tskTimeRepo.findAllActiveUserTasks(userId);
    }

    public List<TaskTime> findAllActiveTasks() {
        return tskTimeRepo.findAllActiveTasks();
    }

    public TaskTime findTaskTimeById(UUID id) {
        return tskTimeRepo.findById(id).orElse(null);
    }

    // --- Task Methods
    public List<Task> findOpenTasks() {
        return tskRepo.findOpenTasks();
    }

    public Task createTask(Task newTask) throws JsonProcessingException {
        return tskRepo.save(newTask);
    }
}
