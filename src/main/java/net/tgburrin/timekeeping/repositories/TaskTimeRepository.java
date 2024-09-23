package net.tgburrin.timekeeping.repositories;

import net.tgburrin.timekeeping.tasks.TaskTime;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskTimeRepository extends CrudRepository<TaskTime, UUID>, SharedRepositoryInt<TaskTime, UUID> {
    @Override
    @Query("select * from timekeeping.task_time_instances where id = :id")
    Optional<TaskTime> findById(UUID id);

    @Query("select * from timekeeping.task_time_instances where upper(task_time) = 'Infinity'")
    List<TaskTime> findAllActiveTasks();

    @Query("select * from timekeeping.task_time_instances " +
            "where user_id = :uid and upper(task_time) = 'Infinity'")
    List<TaskTime> findAllActiveUserTasks(@Param("uid") Long userId);

    @Query("select * from timekeeping.task_time_instances " +
            "where user_id = :uid and task_id = ANY(:tid) and upper(task_time) = 'Infinity'")
    List<TaskTime> findAllActiveUserTaskSet(
            @Param("uid") Long userId,
            @Param("tid") List<Long> taskIds
    );

    @Query("select count(*) = 1 from timekeeping.task_time_instances where id = :id")
    Boolean testTaskTimeIdExists (@Param("id") UUID id);

    // https://stackoverflow.com/questions/1715711/how-to-update-a-postgresql-array-column-with-spring-jdbctemplate
    @Query("select * from timekeeping.start_task_times(:uid, ARRAY[:tids])")
    List<TaskTime> startTasks(@Param("uid") Long userId, @Param("tids") List<Long> taskIds);

    @Query("select * from timekeeping.stop_task_times(:uid, ARRAY[:tids])")
    List<TaskTime> stopTasks(@Param("uid") Long userId, @Param("tids") List<Long> taskIds);

    @Query("select * from timekeeping.set_task_times(:rid, :start_dt, :end_dt)")
    TaskTime setTaskTimes(
            @Param("rid") UUID rowId,
            @Param("start_dt") Instant startDt,
            @Param("end_dt") Instant endDt
    );

    @Query("select count(*) as total " +
            "from " +
                "timekeeping.task_time_instances t1 " +
                "join timekeeping.task_time_instances t2 on " +
                    "t1.user_id = t2.user_id and t1.id != t2.id " +
            "where t1.id != :rid " +
            "and t2.task_time && tstzrange(:start_dt, :end_dt, '[)')")
    Long checkConflictingRange(@Param("rid") UUID taskTimeId, @Param("start_dt") Instant startDt, @Param("end_dt") Instant endDt);

    @Query("delete from timekeeping.task_time_instances where id = :rid returning *")
    TaskTime deleteTaskTime(
            @Param("rid") UUID rid
    );
}
