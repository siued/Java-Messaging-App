package message_client.view;

import message_client.controller.APIController;
import message_client.controller.MessageController;
import message_client.controller.UserController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class RegisterPanel extends JPanel {
    private JTextField usernameTextField;
    private JPasswordField passwordField1;
    private JButton registerButton;
    private JPanel registerPanel;

    public RegisterPanel(UserController uc, MessageController mc, APIController ac) {
        this.add(registerPanel);
        registerButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    uc.registerUser(usernameTextField.getText(), Arrays.toString(passwordField1.getPassword()));
                    MainFrame.getInstance().resetShownScreen();
                    JOptionPane.showMessageDialog(null, "User registered successfully!");
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage());
                }
            }
        });
    }
}
