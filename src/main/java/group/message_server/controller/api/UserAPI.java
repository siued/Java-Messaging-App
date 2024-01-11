package group.message_server.controller.api;

import group.message_server.controller.database.FriendsController;
import group.message_server.controller.database.MessageController;
import group.message_server.controller.database.UserController;
import model.User;
import model.UserCredentials;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserAPI {
    @PostMapping("/new")
    public ResponseEntity<String> registerUser(@RequestBody UserCredentials userCredentials) {
        UserController uc = new UserController();
        User user = new User(userCredentials.username(), userCredentials.password());
        uc.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created");
    }

    // TODO implement proper security with tokens
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserCredentials userCredentials) {
        String username = userCredentials.username();
        String password = userCredentials.password();
        UserController uc = new UserController();
        uc.loginUser(username, password);
        return ResponseEntity.ok("Login successful");
    }

    @PutMapping("/addfriend")
    public ResponseEntity<String> addFriend(@RequestParam String username, @RequestParam String friendName) {
        FriendsController fc = new FriendsController();
        fc.addFriend(username, friendName);
        return ResponseEntity.ok("Friend request sent");
    }

    @GetMapping("/friends")
    public ResponseEntity<List<String>> getFriends(@RequestParam String username) {
        FriendsController fc = new FriendsController();
        return ResponseEntity.ok(fc.getFriends(username));
    }

    @GetMapping("/pending-requests")
    public ResponseEntity<List<String>> getPendingFriendRequests(@RequestParam String username) {
        FriendsController fc = new FriendsController();
        List<String> friendRecords = fc.getPendingRequests(username);
        return ResponseEntity.ok(friendRecords);
    }

    @GetMapping("/reject-request")
    public ResponseEntity<String> rejectFriendRequest(@RequestParam String username, @RequestParam String friendName) {
        FriendsController fc = new FriendsController();
        fc.rejectFriendRequest(username, friendName);
        return ResponseEntity.ok("Friend request rejected");
    }

    @GetMapping("/remove-friend")
    public ResponseEntity<String> removeFriend(@RequestParam String username, @RequestParam String friendName) {
        FriendsController fc = new FriendsController();
        fc.removeFriend(username, friendName);
        return ResponseEntity.ok("Friend removed");
    }

    @GetMapping("/has-unread")
    public ResponseEntity<Boolean> hasUnreadMessages(@RequestParam String username) {
        MessageController mc = new MessageController();
        return ResponseEntity.ok(!mc.getUnreadReceivedMessages(username).isEmpty());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
