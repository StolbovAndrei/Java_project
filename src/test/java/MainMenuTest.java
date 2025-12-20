import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MainMenuTest {

    @Mock
    private GeniusClient mockGeniusClient;

    @Mock
    private UserData mockUserData;

    @Test
    void testMainMenuFlow() throws Exception {
        MainMenu mainMenu = new MainMenu(mockGeniusClient);
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn("/search");
        when(message.getChatId()).thenReturn(12345L);

        when(mockUserData.checkUser(12345L)).thenReturn(true);
        when(mockUserData.checkUserState(12345L)).thenReturn(UserState.WAITING_FOR_ACTIONS);

        SendMessage result = mainMenu.mainMenu(update, mockUserData);
        assertNotNull(result);
        assertEquals("12345", result.getChatId());
        verify(mockUserData).changeUserState(12345L, UserState.WAITING_FOR_ARTISTS);
    }

    @Test
    void testSearchFunctionality() throws Exception {
        MainMenu mainMenu = new MainMenu(mockGeniusClient);
        Map<String, java.util.List<String>> searchResults = new HashMap<>();
        searchResults.put("Test Artist", Arrays.asList("Test Song"));

        when(mockGeniusClient.searchArtists("test")).thenReturn(searchResults);
        when(mockGeniusClient.getUrls("test")).thenReturn(Arrays.asList("url1"));
        SendMessage result = mainMenu.handleSearchMenu("test", mockUserData, 12345L);
        assertNotNull(result);
        assertEquals("12345", result.getChatId());
        assertTrue(result.getText().contains("Найденные треки"));
    }

    @Test
    void testHelpCommand() {
        MainMenu mainMenu = new MainMenu(mockGeniusClient);
        SendMessage result = mainMenu.handleMainMenu("/help", mockUserData, 12345L);
        assertEquals("12345", result.getChatId());
        assertTrue(result.getText().contains(Messages.HELP_MESSAGE));
    }
}