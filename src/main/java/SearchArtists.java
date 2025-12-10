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
    public String geniusApiUrl = "https://api.genius.com/search";
    ParserKeys parserKeys = new ParserKeys();
    Keys keys = parserKeys.getKeys();
    private final String geniusApiToken = keys.getGeniusApiToken();

    public SearchArtists() throws IOException {
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(12))
                .build();
    }
    public SearchArtists(String baseUrl) throws IOException {
        this.geniusApiUrl = baseUrl;
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(12))
                .build();
    }

    public String searchArtists(String message) throws IOException, InterruptedException {
        String searchMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(geniusApiUrl + "?q=" + searchMessage))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + geniusApiToken)
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

