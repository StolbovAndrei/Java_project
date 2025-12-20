import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

public class GeniusClient {
    private final HttpClient client;
    private String geniusApiUrl = "https://api.genius.com/search";
    private final ParserKeys parserKeys = new ParserKeys();
    private final Keys keys = parserKeys.getKeys();
    private String geniusApiToken = keys.getGeniusApiToken();
    private ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public GeniusClient() throws IOException {
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(12))
                .build();
    }

    public GeniusClient(String baseUrl) throws IOException {
        this.geniusApiUrl = baseUrl;
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(12))
                .build();
    }

    public GeniusClient(HttpClient client, ObjectMapper mapper, Keys keys) throws IOException {
        this.client = client;
        this.mapper = mapper;
        this.geniusApiToken = keys.getGeniusApiToken();
        this.geniusApiUrl = "https://api.genius.com/search";

    }

    public Map<String, List<String>> searchArtists(String message) throws IOException, InterruptedException {
        String searchMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(geniusApiUrl + "?q=" + searchMessage))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + geniusApiToken)
                .GET()
                .build();
        HttpResponse<String> responseServer = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (responseServer.statusCode() != 200) {
            System.err.println("Ошибка: " + responseServer.body());
            return null;
        }

        GeniusResponse geniusResponse = mapper.readValue(responseServer.body(), GeniusResponse.class);
        Response response = geniusResponse.getResponse();
        List<Hits> hits = response.getHits();
        Map<String, List<String>> songs = new LinkedHashMap<>();
        for (int i = 0; i < Math.min(geniusResponse.getResponse().getHits().size(), 5); i++) {
            if (songs.containsKey(hits.get(i).getResult().getArtistNames())) {
                songs.get(hits.get(i).getResult().getArtistNames()).add(hits.get(i).getResult().getTitle());
                System.out.println(hits.get(i).getResult().getUrl());
            } else {
                songs.put(hits.get(i).getResult().getArtistNames(), new ArrayList<>());
                songs.get(hits.get(i).getResult().getArtistNames()).add(hits.get(i).getResult().getTitle());
            }
        }
        return songs;
    }
    public List<String> getUrls(String message) throws IOException, InterruptedException {
        String searchMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(geniusApiUrl + "?q=" + searchMessage))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + geniusApiToken)
                .GET()
                .build();
        HttpResponse<String> responseServer = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (responseServer.statusCode() != 200) {
            System.err.println("Ошибка: " + responseServer.body());
            return null;
        }

        GeniusResponse geniusResponse = mapper.readValue(responseServer.body(), GeniusResponse.class);
        Response response = geniusResponse.getResponse();
        List<Hits> hits = response.getHits();
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < Math.min(geniusResponse.getResponse().getHits().size(), 5); i++) {
            urls.add(hits.get(i).getResult().getUrl());
        }
        return urls;
    }

}