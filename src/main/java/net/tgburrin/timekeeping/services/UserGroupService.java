package net.tgburrin.timekeeping.services;

import net.tgburrin.timekeeping.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserGroupService {
    @Autowired
    private GroupRepository groupRepo;
}
