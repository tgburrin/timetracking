package net.tgburrin.timekeeping.repositories;

import net.tgburrin.timekeeping.Tasks.TaskTime;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskTimeRepository extends CrudRepository<TaskTime, UUID> {
    @Override
    @Query("select *, lower(task_time) as start_dt, nullif(upper(task_time), 'Infinity') as end_dt from timekeeping.task_time_instances where id = :id")
    Optional<TaskTime> findById(UUID id);

    @Query("select *, lower(task_time) as start_dt, nullif(upper(task_time), 'Infinity') as end_dt from timekeeping.task_time_instances where upper(task_time) = 'Infinity'")
    List<TaskTime> findAllActiveTasks();

    @Query("select *, lower(task_time) as start_dt, nullif(upper(task_time), 'Infinity') as end_dt from timekeeping.task_time_instances where user_id = :uid and upper(task_time) = 'Infinity'")
    List<TaskTime> findAllActiveUserTasks(@Param("uid") Long userId);

    @Query("select *, lower(task_time) as start_dt, nullif(upper(task_time), 'Infinity') as end_dt from timekeeping.task_time_instances where user_id = :uid and task_id = ANY(:tid) and upper(task_time) = 'Infinity'")
    List<TaskTime> findAllActiveUserTaskSet(@Param("uid") Long userId, @Param("tid") List<Long> taskIds);

    @Query("insert into timekeeping.task_time_instances (id , user_id, task_id, task_time) values (:rid, :uid, :tid, tstzrange(now(), 'Infinity'::timestamptz, '[)')) on conflict do nothing returning *, lower(task_time) as start_dt, nullif(upper(task_time), 'Infinity') as end_dt")
    TaskTime startTask(@Param("rid") UUID rowId, @Param("uid") Long userId, @Param("tid") Long taskId);

    @Query("select * from timekeeping.start_task_times(:uid, :tids)")
    List<TaskTime> startTasks(@Param("uid") Long userId, @Param("tids") List<Long> taskIds);

    @Query("update timekeeping.task_time_instances set task_time = tstzrange(lower(task_time), now(), '[)') where id = :rid and upper(task_time) = 'Infinity' returning *, lower(task_time) as start_dt, nullif(upper(task_time), 'Infinity') as end_dt")
    TaskTime stopTask(@Param("rid") UUID rowId);

    @Query("select * from timekeeping.stop_task_times(:uid, :tids)")
    List<TaskTime> stopTasks(@Param("uid") Long userId, @Param("tids") List<Long> taskIds);

    @Query("select * from timekeeping.set_task_times(:rid, :start_dt, :end_dt)")
    TaskTime correctTaskTime(@Param("rid") UUID rowId, @Param("start_dt") Instant startDt, @Param("end_dt") Instant endDt);
}
