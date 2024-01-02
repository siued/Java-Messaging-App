package group.message_server.controller.database;

import model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTests {
    private static final String username = "TEST_USER";
    private static final String friendName = "TEST_USER2";
    private static final User user1 = new User(username, "password");
    private static final User user2 = new User(friendName, "password");
    private static final UserController uc = new UserController();

    @BeforeAll
    public static void setUp() {
        uc.deleteUser(username);
        uc.deleteUser(friendName);
        assertFalse(uc.userExists(username));
        assertFalse(uc.userExists(friendName));
    }

    @Test
    public void testAddUser() {
        uc.addUser(user1);
        assertTrue(uc.userExists(username));
        uc.deleteUser(username);
    }

    @Test
    public void testLoginUser() {
        uc.addUser(user1);
        assertTrue(uc.loginUser(username, "password"));
        uc.deleteUser(username);
    }

    @Test
    public void testDeleteUser() {
        uc.addUser(user1);
        assertTrue(uc.userExists(username));
        uc.deleteUser(username);
        assertFalse(uc.userExists(username));
    }

    @Test
    public void testUserExists() {
        assertFalse(uc.userExists(username));
        uc.addUser(user1);
        assertTrue(uc.userExists(username));
        uc.deleteUser(username);
    }

    @Test
    public void testGetUser() {
        uc.addUser(user1);
        User user1WithId = uc.getUser(username);
        assertEquals(username, user1WithId.username());
        assertEquals(user1.passwordHash(), user1WithId.passwordHash());
        assertEquals(user1.lastLoginAt(), user1WithId.lastLoginAt());
        assertEquals(user1.createdAt(), user1WithId.createdAt());
        uc.deleteUser(username);
    }

    @Test
    public void testGetUsername() {
        uc.addUser(user1);
        User user1WithId = uc.getUser(username);
        assertEquals(username, uc.getUsername(user1WithId.id()));
        uc.deleteUser(username);
    }

    @Test
    public void testGetId() {
        uc.addUser(user1);
        User user1WithId = uc.getUser(username);
        assertEquals(user1WithId.id(), uc.getUserId(username));
        uc.deleteUser(username);
    }

    @Test
    public void testGetUsers() {
        uc.addUser(user1);
        uc.addUser(user2);
        List<User> users = uc.getUsers();
        assertTrue(users.size() >= 2);
        assertTrue(users.stream().map(User::username).toList().contains(username));
        assertTrue(users.stream().map(User::username).toList().contains(friendName));
        uc.deleteUser(username);
        uc.deleteUser(friendName);
    }

    @AfterAll
    public static void tearDown() {
        uc.deleteUser(username);
        uc.deleteUser(friendName);
        assertFalse(uc.userExists(username));
        assertFalse(uc.userExists(friendName));
    }
}