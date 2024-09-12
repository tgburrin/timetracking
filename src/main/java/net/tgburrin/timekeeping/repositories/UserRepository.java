package net.tgburrin.timekeeping.repositories;

import net.tgburrin.timekeeping.UserGroups.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long>  {
}
