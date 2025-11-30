import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class SearchArtistsTest {
    private HttpResponse<String> mockHttpResponse;
    private HttpClient mockHttpClient;
    private SearchArtists searchArtists;
    @BeforeEach
    void setUp() throws Exception{
        mockHttpClient = mock(HttpClient.class);
        mockHttpResponse = mock(HttpResponse.class);
        searchArtists = new SearchArtists();

        setPrivateField(searchArtists, "client", mockHttpClient);
        setPrivateField(searchArtists, "GENIUS_API_TOKEN", "test-token");
    }
    private void setPrivateField(Object target, String fieldName, Object value) throws Exception{
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void searchArtistsSuccess() throws Exception{
        String exceptedJson = "{\"response\": {\"hits\": [{\"title\": \"Father stretch my hands\"}}]}}";
        when(mockHttpClient.<String>send(any(), any())).thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(200);
        when(mockHttpResponse.body()).thenReturn(exceptedJson);

        String result = searchArtists.searchArtists("Kanye West");

        assertEquals(exceptedJson, result);
    }

    @Test
    void searchArtistErrorResponse() throws Exception{
        when(mockHttpClient.<String>send(any(), any())).thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(500);
        when(mockHttpResponse.body()).thenReturn("Server error");

        String result = searchArtists.searchArtists("Test Artist");
        String exceptedEncoded = URLEncoder.encode("Test Artist", StandardCharsets.UTF_8);

        assertEquals(exceptedEncoded, result);
    }

    @Test
    void searchArtistsIOException() throws Exception{
        when(mockHttpClient.<String>send(any(), any())).thenThrow(new IOException("Network error"));
        assertThrows(IOException.class, () -> searchArtists.searchArtists("Test"));

    }


}
