import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MainMenuStartSearchTest {

    @Mock
    private GeniusClient geniusClient;

    @Mock
    private FormatClass formatClass;

    private MainMenu mainMenu;

    @BeforeEach
    void setUp() {
        mainMenu = new MainMenu(geniusClient, formatClass);
    }

    @Test
    void startSearchTest() throws Exception {
        String query = "Beatles";
        Map<String, List<String>> songs = Map.of("The Beatles", List.of("Hey Jude"));
        when(geniusClient.searchArtists(query)).thenReturn(songs);
        when(formatClass.formatResult(songs)).thenReturn("formatted");

        String result = mainMenu.startSearch(query);

        verify(geniusClient).searchArtists(query);
        verify(formatClass).formatResult(songs);
        assertEquals("formatted", result);
    }
}