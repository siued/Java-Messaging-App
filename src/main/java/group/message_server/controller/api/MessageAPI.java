package group.message_server.controller.api;

import group.message_server.controller.database.FriendsController;
import group.message_server.controller.database.MessageController;
import group.message_server.controller.database.UserController;
import model.Message;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageAPI {
    @PostMapping(value = "/send", consumes = "text/plain")
    public ResponseEntity<String> sendMessage(@RequestParam String sender,
                                              @RequestParam String recipient,
                                              @RequestBody String message) {
        var mc = new MessageController();
        var fc = new FriendsController();

        if (!fc.areFriends(sender, recipient)) {
            throw new UnsupportedOperationException("You must be friends to send a message");
        }
        mc.sendMessage(sender, recipient, message);
        return ResponseEntity.ok("Message sent");
    }

    @GetMapping("/unread")
    public ResponseEntity<List<Message>> getUnreadMessages(@RequestParam String username) {
        var mc = new MessageController();
        List<Message> messages = mc.getUnreadReceivedMessages(username);
        mc.setDelivered(messages);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/received")
    public ResponseEntity<List<Message>> getAllMessages(@RequestParam String username) {
        var mc = new MessageController();
        List<Message> messages = mc.getReceivedMessages(username);
        mc.setDelivered(messages);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/sent")
    public ResponseEntity<List<Message>> getAllSentMessages(@RequestParam String username) {
        var mc = new MessageController();
        List<Message> messages = mc.getSentMessages(username);
        return ResponseEntity.ok(messages);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
