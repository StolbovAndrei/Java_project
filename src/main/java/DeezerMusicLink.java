import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class DeezerMusicLink {

    private final HttpClient httpClient;

    public DeezerMusicLink() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public DeezerMusicLink(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String getDeezerMusicLink(String message) throws Exception {
        message = message.replaceAll("\\(.*?\\)", "");
        String[] parts = message.split("-", 2);
        String artist = parts[0].trim();
        String title = parts.length > 1 ? parts[1].trim() : "";

        String query = URLEncoder.encode(artist + " " + title, StandardCharsets.UTF_8);
        String apiUrl = "https://api.deezer.com/search?q=" + query + "&limit=1";

        HttpResponse<String> response = httpClient.send(
                HttpRequest.newBuilder().uri(URI.create(apiUrl)).GET().build(),
                HttpResponse.BodyHandlers.ofString()
        );

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response.body());

        if (json.has("data") && json.get("data").size() > 0) {
            JsonNode track = json.get("data").get(0);
            return track.path("link").asText();
        }

        return "Трек не найден!";
    }
}