import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SearchArtistsTest {

    @Test
    public void testSearchArtistsConnect() throws IOException, InterruptedException {
        MockWebServer server = new MockWebServer();
        server.start();

        String geniusResponse = """
        {
            "meta": {"status": 200},
            "response": {
                "hits": [
                    {"result": {"primary_artist": {"name": "test artist"}}}
                ]
            }
        }
        """;

        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("content-type", "application/json")
                .setBody(geniusResponse));
        try {
            SearchArtists searchArtists = new SearchArtists(server.url("/search").toString());

            String result = searchArtists.searchArtists("Kanye");

            assertTrue(result.contains("hits"));
            RecordedRequest request = server.takeRequest();
            assertEquals("GET", request.getMethod());
            Assertions.assertNotNull(request.getPath());
            assertTrue(request.getPath().startsWith("/search?q=Kanye"));
            assertNotNull(request.getHeader("Authorization"));

        } finally {
            server.shutdown();
        }
    }

    @Test
    public void testSearchArtistsReturnJson() throws IOException, InterruptedException {
        MockWebServer server = new MockWebServer();
        server.start();

        String geniusResponse = """
        {
            "meta": {"status": 200},
            "response": {
                "hits": [
                    {"result": {"primary_artist": {"name": "Kanye"}}}
                ]
            }
        }
        """;

        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("content-type", "application/json")
                .setBody(geniusResponse)
        );
        try {
            SearchArtists searchArtists = new SearchArtists(server.url("").toString());
            String result = searchArtists.searchArtists("Kanye");
            assertTrue(result.contains("Kanye"));
        } finally {
            server.shutdown();
        }
    }

    @Test
    public void testSearchArtistsReturnError() throws IOException, InterruptedException {
        MockWebServer server = new MockWebServer();
        server.start();

        server.enqueue(new MockResponse().setResponseCode(404));
        try {
            SearchArtists searchArtists = new SearchArtists(server.url("").toString());
            String result = searchArtists.searchArtists("Kanye");

            assertEquals("Kanye", result);
        } finally {
            server.shutdown();
        }
    }
}
