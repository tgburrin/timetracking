package net.tgburrin.timekeeping.controllers;

import jakarta.servlet.http.HttpSession;
import net.tgburrin.timekeeping.authpermission.LoginReq;
import net.tgburrin.timekeeping.authpermission.LoginResp;
import net.tgburrin.timekeeping.services.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${apiPrefix}/${apiVersion}/session")
public class SessionController {

    @Autowired
    UserGroupService ugService;

    // Create a session and store an attribute
    /*
    @GetMapping("/create")
    public String createSession(HttpSession session) {
        // Set a session attribute (e.g., username)
        session.setAttribute("username", "JohnDoe");

        // Retrieve and return the session ID
        String sessionId = session.getId();
        return "Session created with ID: " + sessionId;
    }
    */

    // Retrieve session attribute
    @GetMapping("/get")
    public String getSession(HttpSession session) {
        // Get the session attribute (username)
        String username = (String) session.getAttribute("user_name");

        // If no session exists, return an error message
        if (username == null) {
            return "No session found!";
        }

        // Return session data to the client
        return "Session found with username: " + username;
    }

    // Invalidate the session
    @GetMapping("/invalidate")
    public String invalidateSession(HttpSession session) {
        // Invalidate the session
        session.invalidate();
        return "Session invalidated!";
    }

    @PostMapping(value="/password_hash", consumes = "application/json", produces = "application/json")
    public String generatePasswordHash(@RequestBody String passwordIn) {
        return UserGroupService.hashPassword(passwordIn);
    }

    @PostMapping(value="/login", consumes = "application/json", produces = "application/json")
    public LoginResp checkPassw0rd(HttpSession session, @RequestBody LoginReq req) {
        LoginResp rv = ugService.loginUser(req);
        session.setAttribute("user_id", rv.user.getUserId().toString());
        rv.sessionId = session.getId();
        return rv;
    }

}