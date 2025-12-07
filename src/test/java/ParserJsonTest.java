import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParserJsonTest {

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private ParserJson parserJson;

    private String testJson;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        testJson = """
            {
                "response": {
                    "hits": [
                        {
                            "result": {
                                "artist_names": "Kanye West",
                                "title": "Father stretch my hands"
                            }
                        }
                    ]
                }
            }
            """;
    }

    @Test
    void testParserJsonOnOneArtist() throws Exception {
        GeniusResponse mockResponse = mock(GeniusResponse.class);
        when(mapper.readValue(anyString(), eq(GeniusResponse.class)))
                .thenReturn(mockResponse);

        List<Hits> hits = new ArrayList<>();
        Hits hit = mock(Hits.class);
        Result result = mock(Result.class);
        when(result.getArtistNames()).thenReturn("Kanye West");
        when(result.getTitle()).thenReturn("Father stretch my hands");
        when(hit.getResult()).thenReturn(result);
        hits.add(hit);

        Response response = mock(Response.class);
        when(response.getHits()).thenReturn(hits);
        when(mockResponse.getResponse()).thenReturn(response);

        Map<String, List<String>> songs = parserJson.parserJson(testJson);

        assertEquals(1, songs.size());
        assertTrue(songs.containsKey("Kanye West"));
        assertEquals(1, songs.get("Kanye West").size());
        assertEquals("Father stretch my hands", songs.get("Kanye West").getFirst());
        verify(mapper).readValue(anyString(), eq(GeniusResponse.class));
    }

    @Test
    void testParserJsonOnMultipleArtists() throws Exception {
        String jsonTest = """
        {
            "response": {
                "hits": [
                    {"result": {"artist_names": "Artist1", "title": "Song1"}},
                    {"result": {"artist_names": "Artist2", "title": "Song2"}},
                    {"result": {"artist_names": "Artist1", "title": "Song3"}}
                ]
            }
        }
        """;
        GeniusResponse mockResponse = mock(GeniusResponse.class);
        when(mapper.readValue(anyString(), eq(GeniusResponse.class)))
                .thenReturn(mockResponse);
        List<Hits> hits = new ArrayList<>();
        Hits hit1 = mock(Hits.class);
        Result result1 = mock(Result.class);
        when(result1.getArtistNames()).thenReturn("Artist1");
        when(result1.getTitle()).thenReturn("Song1");
        when(hit1.getResult()).thenReturn(result1);
        hits.add(hit1);

        Hits hit2 = mock(Hits.class);
        Result result2 = mock(Result.class);
        when(result2.getArtistNames()).thenReturn("Artist2");
        when(result2.getTitle()).thenReturn("Song2");
        when(hit2.getResult()).thenReturn(result2);
        hits.add(hit2);

        Hits hit3 = mock(Hits.class);
        Result result3 = mock(Result.class);
        when(result3.getArtistNames()).thenReturn("Artist1");
        when(result3.getTitle()).thenReturn("Song3");
        when(hit3.getResult()).thenReturn(result3);
        hits.add(hit3);



        Response response = mock(Response.class);
        when(response.getHits()).thenReturn(hits);
        when(mockResponse.getResponse()).thenReturn(response);

        Map<String, List<String>> songs = parserJson.parserJson(testJson);

        assertEquals(2, songs.size());
        assertEquals(2, songs.get("Artist1").size());
        assertEquals(1, songs.get("Artist2").size());
        assertTrue(songs.get("Artist1").contains("Song1"));
        assertTrue(songs.get("Artist1").contains("Song3"));
    }


}
