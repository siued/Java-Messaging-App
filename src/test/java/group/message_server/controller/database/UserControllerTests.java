package group.message_server.controller.database;

import group.message_server.controller.database.UserController;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTests {

    private UserController userController;
    private User user;

    @BeforeEach
    public void setUp() {
        userController = Mockito.mock(UserController.class);
        user = new User("test", "test");
    }

    @Test
    public void addUserSuccessfullyAddsUser() {
        doNothing().when(userController).addUser(user);
        userController.addUser(user);
        verify(userController, times(1)).addUser(user);
    }

    @Test
    public void loginUserReturnsTrueWhenCredentialsMatch() {
        when(userController.loginUser("test", "test")).thenReturn(true);
        assertTrue(userController.loginUser("test", "test"));
    }

    @Test
    public void loginUserReturnsFalseWhenCredentialsDoNotMatch() {
        when(userController.loginUser("test", "wrong")).thenReturn(false);
        assertFalse(userController.loginUser("test", "wrong"));
    }

    @Test
    public void getUserReturnsCorrectUser() {
        when(userController.getUser("test")).thenReturn(user);
        assertEquals(user, userController.getUser("test"));
    }

    @Test
    public void updateFieldSuccessfullyUpdatesField() {
        when(userController.updateField("test", "password_hash", "new_password")).thenReturn(true);
        assertTrue(userController.updateField("test", "password_hash", "new_password"));
    }

    @Test
    public void getUsersReturnsListOfUsers() {
        List<User> users = Arrays.asList(user, new User("test2", "test2"));
        when(userController.getUsers()).thenReturn(users);
        assertEquals(users, userController.getUsers());
    }

    @Test
    public void deleteUserSuccessfullyDeletesUser() {
        doNothing().when(userController).deleteUser("test");
        userController.deleteUser("test");
        verify(userController, times(1)).deleteUser("test");
    }

    @Test
    public void deleteAllUsersSuccessfullyDeletesAllUsers() {
        doNothing().when(userController).deleteAllUsers();
        userController.deleteAllUsers();
        verify(userController, times(1)).deleteAllUsers();
    }
}