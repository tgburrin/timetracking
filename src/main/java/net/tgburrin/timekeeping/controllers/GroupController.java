package net.tgburrin.timekeeping.controllers;

import net.tgburrin.timekeeping.UserGroups.Group;
import net.tgburrin.timekeeping.services.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${apiPrefix}/${apiVersion}/group")
public class GroupController {
    @Autowired
    UserGroupService ugService;

    @PostMapping(value="/create", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Group createGroup(@RequestBody Group group) throws Exception {
        return ugService.createGroup(group);
    }

    @RequestMapping(value="/list", method= RequestMethod.GET)
    public List<Group> listGroups() throws Exception {
        return ugService.findAllGroups();
    }
}
