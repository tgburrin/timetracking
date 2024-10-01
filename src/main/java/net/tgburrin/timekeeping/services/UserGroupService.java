package net.tgburrin.timekeeping.services;

import net.tgburrin.timekeeping.authpermission.LoginReq;
import net.tgburrin.timekeeping.authpermission.LoginResp;
import net.tgburrin.timekeeping.exceptions.*;
import net.tgburrin.timekeeping.repositories.GroupRepository;
import net.tgburrin.timekeeping.repositories.UserRepository;
import net.tgburrin.timekeeping.usergroups.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserGroupService {
    @Autowired
    private GroupRepository groupRepo;

    @Autowired
    private UserRepository userRepo;

    private static final MessageDigest pwhasher;

    static {
        try {
            pwhasher = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toHexString(byte[] hash)
    {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 64)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

    public static String hashPassword(String pwIn) {
        return toHexString(pwhasher.digest(pwIn.getBytes(StandardCharsets.UTF_8)));
    }

    public Group findGroupById(Long id) {
        Optional<Group> fg = groupRepo.findById(id);
        return fg.orElse(null);
    }
    public List<Group> findAllGroups() {
        return new ArrayList<Group>(groupRepo.findAllActive());
    }

    public Group createGroup(CreateGroupReq ng) throws InvalidDataException {
        if ( groupRepo.testNameExists(ng.groupName) )
            throw new InvalidDataException("Group name '"+ng.groupName+"' already exists");

        Group g = new Group(ng);
        g.validate();

        try {
            return groupRepo.maintain(g);
        } catch (SQLDataException e) {
            throw new InternalErrorException(e.getMessage());
        }
    }

    public Group updateGroup(UpdateGroupReq gReq) throws InvalidDataException, NoRecordFoundException {
        // validate the request
        Optional<Group> ng = groupRepo.findById(gReq.groupId);
        if ( ng.isEmpty() )
            throw new NoRecordFoundException("Group Id "+gReq.groupId+" could not be found");
        Group g = ng.get();
        if ( !g.getName().equals(gReq.groupName) && groupRepo.testNameExists(gReq.groupName))
            throw new InvalidDataException("Group name '"+gReq.groupName+"' already exists");
        // update the fields and validate them

        g.setName(gReq.groupName);
        g.validate();
        try {
            return groupRepo.maintain(g);
        } catch (SQLDataException e) {
            throw new InternalErrorException(e.getMessage());
        }
    }

    public User createUser(CreateUserReq newUser) {
        if ( newUser.password == null || newUser.password.isEmpty() )
            throw new InvalidDataException("A password "+newUser.name+" must be provided");
        if ( userRepo.testNameExists(newUser.name) )
            throw new InvalidDataException("User with name "+newUser.name+" already exists");
        Group g = groupRepo.findByName(newUser.groupName).orElse(null);
        if ( g == null )
            throw new InvalidDataException("Group "+newUser.groupName+" could not be found");

        newUser.password = hashPassword(newUser.password);
        User u = new User(newUser, g);
        try {
            return userRepo.maintain(u);
        } catch (SQLDataException e) {
            throw new InternalErrorException(e.getMessage());
        }
    }

    public User findUserByName(String userName) {
        return userRepo.findByName(userName).orElse(null);
    }
    public List<User> listUsers() {
        return userRepo.listUsers();
    }

    public LoginResp loginUser(LoginReq req) {
        String passwd = toHexString(pwhasher.digest(req.password.getBytes(StandardCharsets.UTF_8)));
        Optional<User> foundUser = userRepo.findByName(req.username);
        if ( foundUser.isEmpty() )
            throw new InvalidRecordException("User "+req.username+" could not be located");

        if( !foundUser.get().readPasswordHash().equals(passwd) )
            throw new BadCredentialsException("User "+req.username+" could not be validated");

        LoginResp rv = new LoginResp();
        rv.user = foundUser.orElse(null);
        rv.loginSuccess = true;
        return rv;
    }
}
