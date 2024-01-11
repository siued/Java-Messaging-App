package message_client.observer_pattern;

import java.util.ArrayList;
import java.util.List;

public class Listenable {
    List<Listener> listeners = new ArrayList<>();

    public void addListener(Listener listener) {
        listeners.add(listener);
    };

    public void update() {
        for (Listener listener : listeners) {
            listener.update();
        }
    };
}
