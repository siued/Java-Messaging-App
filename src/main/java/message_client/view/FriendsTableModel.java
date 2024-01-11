package message_client.view;

import message_client.controller.UserController;
import message_client.observer_pattern.Listener;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class FriendsTableModel extends AbstractTableModel {
    private List<String> friends;
    private final UserController uc;

    public FriendsTableModel(UserController uc) {
        this.uc = uc;
        uc.addListener(new Listener() {
            @Override
            public void update() {
                friends = uc.getFriends();
                fireTableStructureChanged();
            }
        });
        this.friends = uc.getFriends();
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return friends.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return friends.get(columnIndex);
    }
}
