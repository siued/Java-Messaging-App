package message_client.view;

import message_client.controller.APIController;
import message_client.controller.MessageController;
import message_client.controller.UserController;

import javax.swing.*;
import java.util.Arrays;

/**
 * Panel for the login screen.
 */
public class LoginPanel extends JPanel {
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton loginButton;
    private JPanel LoginPanel;
    private final UserController uc;
    private final MessageController mc;
    private final APIController ac;

    /**
     * Constructor for the LoginPanel.
     * @param uc UserController to log in with.
     * @param mc MessageController to log in with.
     * @param ac APIController to log in with.
     */
    public LoginPanel(UserController uc, MessageController mc, APIController ac) {
        this.uc = uc;
        this.mc = mc;
        this.ac = ac;
        add(LoginPanel);
        loginButton.addActionListener(e -> loginUser());

        passwordField1.addActionListener(e -> loginUser());
    }

    /**
     * Attempt to log in the user.
     */
    private void loginUser() {
        uc.loginUser(textField1.getText(), Arrays.toString(passwordField1.getPassword()));
    }
}
