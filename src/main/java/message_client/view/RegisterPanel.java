package message_client.view;

import message_client.controller.UserController;

import javax.swing.*;
import java.util.Arrays;

/**
 * Panel for the register screen.
 */
public class RegisterPanel extends JPanel {
    private JTextField usernameTextField;
    private JPasswordField passwordField1;
    private JButton registerButton;
    private JPanel registerPanel;

    /**
     * Constructor for the RegisterPanel.
     *
     * @param uc UserController to register with.
     */
    public RegisterPanel(UserController uc) {
        this.add(registerPanel);
        registerButton.addActionListener(e -> {
            try {
                uc.registerUser(usernameTextField.getText(), Arrays.toString(passwordField1.getPassword()));
                MainFrame.getInstance().showLoginRegisterScreen();
                JOptionPane.showMessageDialog(null, "User registered successfully!");
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(null, exception.getMessage());
            }
        });
    }
}
