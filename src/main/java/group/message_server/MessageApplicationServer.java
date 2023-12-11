package group.message_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MessageApplicationServer {
    // TODO get SSL cert and enable https
    public static void main(String[] args) {
        SpringApplication.run(MessageApplicationServer.class, args);
    }
}
