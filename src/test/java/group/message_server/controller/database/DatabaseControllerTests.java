package group.message_server.controller.database;

import org.junit.jupiter.api.Test;

public class DatabaseControllerTests {
    @Test
    public void testGetInstance() {
        DatabaseController instance = DatabaseController.getInstance();
        assert instance != null;
    }

    @Test
    public void testSingleton() {
        DatabaseController instance1 = DatabaseController.getInstance();
        DatabaseController instance2 = DatabaseController.getInstance();
        assert instance1 == instance2;
    }
}
