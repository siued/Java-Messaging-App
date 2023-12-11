import group.message_server.controller.database.DatabaseController;
import group.message_server.controller.database.UserController;
import model.User;

import java.util.Date;

public class Test {

    public static void main(String[] args) {
        DatabaseController dbController = DatabaseController.getInstance();

        String username = "test2";

        User user = new User(username, "test", new Date(), new Date());

        UserController uc = new UserController();

        uc.addUser(user);

        System.out.println(uc.getUsers());

        uc.updateField(username, "lastLoginAt", new Date(10000));

        System.out.println(uc.getUsers());

        uc.deleteUser(username);

        System.out.println(uc.getUsers());
    }
}
