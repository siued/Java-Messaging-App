package message_client;

import message_client.controller.APIController;
import message_client.controller.MessageController;
import message_client.controller.UserController;
import message_client.observer_pattern.Listener;

import javax.swing.*;

public class mainFrame extends JFrame implements Listener {
    private final UserController uc = new UserController();
    private final MessageController mc = new MessageController();
    private final APIController ac = new APIController();
    private final LoginPanel loginPanel = new LoginPanel();
    private final MainPanel mainPanel = new MainPanel();

    public mainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        showLoginScreen();

        uc.addListener(this);
        mc.addListener(this);
    }

    private void showLoginScreen() {
        setTitle("Login");
        setContentPane(loginPanel);
        pack();
        setVisible(true);
        setSize(500, 500);
    }

    private void showMainScreen() {
        setTitle("Main");
        setContentPane(mainPanel);
        pack();
        setVisible(true);
        setSize(500, 500);
    }

    private void showConnectScreen() {
        setTitle("Connection error");
        setContentPane(connectPanel);
        pack();
        setVisible(true);
        setSize(500, 500);
    }

    @Override
    public void update() {
        // TODO handle new messages
        if (uc.isLoggedIn()) {
            showMainScreen();
        } else {
            showLoginScreen();
        }
    }

    public static void main(String[] args) {
        new mainFrame();
    }
}
