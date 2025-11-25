import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class SearchArtists {
    HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(12))
            .build();
    public String GENIUS_API_URL = "https://api.genius.com/search";
    ParserJsonKeys parserJsonKeys = new ParserJsonKeys();
    Keys keys = parserJsonKeys.parserJsonKeysToPojo();
    private final String GENIUS_API_TOKEN = keys.getGeniusApiToken();


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
    public SearchArtists() throws IOException {
    }
    }

