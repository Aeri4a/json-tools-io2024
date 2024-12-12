package pl.put.poznan.jsontools.jsonmapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import pl.put.poznan.jsontools.types.JsonDto;
import pl.put.poznan.jsontools.types.JsonObject;

import java.util.Map;

@Service
public class JsonMapper {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonObject toJsonObject(JsonDto jsonDto) throws JsonProcessingException {
        Map<String, Object> jsonValues = objectMapper.readValue(jsonDto.jsonString(), new TypeReference<>() {});
        return new JsonObject(jsonValues);
    }

    public JsonDto toJsonDto(JsonObject jsonObject) throws JsonProcessingException {
        return new JsonDto(objectMapper.writeValueAsString(jsonObject.getValues()));
    }

    public JsonDto toJsonDtoWithFormat(JsonObject jsonObject) throws JsonProcessingException {
        return new JsonDto(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject.getValues()));
    }

}
