import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserDataTest {
    @Test
    public void testCheckUser() {
        assertFalse(UserData.checkUser(3132L));
    }

    @Test
    public void testAddUserAndCheck() {
        UserData.addUser(1312L);
        assertTrue(UserData.checkUser(1312L));
    }

    @Test
    public void testCheckUserState() {
        UserData.addUser(1312L);
        assertEquals(UserState.WAITING_FOR_ACTIONS, UserData.checkUserState(1312L));
    }

    @Test
    public void testCheckUserStateOnNull(){
        assertNull(UserData.checkUserState(112L));
    }

    @Test
    public void testChangeUserState() {
        UserData.addUser(1312L);
        UserData.changeUserState(1312L, UserState.WAITING_FOR_ARTISTS);
        assertEquals(UserState.WAITING_FOR_ARTISTS, UserData.checkUserState(1312L));
    }

}
