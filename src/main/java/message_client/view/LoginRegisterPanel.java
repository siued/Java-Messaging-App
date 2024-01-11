package message_client.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginRegisterPanel extends JPanel {
    public LoginRegisterPanel() {
        JButton loginButton = new JButton("Login");
        add(loginButton);
        JButton registerButton = new JButton("Register");
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
