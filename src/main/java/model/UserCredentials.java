package model;

import lombok.Getter;
import lombok.Setter;

public class UserCredentials {
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String password;

    public UserCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
