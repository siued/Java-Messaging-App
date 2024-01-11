package message_client.view;

import message_client.controller.APIController;
import message_client.controller.MessageController;
import message_client.controller.UserController;
import message_client.observer_pattern.Listener;
import model.Message;

import javax.swing.*;
import javax.swing.border.Border;
import java.util.List;

public class MainPanel extends JPanel {

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
        uc.addListener(new Listener() {
            @Override
            public void update() {
                updateMessagePane();
            }
        });
        this.mc = mc;
        this.ac = ac;
        add(MainPanel);
        setVisible(true);

        createGUIComponents();
        updateMessagePane();
    }

    private void createGUIComponents() {
        FriendsTableModel model = new FriendsTableModel(uc);
        friendsTable.setModel(model);
        model.addTableModelListener(e -> updateMessagePane());
        ListSelectionModel cellSelectionModel = friendsTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        friendsTable.setColumnSelectionAllowed(true);
        friendsTable.setTableHeader(null);
        friendsPane.doLayout();

        cellSelectionModel.addListSelectionListener(e -> updateMessagePane());

        sendButton.addActionListener(e -> sendMessage());

        messageField.addActionListener(e -> sendMessage());
    }

    private void sendMessage() {
        String friendName = getSelectedFriend();
        if (friendName != null) {
            String message = messageField.getText();
            ac.sendMessage(UserController.getUsername(), friendName, message);
            messageField.setText("");
            updateMessagePane();
        }
    }

    private void updateMessagePane() {
        String friendName = getSelectedFriend();
        if (friendName == null) {
            return;
        }
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        List<Message> messages = mc.getMessagesTo(friendName);
        for (Message message : messages) {
            JLabel label = new JLabel(message.body());
            if (message.sender().equals(friendName)) {
                label.setHorizontalAlignment(SwingConstants.LEFT);
            } else {
                label.setHorizontalAlignment(SwingConstants.RIGHT);
            }
            Border border = BorderFactory.createLineBorder(message.sender().equals(friendName) ? java.awt.Color.BLUE : java.awt.Color.RED, 2);
            label.setBorder(border);
            messagePanel.add(label);
            messagePane.setViewportView(messagePanel);
        }
        messagePane.revalidate();
        messagePane.repaint();
    }

    private String getSelectedFriend() {
        int selectedColumn = friendsTable.getSelectedColumn();
        if (selectedColumn >= 0) {
            return (String) friendsTable.getValueAt(0, selectedColumn);
        }
        return null;
    }
}
