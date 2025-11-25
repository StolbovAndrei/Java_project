import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ParserJsonKeys {
    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    public Keys parserJsonKeysToPojo() throws IOException {
        File file = new File("src/main/resources/keys.json");
        Keys keys = mapper.readValue(file, Keys.class);
        return keys;

    }
}
