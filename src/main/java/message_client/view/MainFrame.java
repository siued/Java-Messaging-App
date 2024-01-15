package message_client.view;

import message_client.controller.APIController;
import message_client.controller.MessageController;
import message_client.controller.MessageUpdateController;
import message_client.controller.UserController;
import message_client.observer_pattern.Listener;
import message_client.view.main_panel.MainPanel;

import javax.swing.*;

/**
 * The main frame of the application.
 */
public class MainFrame extends JFrame {
    private static MainFrame instance = null;
    private final UserController uc = new UserController();
    private final MessageController mc = new MessageController();
    private final APIController ac = new APIController();
    private final LoginPanel loginPanel = new LoginPanel(uc);
    private final RegisterPanel registerPanel = new RegisterPanel(uc);
    private final MainPanel mainPanel;
    private final LoginRegisterPanel loginRegisterPanel = new LoginRegisterPanel();

    /**
     * Private constructor to prevent instantiation.
     */
    private MainFrame() {
        initFrame();
        startBackgroundTasks();
        mainPanel = new MainPanel(uc, mc, ac, this);
    }

    /**
     * Starts background tasks.
     * Currently, starts a thread that checks for new messages
     */
    private void startBackgroundTasks() {
        Thread messageUpdateThread = new Thread(new MessageUpdateController(uc, mc));
        messageUpdateThread.start();
    }

    /**
     * Initializes the frame.
     */
    private void initFrame() {
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
    }

    /**
     * Shows the login/register screen.
     */
    public void showLoginRegisterScreen() {
        setTitle("Login/Register");
        setContentPane(loginRegisterPanel);
        pack();
        setVisible(true);
        setSize(500, 500);
    }

    /**
     * Logs out the user. goes back to login/register screen.
     */
    public void logOut() {
        showLoginRegisterScreen();
    }

    /**
     * Shows the login screen.
     */
    public void showLoginScreen() {
        setTitle("Login");
        setContentPane(loginPanel);
        pack();
        setVisible(true);
        setSize(500, 500);
    }

    /**
     * Shows the register screen.
     */
    public void showRegisterScreen() {
        setTitle("Register");
        setContentPane(registerPanel);
        pack();
        setVisible(true);
        setSize(500, 500);
    }

    /**
     * Shows the main screen.
     */
    private void showMainScreen() {
        setTitle("Main");
        setContentPane(mainPanel);
        pack();
        setVisible(true);
        setSize(500, 500);
    }

    /**
     * Gets the singleton instance of the main frame.
     *
     * @return the instance of the main frame
     */
    public static MainFrame getInstance() {
        if (instance == null) {
            instance = new MainFrame();
        }
        return instance;
    }
}
