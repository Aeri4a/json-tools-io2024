package pl.put.poznan.jsontools.jsonmapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import pl.put.poznan.jsontools.types.JsonObject;

import java.util.Map;

@Service
public class JsonMapper {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonObject toJsonObject(String jsonString) throws JsonProcessingException {
        Map<String, Object> jsonValues = objectMapper.readValue(jsonString, new TypeReference<>() {});
        return new JsonObject(jsonValues);
    }

    public String toJsonString(JsonObject jsonObject) throws JsonProcessingException {
        return objectMapper.writeValueAsString(jsonObject.getValues());
    }
}
