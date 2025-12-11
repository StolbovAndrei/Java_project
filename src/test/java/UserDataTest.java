import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserDataTest {
    UserData userData = new UserData();
    @Test
    public void testCheckUser() {
        assertFalse(userData.checkUser(3132L));
    }

    @Test
    public void testAddUserAndCheck() {
        userData.addUser(1312L);
        assertTrue(userData.checkUser(1312L));
    }

    @Test
    public void testCheckUserState() {
        userData.addUser(1312L);
        assertEquals(UserState.WAITING_FOR_ACTIONS, userData.checkUserState(1312L));
    }

    @Test
    public void testCheckUserStateOnNull(){
        assertNull(userData.checkUserState(112L));
    }

    @Test
    public void testChangeUserState() {
        userData.addUser(1312L);
        userData.changeUserState(1312L, UserState.WAITING_FOR_ARTISTS);
        assertEquals(UserState.WAITING_FOR_ARTISTS, userData.checkUserState(1312L));
    }

}
