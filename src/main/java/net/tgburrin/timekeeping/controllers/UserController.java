package net.tgburrin.timekeeping.controllers;

import jakarta.servlet.http.HttpSession;
import net.tgburrin.timekeeping.InvalidDataException;
import net.tgburrin.timekeeping.InvalidRecordException;
import net.tgburrin.timekeeping.UserGroups.Group;
import net.tgburrin.timekeeping.UserGroups.User;
import net.tgburrin.timekeeping.UserGroups.LoginSessionResp;
import net.tgburrin.timekeeping.services.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${apiPrefix}/${apiVersion}/user")
public class UserController {
    @Autowired
    UserGroupService ugService;

    @RequestMapping(value="/login_as/name/{name}", method= RequestMethod.GET)
    public LoginSessionResp loginAsName(HttpSession s, @PathVariable("name") String name) throws InvalidDataException {
        User u = ugService.findUserByName(name);
        if ( u == null )
            throw new InvalidRecordException("Username "+name+" could not be found");

        s.setAttribute("user_id", u.getId().toString());
        s.setAttribute("user_name", u.getName());

        return new LoginSessionResp(s.getId(), u.getId(), u.getName() );
    }
}
