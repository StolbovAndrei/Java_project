import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class SearchArtists {
    private final HttpClient client;
    public String GENIUS_API_URL = "https://api.genius.com/search";
    private final String GENIUS_API_TOKEN = "pMyqp0t07vzfUsnC-h0XsjYyV_rHi282GKczPrE6VjPLIzo2rn0XvKVTl8j-c1Bb";

    public SearchArtists() throws IOException {
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(12))
                .build();

    }

    public String searchArtists(String message) throws IOException, InterruptedException {
       String searchMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);



       HttpRequest request = HttpRequest.newBuilder()
               .uri(URI.create(GENIUS_API_URL + "?q=" + searchMessage))
               .header("Accept", "application/json")
               .header("Authorization", "Bearer " + GENIUS_API_TOKEN)
               .GET()
               .build();
       HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

       if(response.statusCode() != 200){
           System.err.println("Ошибка: " + response.body());
           return searchMessage;
       }
       return response.body();
    }
    }

