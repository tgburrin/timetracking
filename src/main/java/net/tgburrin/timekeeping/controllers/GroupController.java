package net.tgburrin.timekeeping.controllers;

import net.tgburrin.timekeeping.exceptions.InvalidDataException;
import net.tgburrin.timekeeping.exceptions.InvalidRecordException;
import net.tgburrin.timekeeping.services.UserGroupService;
import net.tgburrin.timekeeping.usergroups.CreateGroupReq;
import net.tgburrin.timekeeping.usergroups.Group;
import net.tgburrin.timekeeping.usergroups.UpdateGroupReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${apiPrefix}/${apiVersion}/group")
public class GroupController {
    @Autowired
    UserGroupService ugService;

    @PostMapping(value="/maintain/", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Group createGroup(@RequestBody CreateGroupReq group) throws Exception {
        return ugService.createGroup(group);
    }

    @PutMapping(value="/maintain/", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Group updateGroup(@RequestBody UpdateGroupReq group) throws Exception {
        return ugService.updateGroup(group);
    }

    @RequestMapping(value="/list", method=RequestMethod.GET)
    public List<Group> listGroups() throws Exception {
        return ugService.findAllGroups();
    }

    @RequestMapping(value="/read/id/{id}", method=RequestMethod.GET)
    public Group findGroup(@PathVariable("id") Long id) throws InvalidDataException {
        Group g = ugService.findGroupById(id);
        if( g == null )
            throw new InvalidRecordException("Group id " + id + " could not be found");
        return g;
    }
}
