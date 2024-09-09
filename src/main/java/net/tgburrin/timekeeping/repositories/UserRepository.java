package net.tgburrin.timekeeping.repositories;

import net.tgburrin.timekeeping.UserGroups.Group;
import net.tgburrin.timekeeping.UserGroups.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long>  {
    @Override
    @Query("select * from timekeeping.usergroups where type='U' and ug_id=:id")
    Optional<User> findById(@Param("id") Long userId);

    @Query("select * from timekeeping.usergroups where type='U' and name=:name")
    Optional<User> findByName(@Param("name") String userName);
}
