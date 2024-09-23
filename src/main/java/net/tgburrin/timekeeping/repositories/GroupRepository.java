package net.tgburrin.timekeeping.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import net.tgburrin.timekeeping.UserGroups.Group;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends CrudRepository<Group, Long> {
    @Override
    @Query("select * from timekeeping.groups where group_id=:id")
    Optional<Group> findById(@Param("id") Long groupId);

    @Query("select * from timekeeping.groups where status = 'A'")
    List<Group> findAllActive();

    @Query("insert into timekeeping.groups (name,group_id) values (:name, :groupId)")
    Group addGroup(@Param("name") String name, @Param("groupId") Long groupId );
}
