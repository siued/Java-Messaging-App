package message_client;

import message_client.controller.APIController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectPanel extends JPanel {
    private JButton tryAgainButton;
    private JLabel infoLabel;
    private JPanel ConnectPanel;
    private APIController ac;

    public ConnectPanel(APIController ac) {
        this.ac = ac;
        add(ConnectPanel);
        tryAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ac.checkConnection();
            }
        });
    }
}
