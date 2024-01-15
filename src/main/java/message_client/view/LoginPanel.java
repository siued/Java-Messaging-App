package message_client.view;

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

    /**
     * Constructor for the LoginPanel.
     *
     * @param uc UserController to log in with.
     */
    public LoginPanel(UserController uc) {
        this.uc = uc;
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
