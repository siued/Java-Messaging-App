package group.message_server.api;

import group.message_server.controller.database.DatabaseController;
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
    public ResponseEntity<String> registerUser(@RequestParam(name="username") String username,
                                          @RequestParam(name="password") String password) {
        UserController uc = DatabaseController.getInstance().getUserController();
        User user = new User(username, password);
        try {
            uc.addUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
