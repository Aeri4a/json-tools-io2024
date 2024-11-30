package pl.put.poznan.jsontools;

import java.util.Map;
import java.util.Objects;

public class JsonObject {
    private Map<String, Object> values;

    public JsonObject(Map<String, Object> values) {
        this.values = values;
    }

    public Map<String, Object> getValues() {
        return values;
    }

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
