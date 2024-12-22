package pl.put.poznan.jsontools.jsonmapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.put.poznan.jsontools.decorators.ExclusionDecorator;
import pl.put.poznan.jsontools.decorators.InclusionDecorator;
import pl.put.poznan.jsontools.types.IJsonObject;
import pl.put.poznan.jsontools.types.JsonDto;
import pl.put.poznan.jsontools.types.JsonObject;
import pl.put.poznan.jsontools.types.UnnestedJsonDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Klasa typu Service zawierająca metody do przekształcania JSONa między postacią w formacie String oraz obiektu JsonObject
 */
public class JsonMapper {

    /**
     * Obiekt mappera z biblioteki Jackson wykorzystywany do przetwarzania formatu json
     */
    private final ObjectMapper objectMapper;


    public JsonMapper() {
        this.objectMapper = new ObjectMapper();
    }

    public JsonMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Mapuje strukturę JsonDTO na strukturę JsonObject, zachowując wszystkie pary klucz-wartość oryginalnego JSONa
     *
     * @param jsonDto obiekt zawierający reprezentację JSONa w formacie String
     * @return obiekt zawierający reprezentację JSONa w postaci mapy klucz-wartość, z zachowaniem kształtu danych
     * @throws JsonProcessingException Jeżeli String JSONa jest syntaktycznie niepoprawny albo pusty
     * @see JsonObject
     * @see JsonDto
     */
    public IJsonObject toJsonObject(JsonDto jsonDto) throws JsonProcessingException {
        Map<String, Object> jsonValues = objectMapper.readValue(jsonDto.jsonString(), new TypeReference<>() {
        });
        IJsonObject jsonObject = new JsonObject(jsonValues);

        if (jsonDto.includeKeys() != null) {
            jsonObject = new InclusionDecorator(jsonObject, jsonDto.includeKeys());
        }
        if (jsonDto.excludeKeys() != null) {
            jsonObject = new ExclusionDecorator(jsonObject, jsonDto.excludeKeys());
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
    public JsonDto toJsonDto(IJsonObject jsonObject) throws JsonProcessingException {
        return new JsonDto(objectMapper.writeValueAsString(jsonObject.getValues()), null, null);
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
    public JsonDto toJsonDtoWithFormat(IJsonObject jsonObject) throws JsonProcessingException {
        return new JsonDto(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject.getValues()), null, null);
    }

    /**
     * Mapuje strukturę List<JsonObject> na strukturę UnnestedJsonDto z
     *
     * @param jsonObjects lista obiektów zawierającuych reprezentację JSONa w postaci mapy klucz-wartość
     * @return Lista zawierający reprezentację każdej zagnieżdżonej struktury JSON
     * @throws JsonProcessingException Nie powinno się nigdy stać na tym etapie, ale teoretycznie może
     * @see JsonObject
     * @see JsonDto
     */
    public UnnestedJsonDto toUnnestedJsonDto(List<IJsonObject> jsonObjects) throws JsonProcessingException {
        List<JsonDto> jsonDtos = new ArrayList<>();
        for (IJsonObject jsonObject : jsonObjects) {
            jsonDtos.add(toJsonDto(jsonObject));
        }
        return new UnnestedJsonDto(jsonDtos);
    }
}
