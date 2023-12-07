package group.message_server.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {

    @GetMapping("/test")
    public String testReply() {
        return "hello yes successful test yes hello we hear you";
    }
}
