package pl.put.poznan.jsontools.decorators;


import pl.put.poznan.jsontools.types.IJsonObject;
import pl.put.poznan.jsontools.types.JsonObject;

import java.util.List;
import java.util.Map;

/**
 * Implementuje konkretny dekorator, który filtruje dane JSON, usuwając wskazane klucze <code>keys</code>.
 * <p>
 * Stosowany do obiektu {@link JsonObject} (implementującego interfejs {@link IJsonObject}),
 * a jego zadaniem jest wykluczenie z niego wartości związanych z określonymi kluczami.
 */
public class ExclusionDecorator extends JsonDecorator {
    public ExclusionDecorator(IJsonObject jsonObject, List<String> keys) {
        super(jsonObject, keys);
        filter();
    }

    /**
     * Filtruje obiekt JSON, usuwając z niego wartości skojarzone z podanymi kluczami z mapy.
     */
    private void filter() {
        Map<String, Object> map = this.jsonObject.getValues();

        for (String key : keys) {
            map.remove(key);
        }
    }
}
