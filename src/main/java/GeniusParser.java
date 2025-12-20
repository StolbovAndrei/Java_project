import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class GeniusParser {

    public GeniusParser() {
    }

    public String getCleanLyrics(String url) throws IOException {
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                        + "AppleWebKit/537.36 (KHTML, like Gecko) "
                        + "Chrome/131.0.0.0 Safari/537.36")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .header("Accept-Language", "en-US,en;q=0.9")
                .header("Referer", "https://www.google.com/")
                .header("Connection", "keep-alive")
                .header("Upgrade-Insecure-Requests", "1")
                .timeout(15000)
                .get();


        Elements lyricsContainers = doc.select("div[data-lyrics-container=true]");

        StringBuilder result = new StringBuilder();
        for (Element container : lyricsContainers) {
            result.append(container.wholeText()).append("\n");
        }
        String lyrics = result.toString();
        lyrics = lyrics.replaceAll("\\[.*?\\]", "").trim();

        return lyrics.isEmpty() ? "Текст не найден" : lyrics;
    }}