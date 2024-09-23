package net.tgburrin.timekeeping.repositories;

import net.tgburrin.timekeeping.usergroups.Group;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends CrudRepository<Group, Long>, SharedRepositoryInt<Group, Long> {
    @Query("select * from timekeeping.groups where name=:name")
    Optional<Group> findByName(@Param("name") String groupName);

    @Query("select count(*) = 1 from timekeeping.groups where name=:name")
    Boolean testNameExists(@Param("name") String groupName);

    @Query("select * from timekeeping.groups where status = 'A'")
    List<Group> findAllActive();

    @Query("insert into timekeeping.groups (name) values (:name) returning *")
    Group addGroup(@Param("name") String name);
}
