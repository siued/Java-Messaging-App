package message_client;

import message_client.controller.APIController;
import message_client.controller.MessageController;
import message_client.controller.UserController;

import java.util.Scanner;

/**
 * Main class for the CLI message client.
 */
public class MessageClientCLI {
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

        // This could be command pattern, but it's not worth the effort for a CLI
        // Trying to keep this simple
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            String username, password, friendName, recipient, body;
            switch (input) {
                case "login":
                    System.out.println("Enter username:");
                    username = scanner.nextLine();
                    System.out.println("Enter password:");
                    password = scanner.nextLine();
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
                    uc.registerUser(username, password);
                    System.out.println("User created");
                    break;
                case "add friend":
                    System.out.println("Enter friend name:");
                    friendName = scanner.nextLine();
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
                    System.out.println("Friend request accepted");
                    break;
                case "friends":
                    System.out.println(uc.getFriends());
                    break;
                case "send":
                    System.out.println("Enter recipient name:");
                    recipient = scanner.nextLine();
                    System.out.println("Enter message body:");
                    body = scanner.nextLine();
                    mc.sendMessage(recipient, body);
                    System.out.println("Message sent");
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
                case "help":
                    System.out.println("login - login to an existing account");
                    System.out.println("register - register a new account");
                    System.out.println("add friend - send a friend request");
                    System.out.println("requests - view pending friend requests");
                    System.out.println("accept - accept a friend request");
                    System.out.println("friends - view friends");
                    System.out.println("send - send a message");
                    System.out.println("unread - view unread messages");
                    System.out.println("received - view received messages");
                    System.out.println("sent - view sent messages");
                    System.out.println("exit - exit the program");
                    break;
                default:
                    System.out.println("Invalid command");
            }
        }
    }
}
