package pl.put.poznan.jsontools;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import pl.put.poznan.jsontools.jsonmapper.JsonMapper;
import pl.put.poznan.jsontools.types.JsonDto;

@Service
public class JsonToolsService {

    JsonMapper jsonMapper = new JsonMapper();

    /**
     * Minifikuje otrzymaną strukturę JSON
     *
     * @param inputJson String w formacie JSON otrzymany w zapytaniu
     * @return String zminifikowanej struktury JSON
     */
    public JsonDto minify(JsonDto inputJson) {

        try {
            return jsonMapper.toJsonDto(jsonMapper.toJsonObject(inputJson));
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public JsonDto format(JsonDto inputJson) {
        return new JsonDto(inputJson.jsonString().toUpperCase());
    }
}
