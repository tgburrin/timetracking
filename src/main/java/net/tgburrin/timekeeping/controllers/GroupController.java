package net.tgburrin.timekeeping.controllers;

import net.tgburrin.timekeeping.UserGroups.Group;
import net.tgburrin.timekeeping.services.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/${apiPrefix}/${apiVersion}/group")
public class GroupController {
    @Autowired
    UserGroupService ugService;

    @RequestMapping(value="/list", method= RequestMethod.GET)
    public List<Group> listGroups() throws Exception {
        return ugService.findAllGroups();
    }
}
