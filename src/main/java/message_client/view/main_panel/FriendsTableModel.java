package message_client.view.main_panel;

import message_client.controller.UserController;
import message_client.observer_pattern.Listener;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * TAble model for the friends table in the MainPanel.
 */
public class FriendsTableModel extends AbstractTableModel {
    private List<String> friends;
    private final UserController uc;

    /**
     * Constructor for the FriendsTableModel.
     * @param uc UserController to get the friends from.
     */
    public FriendsTableModel(UserController uc) {
        this.uc = uc;
        this.friends = uc.getFriends();

        uc.addListener(new Listener() {
            @Override
            public void update() {
                friends = uc.getFriends();
                fireTableStructureChanged();
            }
        });
    }

    /**
     * Get the number of columns in the table.
     * @return Number of columns in the table. Always 1.
     */
    @Override
    public int getColumnCount() {
        return 1;
    }

    /**
     * Get the number of rows in the table.
     * @return Number of rows in the table.
     */
    @Override
    public int getRowCount() {
        if (friends == null) return 0;
        return friends.size();
    }

    /**
     * Get the value at a specific row and column.
     * @param rowIndex        the row whose value is to be queried.
     * @param columnIndex     the column whose value is to be queried.
     * @return the value in that cell (a String object).
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return friends.get(rowIndex);
    }
}
