package message_client;

import message_client.controller.APIController;
import message_client.controller.MessageController;
import message_client.controller.UserController;

import javax.swing.*;

public class MainPanel extends JPanel {

    private JPanel MainPanel;
    private JTable friendsTable;
    private JScrollPane MessagePane;
    private JScrollPane FriendsPane;

    public MainPanel(UserController uc, MessageController mc, APIController ac) {
    }
}
