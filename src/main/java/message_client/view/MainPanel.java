package message_client.view;

import message_client.controller.APIController;
import message_client.controller.MessageController;
import message_client.controller.UserController;
import message_client.observer_pattern.Listener;
import model.Message;

import javax.swing.*;
import javax.swing.border.Border;
import java.util.List;

/**
 * Panel for the main screen.
 */
public class MainPanel extends JPanel {

    private JPanel MainPanel;
    private JTable friendsTable;
    private JScrollPane messagePane;
    private JScrollPane friendsPane;
    private JTextField messageField;
    private JButton sendButton;
    private JPanel messagePanel;
    private final UserController uc;
    private final MessageController mc;
    private final APIController ac;

    /**
     * Constructor for the MainPanel.
     * @param uc UserController to get the friends from.
     * @param mc MessageController to get the messages from.
     * @param ac APIController to send messages with.
     */
    public MainPanel(UserController uc, MessageController mc, APIController ac) {
        this.uc = uc;
        uc.addListener(new Listener() {
            @Override
            public void update() {
                updateMessagePane();
            }
        });
        mc.addListener(new Listener() {
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

    /**
     * Create GUI components.
     */
    private void createGUIComponents() {
        FriendsTableModel model = new FriendsTableModel(uc);
        friendsTable.setModel(model);
        model.addTableModelListener(e -> updateMessagePane());
        friendsTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        friendsTable.getSelectionModel().addListSelectionListener(e -> updateMessagePane());
        friendsTable.setColumnSelectionAllowed(true);
        friendsTable.setTableHeader(null);

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePane.setViewportView(messagePanel);

        sendButton.addActionListener(e -> sendMessage());

        messageField.addActionListener(e -> sendMessage());
    }

    /**
     * Send a message to the selected friend.
     */
    private void sendMessage() {
        String friendName = getSelectedFriend();
        if (friendName != null) {
            String message = messageField.getText();
            ac.sendMessage(UserController.getUsername(), friendName, message);
            messageField.setText("");
            updateMessagePane();
        }
    }

    /**
     * Update the message pane with new messages.
     */
    private void updateMessagePane() {
        String friendName = getSelectedFriend();
        if (friendName == null) {
            return;
        }
        messagePanel.removeAll();
        List<Message> messages = mc.getMessagesTo(friendName);
        for (Message message : messages) {
            JLabel label = new JLabel(message.body());
            // TODO fix horizontal alignment
            if (message.sender().equals(friendName)) {
                label.setHorizontalAlignment(SwingConstants.LEFT);
            } else {
                label.setHorizontalAlignment(SwingConstants.RIGHT);
            }
            Border border = BorderFactory.createLineBorder(message.sender().equals(friendName) ? java.awt.Color.BLUE : java.awt.Color.RED, 2);
            label.setBorder(border);
            messagePanel.add(label);
        }
        messagePanel.revalidate();
    }

    /**
     * Get the selected friend from the friends table.
     * @return Selected friend.
     */
    private String getSelectedFriend() {
        int selectedRow = friendsTable.getSelectedRow();
        if (selectedRow >= 0) {
            return (String) friendsTable.getValueAt(selectedRow, 0);
        }
        return null;
    }
}
