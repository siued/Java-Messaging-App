package group.message_server.controller.api;

import group.message_server.controller.database.FriendsController;
import group.message_server.controller.database.UserController;
import model.User;
import model.UserCredentials;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserAPI {

    @GetMapping("/test")
    public String userTestReply() {
        return "user/test says hello";
    }

    @PostMapping("/new")
    public ResponseEntity<String> registerUser(@RequestBody UserCredentials userCredentials) {
        UserController uc = new UserController();
        String username = userCredentials.getUsername();
        String password = userCredentials.getPassword();
        try {
            User user = new User(username, password);
            uc.addUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // TODO proper logging
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // TODO implement proper security with tokens
    @GetMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserCredentials userCredentials) {
        String username = userCredentials.getUsername();
        String password = userCredentials.getPassword();
        UserController uc = new UserController();
        try {
            uc.loginUser(username, password);
            return ResponseEntity.ok("Login successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/addfriend")
    public ResponseEntity<String> addFriend(@RequestParam String username, @RequestParam String friendName) {
        UserController uc = new UserController();
        FriendsController fc = new FriendsController();
        try {
            fc.addFriend(uc.getUserId(username), uc.getUserId(friendName));
            return ResponseEntity.ok("Friend request sent");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("friends")
    public ResponseEntity<?> getFriends(@RequestParam String username) {
        UserController uc = new UserController();
        FriendsController fc = new FriendsController();
        try {
            return ResponseEntity.ok(fc.getFriends(uc.getUserId(username)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
