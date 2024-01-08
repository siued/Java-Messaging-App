package message_client.observer_pattern;

public interface Listenable {
    public abstract void addListener(Listener listener);
    public abstract void update();
}
