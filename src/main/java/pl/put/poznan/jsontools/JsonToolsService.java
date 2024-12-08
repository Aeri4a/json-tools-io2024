package pl.put.poznan.jsontools;

import org.springframework.stereotype.Service;
import pl.put.poznan.jsontools.types.JsonDto;

@Service
public class JsonToolsService {

    public JsonDto minify(JsonDto inputJson) {
        return new JsonDto(inputJson.jsonString().toLowerCase());
    }

    public JsonDto format(JsonDto inputJson) {
        return new JsonDto(inputJson.jsonString().toUpperCase());
    }
}
