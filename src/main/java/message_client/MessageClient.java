package message_client;

import message_client.controller.APIController;
import message_client.controller.MessageController;
import message_client.controller.UserController;
import org.json.JSONException;

import java.util.Scanner;

public class MessageClient {
    public static void main(String[] args) {
        var uc = new UserController();
        var mc = new MessageController();
        var ac = new APIController();

        if (ac.checkConnection()) {
            System.out.println("Connected to server");
        } else {
            System.out.println("Could not connect to server");
            System.exit(1);
        }

        // TODO Command pattern
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            switch (input) {
                case "login":
                    System.out.println("Enter username:");
                    String username = scanner.nextLine();
                    System.out.println("Enter password:");
                    String password = scanner.nextLine();
                    if (uc.loginUser(username, password))
                        System.out.println("Login successful");
                    else
                        System.out.println("Login failed");
                    break;
                case "register":
                    System.out.println("Enter username:");
                    username = scanner.nextLine();
                    System.out.println("Enter password:");
                    password = scanner.nextLine();
                    try {
                        uc.registerUser(username, password);
                    } catch (JSONException e) {
                        System.out.println("Error registering user");
                        System.out.println(e.getMessage());
                    }
                    break;
                case "add friend":
                    System.out.println("Enter friend name:");
                    String friendName = scanner.nextLine();
                    uc.addFriend(friendName);
                    System.out.println("Friend request sent");
                    break;
                case "requests":
                    System.out.println(uc.getFriendRequests());
                    break;
                case "accept":
                    System.out.println("Enter friend name:");
                    friendName = scanner.nextLine();
                    uc.acceptFriend(friendName);
                    break;
                case "friends":
                    System.out.println(uc.getFriends());
                    break;
                case "send":
                    System.out.println("Enter recipient name:");
                    String recipientId = scanner.nextLine();
                    System.out.println("Enter message body:");
                    String body = scanner.nextLine();
                    mc.sendMessage(recipientId, body);
                    break;
                case "unread":
                    System.out.println(mc.getUnreadMessages());
                    break;
                case "received":
                    System.out.println(mc.getReceivedMessages());
                    break;
                case "sent":
                    System.out.println(mc.getSentMessages());
                    break;
                case "exit":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid command");
                    break;
            }
        }
    }
}