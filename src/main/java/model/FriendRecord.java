package model;

public record FriendRecord(int user1_id,
                           int user2_id) {

    public boolean contains(int user_id) {
        return user1_id == user_id || user2_id == user_id;
    }

    public int other(int user_id) {
        if (user1_id == user_id) {
            return user2_id;
        } else if (user2_id == user_id) {
            return user1_id;
        } else {
            throw new IllegalArgumentException("user_id " + user_id + " is not in this Friend_record");
        }
    }
}
