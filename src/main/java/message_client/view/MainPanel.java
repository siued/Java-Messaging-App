package message_client.view;

import message_client.controller.APIController;
import message_client.controller.MessageController;
import message_client.controller.UserController;
import message_client.observer_pattern.Listener;
import model.Message;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainPanel extends JPanel implements Listener {

    private JPanel MainPanel;
    private JTable friendsTable;
    private JScrollPane messagePane;
    private JScrollPane friendsPane;
    private JTextField messageField;
    private JButton sendButton;
    private final UserController uc;
    private final MessageController mc;
    private final APIController ac;

    public MainPanel(UserController uc, MessageController mc, APIController ac) {
        this.uc = uc;
        uc.addListener(this);
        this.mc = mc;
        this.ac = ac;
        add(MainPanel);

        createGUIComponents();
        updateMessagePane();
    }

    private void createGUIComponents() {
        friendsTable.setModel(new FriendsTableModel(uc));
        ListSelectionModel cellSelectionModel = friendsTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        friendsTable.setColumnSelectionAllowed(true);
        friendsTable.setTableHeader(null);

        cellSelectionModel.addListSelectionListener(e -> {
            updateMessagePane();
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String friendName = getSelectedFriend();
                if (friendName != null) {
                    String message = messageField.getText();
                    ac.sendMessage(UserController.getUsername(), friendName, message);
                    messageField.setText("");
                    updateMessagePane();
                }
            }
        });
    }

    private void updateMessagePane() {
        String friendName = getSelectedFriend();
        if (friendName == null) {
            return;
        }
        List<Message> messages = mc.getMessagesTo(friendName);
        System.out.println("Messages: " + messages);
        messagePane.removeAll();
        for (Message message : messages) {
            JLabel label = new JLabel(message.body());
            if (message.sender().equals(friendName)) {
                label.setHorizontalAlignment(SwingConstants.LEFT);
            } else {
                label.setHorizontalAlignment(SwingConstants.RIGHT);
            }
            messagePane.add(label);
        }
        messagePane.revalidate();
        messagePane.repaint();
        MainFrame.getInstance().pack();
    }

    private String getSelectedFriend() {
        int selectedColumn = friendsTable.getSelectedColumn();
        if (selectedColumn >= 0) {
            return (String) friendsTable.getValueAt(0, selectedColumn);
        }
        return null;
    }

    @Override
    public void update() {
        friendsTable.revalidate();
        friendsTable.repaint();
        updateMessagePane();
    }
}
