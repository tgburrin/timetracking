package net.tgburrin.timekeeping.services;

import net.tgburrin.timekeeping.InvalidDataException;
import net.tgburrin.timekeeping.NoRecordFoundException;
import net.tgburrin.timekeeping.UserGroups.Group;
import net.tgburrin.timekeeping.UserGroups.User;
import net.tgburrin.timekeeping.repositories.GroupRepository;
import net.tgburrin.timekeeping.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserGroupService {
    @Autowired
    private GroupRepository groupRepo;

    @Autowired
    private UserRepository userRepo;

    public Group findGroupById(Long id) {
        Optional<Group> fg = groupRepo.findById(id);
        return fg.orElse(null);
    }
    public List<Group> findAllGroups() {
        return new ArrayList<Group>(groupRepo.findAllActive());
    }

    public Group createGroup(Group ng) throws InvalidDataException {
        Group g = new Group(ng.getName());
        g.validateRecord();
        return groupRepo.save(g);

    }

    public Group updateGroup(Group g) throws InvalidDataException, NoRecordFoundException {
        Optional<Group> ng = groupRepo.findById(g.getGroupId());
        if ( ng.isEmpty() )
            throw new NoRecordFoundException("Group Id "+g.getGroupId()+" could not be found");
        g.validateRecord();
        return groupRepo.save(g);
    }

    public User findUserByName(String userName) {
        return userRepo.findByName(userName).orElse(null);
    }
    public List<User> listUsers() {
        return userRepo.listUsers();
    }
}
