package net.tgburrin.timekeeping.repositories;

import net.tgburrin.timekeeping.AuthPermission.GroupPermission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends CrudRepository<GroupPermission, Long>  {
}
