package pl.put.poznan.jsontools.decorators;

import pl.put.poznan.jsontools.exceptions.InvalidInputException;
import pl.put.poznan.jsontools.types.IJsonObject;
import pl.put.poznan.jsontools.types.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementuje konkretny dekorator, który filtruje dane JSON, zostawiając jedynie wskazane klucze <code>keys</code>.
 * <p>
 * Stosowany do obiektu {@link JsonObject} (implementującego interfejs {@link IJsonObject}),
 * a jego zadaniem jest zachowanie z niego wartości związanych z określonymi kluczami.
 */
public class InclusionDecorator extends JsonDecorator {
    public InclusionDecorator(IJsonObject jsonObject, List<String> keys) {
        super(jsonObject, keys);
        filter();
    }

    /**
     * Filtruje obiekt JSON, zostawiając w nim tylko wartości skojarzone z podanymi kluczami z mapy.
     */
    private void filter() {
        Map<String, Object> map = this.jsonObject.getValues();
        Map<String, Object> filteredMap = new HashMap<>();

        for (String key : keys) {
            if (!map.containsKey(key)) {
                throw new InvalidInputException("jsonString nie zawiera w sobie klucza: " + key);
            } else {
                filteredMap.put(key, map.get(key));
            }
        }

        this.jsonObject.setValues(filteredMap);
    }
}
