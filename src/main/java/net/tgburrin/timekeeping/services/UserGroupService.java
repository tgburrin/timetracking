package net.tgburrin.timekeeping.services;

import net.tgburrin.timekeeping.UserGroups.Group;
import net.tgburrin.timekeeping.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserGroupService {
    @Autowired
    private GroupRepository groupRepo;

    public List<Group> findAllGroups() {
        List<Group> groups = new ArrayList<Group>();
        groupRepo.findAllActive().forEach(groups::add);
        return groups;
    }
}
