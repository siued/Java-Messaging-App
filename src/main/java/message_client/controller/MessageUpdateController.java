package message_client.controller;

/**
 * This class is responsible for updating the messages in the background.
 * It is run as a separate thread. It does this using busy-waiting.
 */
public class MessageUpdateController implements Runnable {
    private final UserController uc;
    private final MessageController mc;
    // Time between updates in milliseconds
    private final int SLEEP_TIME = 5000;

    /**
     * Constructor for MessageUpdateController.
     *
     * @param uc the UserController
     * @param mc the MessageController
     */
    public MessageUpdateController(UserController uc, MessageController mc) {
        this.uc = uc;
        this.mc = mc;
    }

    /**
     * This method is called when the thread is started.
     * It checks if there are any new messages.
     * If there are new messages, it tells the
     * MessageController to update its listeners.
     */
    @Override
    public void run() {
        while (true) {
            if (uc.isLoggedIn() && mc.hasNewMessages()) {
                mc.update();
            }
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                // exit thread
                return;
            }
        }
    }
}
