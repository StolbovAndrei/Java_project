import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MainMenuTest {

    @Mock
    private UserData userData;

    @Mock
    private Update update;

    @Mock
    private Message tgMessage;

    private MainMenu mainMenu;

    @BeforeEach
    void setUp() {
        mainMenu = new MainMenu();
    }

    @Test
    void helpCommandTest() {
        long chatId = 123L;

        when(update.getMessage()).thenReturn(tgMessage);
        when(tgMessage.getChatId()).thenReturn(chatId);
        when(tgMessage.getText()).thenReturn("/help");

        when(userData.checkUser(chatId)).thenReturn(true);
        when(userData.checkUserState(chatId)).thenReturn(UserState.WAITING_FOR_ACTIONS);

        SendMessage out = mainMenu.mainMenu(update, userData);

        assertEquals(String.valueOf(chatId), out.getChatId());
        assertEquals(ParseMode.HTML, out.getParseMode());
        assertEquals(Messages.HELP_MESSAGE, out.getText());
        verify(userData, never()).addUser(anyLong());
    }

    @Test
    void searchCommandChangeUserStateTest() {
        long chatId = 123L;

        when(update.getMessage()).thenReturn(tgMessage);
        when(tgMessage.getChatId()).thenReturn(chatId);
        when(tgMessage.getText()).thenReturn("/search");

        when(userData.checkUser(chatId)).thenReturn(true);
        when(userData.checkUserState(chatId)).thenReturn(UserState.WAITING_FOR_ACTIONS);

        SendMessage out = mainMenu.mainMenu(update, userData);

        verify(userData).changeUserState(chatId, UserState.WAITING_FOR_ARTISTS);
        assertEquals(Messages.SEARCH_MESSAGE, out.getText());
    }

    @Test
    void backCommandTest () {
        long chatId = 123L;

        when(update.getMessage()).thenReturn(tgMessage);
        when(tgMessage.getChatId()).thenReturn(chatId);
        when(tgMessage.getText()).thenReturn("/back");

        when(userData.checkUser(chatId)).thenReturn(true);
        when(userData.checkUserState(chatId)).thenReturn(UserState.WAITING_FOR_ARTISTS);

        SendMessage out = mainMenu.mainMenu(update, userData);

        verify(userData).changeUserState(chatId, UserState.WAITING_FOR_ACTIONS);
        assertEquals(Messages.BACK_MESSAGE, out.getText());
    }

    @Test
    void addNewUserTest() {
        long chatId = 123L;

        when(update.getMessage()).thenReturn(tgMessage);
        when(tgMessage.getChatId()).thenReturn(chatId);
        when(tgMessage.getText()).thenReturn("/start");

        when(userData.checkUser(chatId)).thenReturn(false);

        SendMessage out = mainMenu.mainMenu(update, userData);

        verify(userData).addUser(chatId);
        assertEquals(Messages.FIRST_MESSAGE, out.getText());
    }
}
