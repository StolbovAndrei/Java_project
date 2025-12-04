import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ParserKeys {
    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public Keys getKeys() throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("keys.json")) {
            if (inputStream == null) {
                throw new IOException("keys.json not found");
            }
            String json = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            Keys keys = mapper.readValue(json, Keys.class);
            return keys;
        }
    }
}