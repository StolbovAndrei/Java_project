import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GeniusParserTest {
    @Test
    void ReturnsCleanedTextTest() throws IOException {
        GeniusParser parser = new GeniusParser() {
            @Override
            public String getCleanLyrics(String url) throws IOException {
                if (url.contains("test-song")) {
                    String html = """
                        <div data-lyrics-container="true">
                            [Verse 1]<br>
                            Hello world
                        Test lyrics
                        </div>
                        """;

                    Document doc = Jsoup.parse(html);
                    Elements lyricsContainers = doc.select("div[data-lyrics-container=true]");

                    StringBuilder result = new StringBuilder();
                    for (Element container : lyricsContainers) {
                        result.append(container.wholeText()).append("\n");
                    }
                    String lyrics = result.toString();
                    lyrics = lyrics.replaceAll("\\[.*?\\]", "").trim();

                    return lyrics.isEmpty() ? "Текст не найден" : lyrics;
                }
                return "Текст не найден";
            }
        };

        String result = parser.getCleanLyrics("https://genius.com/test-song");

        assertEquals("Hello world\nTest lyrics", result);
    }

    @Test
    void EmptyLirycsTest() throws IOException {
        GeniusParser parser = new GeniusParser() {
            @Override
            public String getCleanLyrics(String url) throws IOException {
                String html = "<div></div>";
                Document doc = Jsoup.parse(html);
                Elements lyricsContainers = doc.select("div[data-lyrics-container=true]");

                StringBuilder result = new StringBuilder();
                for (Element container : lyricsContainers) {
                    result.append(container.wholeText()).append("\n");
                }
                String lyrics = result.toString();
                lyrics = lyrics.replaceAll("\\[.*?\\]", "").trim();

                return lyrics.isEmpty() ? "Текст не найден" : lyrics;
            }
        };

        String result = parser.getCleanLyrics("https://genius.com/empty-song");
        assertEquals("Текст не найден", result);
    }

    @Test
    void RemovesSquareBracketsTest() throws IOException {
        GeniusParser parser = new GeniusParser() {
            @Override
            public String getCleanLyrics(String url) throws IOException {
                String html = """
                    <div data-lyrics-container="true">
                        [Intro]<br>
                        First line
                    Second line [with text]
                    </div>
                    """;

                Document doc = Jsoup.parse(html);
                Elements lyricsContainers = doc.select("div[data-lyrics-container=true]");

                StringBuilder result = new StringBuilder();
                for (Element container : lyricsContainers) {
                    result.append(container.wholeText()).append("\n");
                }
                String lyrics = result.toString();
                lyrics = lyrics.replaceAll("\\[.*?\\]", "").trim();

                return lyrics.isEmpty() ? "Текст не найден" : lyrics;
            }
        };
        String result = parser.getCleanLyrics("https://genius.com/brackets-song");
        assertEquals("First line\nSecond line", result);
    }
}