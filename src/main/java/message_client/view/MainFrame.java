package message_client.view;

import message_client.controller.APIController;
import message_client.controller.MessageController;
import message_client.controller.UserController;
import message_client.observer_pattern.Listener;

import javax.swing.*;

public class MainFrame extends JFrame {
    private static MainFrame instance = null;
    private final UserController uc = new UserController();
    private final MessageController mc = new MessageController();
    private final APIController ac = new APIController();
    private final LoginPanel loginPanel = new LoginPanel(uc, mc, ac);
    private final RegisterPanel registerPanel = new RegisterPanel(uc, mc, ac);
    private final MainPanel mainPanel = new MainPanel(uc, mc, ac);
    private final ConnectPanel connectPanel = new ConnectPanel(ac);
    private final LoginRegisterPanel loginRegisterPanel = new LoginRegisterPanel();

    private MainFrame() {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        showLoginRegisterScreen();

        uc.addListener(new Listener() {
            @Override
            public void update() {
                if (uc.isLoggedIn()) {
                    showMainScreen();
                } else {
                    showLoginScreen();
                }
            }
        });
        mc.addListener(new Listener() {
            @Override
            public void update() {
                // TODO handle incoming messages
            }
        });
    }

    public void showLoginRegisterScreen() {
        setTitle("Login/Register");
        setContentPane(loginRegisterPanel);
        pack();
        setVisible(true);
        setSize(500, 500);
    }

    public void resetShownScreen() {
        showLoginRegisterScreen();
    }

    public void showLoginScreen() {
        setTitle("Login");
        setContentPane(loginPanel);
        pack();
        setVisible(true);
        setSize(500, 500);
    }

    public void showRegisterScreen() {
        setTitle("Register");
        setContentPane(registerPanel);
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

    public static MainFrame getInstance() {
        if (instance == null) {
            instance = new MainFrame();
        }
        return instance;
    }

    public static void main(String[] args) {
        MainFrame.getInstance();
    }
}
