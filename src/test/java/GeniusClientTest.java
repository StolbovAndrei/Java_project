import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GeniusClientTest {

    private MockWebServer server;
    private HttpClient httpClient;

    @BeforeEach
    public void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }
    @AfterEach
    public void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    public void testSearchArtistsConnect() throws IOException, InterruptedException {

        String geniusResponse = """
        {
            "response": {
                "hits": [
                    {
                        "result": {
                            "artist_names": "The Beatles",
                            "title": "Hey Jude"
                        }
                    },
                    {
                        "result": {
                            "artist_names": "The Beatles",
                            "title": "Yesterday"
                        }
                    }
                ]
            }
        }
        """;

        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("content-type", "application/json")
                .setBody(geniusResponse));

        GeniusClient client;
        client = new GeniusClient(server.url("/search").toString());

        Map<String, List<String>> result = client.searchArtists("Beatles");

        assertEquals(1, result.size());
        assertEquals(2, result.get("The Beatles").size());
        assertTrue(result.get("The Beatles").contains("Hey Jude"));

        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertTrue(recordedRequest.getPath().endsWith("q=Beatles"));
        assertNotNull(recordedRequest.getHeader("Authorization"));
    }

    @Test
    public void testSearchArtistsMultipleArtists() throws IOException, InterruptedException {

        String geniusResponse = """
        {
            "response": {
                "hits": [
                    {"result": {"artist_names": "The Beatles", "title": "Hey Jude"}},
                    {"result": {"artist_names": "Queen", "title": "Bohemian Rhapsody"}},
                    {"result": {"artist_names": "The Beatles", "title": "Yesterday"}}
                ]
            }
        }
        """;

        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("content-type", "application/json")
                .setBody(geniusResponse)
        );

        GeniusClient client = new GeniusClient(server.url("").toString());
        Map<String, List<String>> result = client.searchArtists("rock");

        assertEquals(2, result.size());
        assertEquals(2, result.get("The Beatles").size());
        assertEquals(1, result.get("Queen").size());
    }

    @Test
    public void testSearchArtistsReturnError() throws IOException, InterruptedException {

        server.enqueue(new MockResponse().setResponseCode(401));

        GeniusClient client = new GeniusClient(server.url("").toString());
        Map<String, List<String>> result = client.searchArtists("Beatles");

        assertNull(result);
    }

    @Test
    void testSearchArtistsLimit() throws IOException, InterruptedException {
        StringBuilder json = new StringBuilder("""
                {
                    "response": {
                        "hits": [
                """);
        for (int i = 1; i <= 6; i++) {
            json.append("{\"result\": {\"artist_names\": \"Artist").append(i).append("\", \"title\": \"Song").append(i).append("\"}},");
        }
        json.setLength(json.length() - 1);
        json.append("]\n            }\n        }");
        server.enqueue(new MockResponse().setResponseCode(200).setBody(json.toString()));
        GeniusClient client = new GeniusClient(server.url("").toString());
        Map<String, List<String>> result = client.searchArtists("many");

        assertEquals(5, result.values().stream().mapToInt(List::size).sum());

    }
}
