package pl.put.poznan.jsontools.types;

import java.util.Map;
import java.util.Objects;

/**
 * Struktura przechowująca JSONa w postaci mapy klucz-wartość
 */
public class JsonObject implements IJsonObject {
    private Map<String, Object> values;

    public JsonObject(Map<String, Object> values) {
        this.values = values;
    }

    @Override
    public Map<String, Object> getValues() {
        return values;
    }

    @Override
    public void setValues(Map<String, Object> values) {
        this.values = values;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        JsonObject that = (JsonObject) o;
        return Objects.equals(values, that.values);
    }

    @Override
    public String toString() {
        return values.toString();
    }
}
