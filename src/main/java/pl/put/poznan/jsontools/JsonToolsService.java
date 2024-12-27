package pl.put.poznan.jsontools;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.put.poznan.jsontools.exceptions.InvalidInputException;
import pl.put.poznan.jsontools.jsonmapper.JsonMapper;
import pl.put.poznan.jsontools.types.IJsonObject;
import pl.put.poznan.jsontools.types.InputCompareDto;
import pl.put.poznan.jsontools.types.JsonDto;
import pl.put.poznan.jsontools.types.OutputCompareDto;

import java.util.ArrayList;
import java.util.List;
import pl.put.poznan.jsontools.types.JsonObject;
import pl.put.poznan.jsontools.types.UnnestedJsonDto;

import java.util.*;

/**
 * Klasa JsonToolsService zawiera metody wywoływane przez JsonToolsController
 * w odpowiedzi na przychodzące requesty. Metody wykonują operację/przekazują sterowanie do innych obiektów.
 */
@Service
public class JsonToolsService {

    private static final Logger logger = LoggerFactory.getLogger(JsonToolsService.class);

    /**
     * Instancja mappera wykonuje operacje na obiekcie JsonObject, JsonDto i przetwarzanym stringu
     */
    private final JsonMapper jsonMapper;

    public JsonToolsService() {
        this.jsonMapper = new JsonMapper();
    }

    public JsonToolsService(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

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
    /**
     * Rekursywnie przechodzi przez strukturę JSON dodając do listy znalezione zagnieżdżenia
     * metoda wyrzuca wyjątek InvalidInputException
     *
     * @param jsonObject reprezentacja struktury JSON jaka para klucz, wartość
     * @return Listę zawierającą wszystkie zagnieżdżone struktury
     */
    private List<IJsonObject> unnest_rec(LinkedHashMap<String, ?> jsonObject) {
        List<IJsonObject> unnest = new ArrayList<>();
        Map<String, Object> tmp = new HashMap<>();
        for (String key : jsonObject.keySet()) {
            if (jsonObject.get(key) instanceof LinkedHashMap<?,?>) {
                    LinkedHashMap<String,?> value = (LinkedHashMap<String, ?>) jsonObject.get(key);
                    unnest_rec(value, unnest);
            } else {
                tmp.put(key, jsonObject.get(key));
            }
        }

        IJsonObject item = new JsonObject(tmp);
        unnest.add(item);

        return unnest;
    }

    /**
     * Rekursywnie przechodzi przez strukturę JSON dodając do listy znalezione zagnieżdżenia
     * metoda wyrzuca wyjątek InvalidInputException
     *
     * @param jsonObject reprezentacja struktury JSON jaka para klucz, wartość
     */
    private void unnest_rec(LinkedHashMap<String,?> jsonObject, List<IJsonObject> jsonList) {
        Map<String, Object> tmp = new HashMap<>();
        for (String key : jsonObject.keySet()) {
            if (jsonObject.get(key) instanceof LinkedHashMap<?, ?>) {
                LinkedHashMap<String, ?> value = (LinkedHashMap<String, ?>) jsonObject.get(key);
                unnest_rec(value, jsonList);
            } else {
                tmp.put(key, jsonObject.get(key));
            }
        }

        IJsonObject item = new JsonObject(tmp);
        if (!item.getValues().isEmpty()){
            jsonList.add(item);
        }
    }

    /**
     * Przekształca JSON w listę wszystkich struktur JSON poprzez usuwanie zagnieżdżeń
     * metoda wyrzuca wyjątek InvalidInputException
     *
     * @param inputJson obiekt zawierający String w formacie JSON
     * @return Obiekt zawierający listę wszystkich struktur JSON nie posiądających zagnieżdżeń
     */
    public UnnestedJsonDto unnest(JsonDto inputJson) {
        try {
            Map<String, Object> jsonObject = jsonMapper.toJsonObject(inputJson).getValues();

            List<IJsonObject> unnested = unnest_rec((LinkedHashMap<String, ?>) jsonObject);

            return jsonMapper.toUnnestedJsonDto(unnested);
        } catch (JsonProcessingException e) {
            logger.debug("tried unnesting an invalid JSON: {}", inputJson.jsonString());
            throw new InvalidInputException("jsonString jest w niepoprawnym formacie");
        }
    }

    /**
     * Porównuje dwa obiekty String i identyfikuje numery linii, które są od siebie różne.
     * <p>
     * Dzieli dane wejściowe na linie i porównuje odpowiadające sobie linie.
     * Jeżeli linie są różne, ich numer jest dodawany do listy result.
     *
     * @param inputStrings obiekt typu {@link InputCompareDto}, zawierający dwa ciągi tekstowe, które mają zostać porównane
     * @return obiekt {@link OutputCompareDto}, zawierający listę numerów linii, które różnią się między sobą
     */
    public OutputCompareDto compare(InputCompareDto inputStrings) {
        if (inputStrings.string1() == null || inputStrings.string2() == null) {
            throw new InvalidInputException("at last one of string is null");
        }

        String[] lines1 = inputStrings.string1().split("\\R");
        String[] lines2 = inputStrings.string2().split("\\R");

        List<Integer> result = new ArrayList<>();
        int linesNumber = Math.max(lines1.length, lines2.length);

        for (int i = 0; i < linesNumber; i++) {
            String line1 = (i < lines1.length) ? lines1[i].trim() : "";
            String line2 = (i < lines2.length) ? lines2[i].trim() : "";

            if (!line1.equals(line2)) {
                result.add(i + 1);
            }
        }
        return new OutputCompareDto(result);
    }
}
