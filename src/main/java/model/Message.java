package model;

import java.util.Date;

public record Message(int id,
                      String body,
                      int sender_id,
                      int recipient_id,
                      Date created_at,
                      Date delivered_at) {

    public boolean isDelivered() {
        return delivered_at != null;
    }
}
