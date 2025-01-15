package pl.put.poznan.jsontools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.DeltaType;
import com.github.difflib.patch.Patch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.put.poznan.jsontools.exceptions.InvalidInputException;
import pl.put.poznan.jsontools.jsonmapper.JsonMapper;
import pl.put.poznan.jsontools.types.IJsonObject;
import pl.put.poznan.jsontools.types.InputCompareDto;
import pl.put.poznan.jsontools.types.JsonDto;
import pl.put.poznan.jsontools.types.OutputCompareDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
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
     * Rozbija obiekt String na listę linijek w nim się zawierających z uszanowaniem pustych linii
     *
     * @param string obiekt, który ma zostać przekształcony na listę stringów
     * @return obiekt będący listą linijek obecnych w oryginalnym stringu
     */
    private List<String> splitLines(String string) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new StringReader(string))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            if (string.endsWith("\n") || string.isEmpty()) {
                lines.add("");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error splitting lines", e);
        }
        return lines;
    }

    /**
     * Porównuje dwa obiekty String i identyfikuje różnice między nimi w domyślnym formacie diff (Unix)
     *
     * @param inputStrings obiekt typu {@link InputCompareDto}, zawierający dwa ciągi tekstowe, które mają zostać porównane
     * @return obiekt {@link OutputCompareDto}, zawierający listę różnic diff
     */
    public OutputCompareDto compare(InputCompareDto inputStrings) {
        if (inputStrings.string1() == null || inputStrings.string2() == null) {
            throw new InvalidInputException("at least one of strings is null");
        }

        List<String> string1 = splitLines(inputStrings.string1());
        List<String> string2 = splitLines(inputStrings.string2());

        List<String> result = new ArrayList<>();

        Patch<String> patch = DiffUtils.diff(string1, string2);

        for (AbstractDelta<String> delta : patch.getDeltas()) {
            DeltaType type = delta.getType();
            int position1 = delta.getSource().getPosition() + 1;
            int position2 = delta.getTarget().getPosition() + 1;

            List<String> oldLines = delta.getSource().getLines();
            List<String> newLines = delta.getTarget().getLines();

            switch (type) {
                case DELETE:
                    result.add(position1 + "d" + (position2 - 1));
                    for (String line : oldLines) {
                        result.add("< " + line);
                    }
                    break;
                case INSERT:
                    result.add((position1 - 1) + "a" + position2 + (newLines.size() > 1 ? "," + (position2 + newLines.size() - 1) : ""));
                    for (String line : newLines) {
                        result.add("> " + line);
                    }
                    break;
                case CHANGE:
                    result.add(
                            (position1 + (oldLines.size() > 1 ? "," + (position1 + oldLines.size() - 1) : "")) + "c" + position2
                                    + (newLines.size() > 1 ? "," + (position2 + newLines.size() - 1) : "")
                    );
                    for (String line : oldLines) {
                        result.add("< " + line);
                    }
                    result.add("---");
                    for (String line : newLines) {
                        result.add("> " + line);
                    }
                    break;
            }
        }

        return new OutputCompareDto(result);
    }
}
