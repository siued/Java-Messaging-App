package message_client.view;

import javax.swing.*;

/**
 * Panel for the login/register screen.
 */
public class LoginRegisterPanel extends JPanel {
    /**
     * Constructor for the LoginRegisterPanel.
     */
    public LoginRegisterPanel() {
        JButton loginButton = new JButton("Login");
        add(loginButton);
        JButton registerButton = new JButton("Register");
        add(registerButton);

        loginButton.addActionListener(e -> MainFrame.getInstance().showLoginScreen());

        registerButton.addActionListener(e -> MainFrame.getInstance().showRegisterScreen());
    }
}
