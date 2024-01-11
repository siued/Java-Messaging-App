package message_client.observer_pattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Listenable class for the observer pattern.
 */
public class Listenable {
    List<Listener> listeners = new ArrayList<>();

    /**
     * Add a listener to the list of listeners.
     * @param listener Listener to add
     */
    public void addListener(Listener listener) {
        listeners.add(listener);
    };

    /**
     * Update all listeners.
     */
    public void update() {
        for (Listener listener : listeners) {
            listener.update();
        }
    };
}
