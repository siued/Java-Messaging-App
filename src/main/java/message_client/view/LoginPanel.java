package message_client.view;

import message_client.controller.APIController;
import message_client.controller.MessageController;
import message_client.controller.UserController;

import javax.swing.*;
import java.util.Arrays;

public class LoginPanel extends JPanel {
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton loginButton;
    private JPanel LoginPanel;
    private final UserController uc;
    private final MessageController mc;
    private final APIController ac;

    public LoginPanel(UserController uc, MessageController mc, APIController ac) {
        this.uc = uc;
        this.mc = mc;
        this.ac = ac;
        add(LoginPanel);
        loginButton.addActionListener(e -> loginUser());

        passwordField1.addActionListener(e -> loginUser());
    }

    private void loginUser() {
        uc.loginUser(textField1.getText(), Arrays.toString(passwordField1.getPassword()));
    }
}
