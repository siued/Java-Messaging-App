package group.message_server.controller.api;

import group.message_server.controller.database.DatabaseController;
import group.message_server.controller.database.FriendsController;
import group.message_server.controller.database.UserController;
import model.User;
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
    public ResponseEntity<String> registerUser(@RequestParam String username, @RequestParam String password) {
        UserController uc = new UserController();
        User user = new User(username, password);
        try {
            uc.addUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/addFriend")
    public ResponseEntity<String> addFriend(@RequestParam String username, @RequestParam String friendname) {
        UserController uc = new UserController();
        FriendsController fc = new FriendsController();
        try {
            fc.addFriend(uc.getUserId(username), uc.getUserId(friendname));
            return ResponseEntity.ok("Friend added");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
