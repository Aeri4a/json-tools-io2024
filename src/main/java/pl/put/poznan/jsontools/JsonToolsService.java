package pl.put.poznan.jsontools;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.put.poznan.jsontools.exceptions.InvalidInputException;
import pl.put.poznan.jsontools.jsonmapper.JsonMapper;
import pl.put.poznan.jsontools.types.IJsonObject;
import pl.put.poznan.jsontools.types.JsonDto;

/**
 * Klasa JsonToolsService zawiera metody wywoływane przez JsonToolsController
 * w odpowiedzi na przychodzęce requesty. Metody wykonują operację/przekazują sterowanie do innych obiektów.
 */
@Service
public class JsonToolsService {

    private static final Logger logger = LoggerFactory.getLogger(JsonToolsService.class);

    /**
     * Instancja mappera wykonuje operacje na obiekcie JsonObject, JsonDto i przetwarzanym stringu
     */
    private final JsonMapper jsonMapper = new JsonMapper();

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
            logger.debug("tried minifying an invalid JSON: {}", inputJson.jsonString());
            throw new InvalidInputException("jsonString jest w niepoprawnym formacie");
        }
    }

    /**
     * Przekształca JSON w zminifikowanym zapisie na pełną strukturę. Jeżeli JSON jest syntaktycznie niepoprawny,
     * metoda wyrzuca wyjątek InvalidInputException
     *
     * @param inputJson obiekt zawierający String w formacie JSON
     * @return Obiekt zawierający String pełnej, sformatowanej struktury JSON.
     */
    public JsonDto format(JsonDto inputJson) {
        try {
            IJsonObject jsonObject = jsonMapper.toJsonObject(inputJson);
            return jsonMapper.toJsonDtoWithFormat(jsonObject);
        } catch (JsonProcessingException e) {
            logger.debug("tried minifying an invalid JSON: {}", inputJson.jsonString());
            throw new InvalidInputException("jsonString jest w niepoprawnym formacie");
        }
    }
}
