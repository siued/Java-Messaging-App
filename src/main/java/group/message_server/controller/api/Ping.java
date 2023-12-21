package group.message_server.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Ping {
    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok("Success");
    }
}
