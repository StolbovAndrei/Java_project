import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MainMenuTest {
    Update update = mock(Update.class);
    Message message = mock(Message.class);
    MainMenu mainMenu = new MainMenu();

    @Test
    public void testStartCommand() {
        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn("/start");
        when(message.getChatId()).thenReturn(1231L);
        try (MockedStatic<UserData> userDataMockedStatic = Mockito.mockStatic(UserData.class)) {
            userDataMockedStatic.when(() -> UserData.checkUser(1231L)).thenReturn(false);

            String result = mainMenu.mainMenu(update);
            assertEquals(Messages.FIRST_MESSAGE, result);
            userDataMockedStatic.verify(() -> UserData.addUser(1231L));
        }
    }

    @Test
    public void testHelpCommand() {
        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn("/help");
        when(message.getChatId()).thenReturn(1231L);

        try (MockedStatic<UserData> userDataMockedStatic = Mockito.mockStatic(UserData.class)) {
            userDataMockedStatic.when(() -> UserData.checkUserState(1231L)).thenReturn(UserState.WAITING_FOR_ACTIONS);

            String result = mainMenu.mainMenu(update);
            assertEquals(Messages.HELP_MESSAGE, result);
            userDataMockedStatic.verify(() -> UserData.checkUserState(1231L));
        }
    }

    @Test
    public void testSearchCommand() {
        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn("/search");
        when(message.getChatId()).thenReturn(1231L);

        try (MockedStatic<UserData> userDataMockedStatic = Mockito.mockStatic(UserData.class)) {
            userDataMockedStatic.when(() -> UserData.checkUserState(1231L)).thenReturn(UserState.WAITING_FOR_ACTIONS);

            String result = mainMenu.mainMenu(update);
            assertEquals(Messages.SEARCH_MESSAGE, result);
            userDataMockedStatic.verify(() -> UserData.changeUserState(1231L, UserState.WAITING_FOR_ARTISTS));
        }
    }

    @Test
    public void testStateWaitingForArtists() {
        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn("Test-Song");
        when(message.getChatId()).thenReturn(1231L);
        try (MockedStatic<UserData> userDataMockedStatic = Mockito.mockStatic(UserData.class)) {
            userDataMockedStatic.when(() ->  UserData.checkUserState(1231L)).thenReturn(UserState.WAITING_FOR_ARTISTS);

            String result = mainMenu.mainMenu(update);
            assertTrue(result.contains("\nЕсли хотите выйти в меню, пропишите /back"));
        }
    }



}
