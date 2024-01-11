package message_client.controller;

public class MessageUpdateController implements Runnable {
    private final UserController uc;
    private final MessageController mc;
    private final int SLEEP_TIME = 5000;

    public MessageUpdateController(UserController uc, MessageController mc) {
        this.uc = uc;
        this.mc = mc;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("MessageUpdateController running");
            if (mc.hasNewMessages()) {
                mc.update();
            }
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
