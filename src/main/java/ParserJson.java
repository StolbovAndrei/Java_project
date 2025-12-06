import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

public class ParserJson {
    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    public Map<String, List<String>> parserJson(String json) throws JsonProcessingException {
        GeniusResponse geniusResponse = mapper.readValue(json, GeniusResponse.class);

        Map<String, List<String>> songs = new LinkedHashMap<>();
        for(int i = 0; i < Math.min(geniusResponse.getResponse().getHits().size(), 5); i++){
            if(songs.containsKey(geniusResponse.getResponse().getHits().get(i).getResult().getArtistNames())){
                songs.get(geniusResponse.getResponse().getHits().get(i).getResult().getArtistNames()).add(geniusResponse.getResponse().getHits().get(i).getResult().getTitle());
            }
            else{
                songs.put(geniusResponse.getResponse().getHits().get(i).getResult().getArtistNames(), new ArrayList<>());
                songs.get(geniusResponse.getResponse().getHits().get(i).getResult().getArtistNames()).add(geniusResponse.getResponse().getHits().get(i).getResult().getTitle());
            }
        }
        return songs;
    }
    public String resultOfSearch(Map<String, List<String>> songs){
        StringBuilder result = new StringBuilder();
        result.append("Найденные треки: \n\n");
        for (Map.Entry<String, List<String>> entry : songs.entrySet()){
            for(int i = 0; i < entry.getValue().size(); ++i) {
                result.append(entry.getKey()).append(" ---- ").append(entry.getValue().get(i)).append("\n\n");
            }
        }
        return result.toString();

    }

    }


