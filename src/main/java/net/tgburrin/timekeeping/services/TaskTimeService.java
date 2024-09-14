package net.tgburrin.timekeeping.services;

import net.tgburrin.timekeeping.Tasks.TaskTime;
import net.tgburrin.timekeeping.repositories.TaskTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskTimeService {
    @Autowired
    TaskTimeRepository tskTimeRepo;

    @Transactional
    public List<TaskTime> startTasks(Long userId, List<Long> taskIds) {
        ArrayList<TaskTime> rv = new ArrayList<TaskTime>();
        for(Long taskId : taskIds) {
            TaskTime t = tskTimeRepo.startTask(UUID.randomUUID(), userId, taskId);
            if ( t != null )
                rv.add(t);
        }
        return rv;
    }

    @Transactional
    public List<TaskTime> stopTasks(List<UUID> taskTimeIds) {
        ArrayList<TaskTime> rv = new ArrayList<TaskTime>();
        for(UUID taskTimeId : taskTimeIds) {
            TaskTime t = tskTimeRepo.stopTask(taskTimeId);
            if ( t != null )
                rv.add(t);
        }
        return rv;
    }

    public List<TaskTime> findAllActiveUserTasks(Long userId) {
        return tskTimeRepo.findAllActiveUserTasks(userId);
    }

    public TaskTime findTaskTimeById(UUID id) {
        Optional<TaskTime> ft = tskTimeRepo.findById(id);
        return ft.orElse(null);
    }

}
