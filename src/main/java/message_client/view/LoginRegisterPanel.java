package message_client.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginRegisterPanel extends JPanel {
    private final JButton loginButton = new JButton("Login");
    private final JButton registerButton = new JButton("Register");
    public LoginRegisterPanel() {
        add(loginButton);
        add(registerButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.getInstance().showLoginScreen();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.getInstance().showRegisterScreen();
            }
        });
    }
}
