package net.tgburrin.timekeeping.controllers;

import jakarta.servlet.http.HttpSession;
import net.tgburrin.timekeeping.exceptions.InvalidDataException;
import net.tgburrin.timekeeping.exceptions.InvalidRecordException;
import net.tgburrin.timekeeping.usergroups.User;
import net.tgburrin.timekeeping.usergroups.LoginSessionResp;
import net.tgburrin.timekeeping.services.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${apiPrefix}/${apiVersion}/user")
public class UserController {
    @Autowired
    UserGroupService ugService;

    @RequestMapping(value="/login_as/by_name/{name}", method= RequestMethod.GET)
    public LoginSessionResp loginAsName(HttpSession s, @PathVariable("name") String name) throws InvalidDataException {
        User u = ugService.findUserByName(name);
        if ( u == null )
            throw new InvalidRecordException("Username "+name+" could not be found");

        s.setAttribute("user_id", u.getUserId().toString());
        s.setAttribute("user_name", u.getName());

        return new LoginSessionResp(s.getId(), u.getUserId(), u.getName() );
    }
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<User> listUsers(HttpSession s) {
        return ugService.listUsers();
    }
}
