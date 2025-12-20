import org.junit.jupiter.api.Test;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DeezerMusicLinkTest {

    @Test
    void getDeezerMusicLinkValidTrackReturnsCorrectLink() throws Exception {
        String message = "Test Artist - Test Song";
        String apiResponse = """
            {
                "data": [
                    {
                        "link": "https://deezer.com/track/12345"
                    }
                ]
            }
            """;
        HttpClient mockHttpClient = mock(HttpClient.class);
        HttpResponse mockHttpResponse = mock(HttpResponse.class);

        when(mockHttpClient.send(any(HttpRequest.class), any()))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.body()).thenReturn(apiResponse);

        DeezerMusicLink deezerMusicLink = new DeezerMusicLink(mockHttpClient);
        String result = deezerMusicLink.getDeezerMusicLink(message);

        assertEquals("https://deezer.com/track/12345", result);
    }

    @Test
    void getDeezerMusicLinkTrackNotFoundReturnsNotFoundMessage() throws Exception {
        String message = "Unknown Artist - Unknown Song";
        String apiResponse = """
            {
                "data": []
            }
            """;

        HttpClient mockHttpClient = mock(HttpClient.class);
        HttpResponse mockHttpResponse = mock(HttpResponse.class);

        when(mockHttpClient.send(any(HttpRequest.class), any()))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.body()).thenReturn(apiResponse);

        DeezerMusicLink deezerMusicLink = new DeezerMusicLink(mockHttpClient);
        String result = deezerMusicLink.getDeezerMusicLink(message);
        assertEquals("Трек не найден!", result);
    }
}