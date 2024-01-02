package group.message_server.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Ping {
    /**
     * Returns a ResponseEntity with a body of "Success" and a status of 200.
     * Serves to check whether the server is up and running.
     *
     * @return a ResponseEntity with a body of "Success" and a status of 200.
     */
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Success");
    }
}
