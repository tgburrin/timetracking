package net.tgburrin.timekeeping.repositories;

import jakarta.validation.constraints.NotNull;
import net.tgburrin.timekeeping.Tasks.TaskTime;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskTimeRepository extends CrudRepository<TaskTime, UUID> {
    @Override
    @Query("select *, lower(task_time) as start_dt, upper(task_time) as end_dt from timekeeping.task_time_instances where id = :id")
    Optional<TaskTime> findById(UUID id);

    @Query("select *, lower(task_time) as start_dt, upper(task_time) as end_dt from timekeeping.task_time_instances where upper(task_time) = 'Infinity'")
    List<TaskTime> findAllActiveTasks();

    @Query("select *, lower(task_time) as start_dt, upper(task_time) as end_dt from timekeeping.task_time_instances where user_id = :uid and upper(task_time) = 'Infinity'")
    List<TaskTime> findAllActiveUserTasks(@Param("uid") Long userId);

    @Query("insert into timekeeping.task_time_instances (id , user_id, task_id, task_time) values (:rid, :uid, :tid, tstzrange(now(), 'Infinity'::timestamptz, '[)'))")
    TaskTime startTask(@Param("rid") Long rowId, @Param("uid") Long userId, @Param("tid") Long taskId);
}
