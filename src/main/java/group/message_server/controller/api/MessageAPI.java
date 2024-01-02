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
        var uc = new UserController();
        var fc = new FriendsController();

        try {
            ObjectId senderId = uc.getUserId(sender);
            ObjectId recipientId = uc.getUserId(recipient);
            if (!fc.areFriends(senderId, recipientId)) {
                return ResponseEntity.badRequest().body("You must be friends to send a message");
            }
            mc.sendMessage(sender, recipient, message);
            return ResponseEntity.ok("Message sent");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/unread")
    public ResponseEntity<?> getUnreadMessages(@RequestParam String username) {
        var mc = new MessageController();

        try {
            List<Message> messages = mc.getUnreadReceivedMessages(username);
            mc.setDelivered(messages);
            return ResponseEntity.ok(messages);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/received")
    public ResponseEntity<?> getAllMessages(@RequestParam String username) {
        var mc = new MessageController();

        try {
            List<Message> messages = mc.getReceivedMessages(username);
            mc.setDelivered(messages);
            return ResponseEntity.ok(messages);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/sent")
    public ResponseEntity<?> getAllSentMessages(@RequestParam String username) {
        var mc = new MessageController();

        try {
            List<Message> messages = mc.getSentMessages(username);
            return ResponseEntity.ok(messages);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
