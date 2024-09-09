package net.tgburrin.timekeeping.controllers;

import net.tgburrin.timekeeping.services.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${apiPrefix}/${apiVersion}/group")
public class GroupController {
    @Autowired
    UserGroupService ugService;
}
