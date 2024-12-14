package pl.put.poznan.jsontools.jsonmapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import pl.put.poznan.jsontools.decorations.InclusionDecorator;
import pl.put.poznan.jsontools.decorations.JsonDecorator;
import pl.put.poznan.jsontools.types.JsonDto;
import pl.put.poznan.jsontools.types.JsonObject;

import java.util.List;
import java.util.Map;

/**
 * Klasa typu Service zawierająca metody do przekształcania JSONa między postacią w formacie String oraz obiektu JsonObject
 */
public class JsonMapper {
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Mapuje strukturę JsonDTO na strukturę JsonObject, zachowując wszystkie pary klucz-wartość oryginalnego JSONa
     *
     * @param jsonDto obiekt zawierający reprezentację JSONa w formacie String
     * @return obiekt zawierający reprezentację JSONa w postaci mapy klucz-wartość, z zachowaniem kształtu danych
     * @throws JsonProcessingException Jeżeli String JSONa jest syntaktycznie niepoprawny albo pusty
     * @see JsonObject
     * @see JsonDto
     */
    public JsonObject toJsonObject(JsonDto jsonDto) throws JsonProcessingException {
        Map<String, Object> jsonValues = objectMapper.readValue(jsonDto.jsonString(), new TypeReference<>() {});
        JsonObject jsonObject = new JsonObject(jsonValues);

        if (jsonDto.includeKeys() != null) {
            JsonDecorator decorator = new InclusionDecorator(jsonObject, jsonDto.includeKeys());
            decorator.setValues(jsonValues);
        }
        if (jsonDto.excludeKeys() != null) {
            ;
        }
        return jsonObject;
    }

    /**
     * Mapuje strukturę JsonObject na strukturę JsonDto, zachowując wszystkie pary klucz-wartość oryginalnego JSONa
     *
     * @param jsonObject obiekt zawierający reprezentację JSONa w postaci mapy klucz-wartość
     * @return obiekt zawierający reprezentację JSONa w formacie String, z zachowaniem kształtu danych
     * @throws JsonProcessingException Nie powinno się nigdy stać na tym etapie, ale teoretycznie może
     * @see JsonObject
     * @see JsonDto
     */
    public JsonDto toJsonDto(JsonObject jsonObject) throws JsonProcessingException {
        return new JsonDto(objectMapper.writeValueAsString(jsonObject.getValues()));
    }

    /**
     * Mapuje strukturę JsonObject na strukturę JsonDto, zachowując wszystkie pary klucz-wartość oryginalnego JSONa.
     * Finalny string występuje w sformatowanej postaci (z dodaną indentacją oraz wielolinijkowy)
     *
     * @param jsonObject obiekt zawierający reprezentację JSONa w postaci mapy klucz-wartość
     * @return obiekt zawierający reprezentację JSONa w formacie String, z zachowaniem kształtu danych
     * @throws JsonProcessingException Nie powinno się nigdy stać na tym etapie, ale teoretycznie może
     * @see JsonObject
     * @see JsonDto
     */
    public JsonDto toJsonDtoWithFormat(JsonObject jsonObject) throws JsonProcessingException {
        return new JsonDto(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject.getValues()));
    }

}
