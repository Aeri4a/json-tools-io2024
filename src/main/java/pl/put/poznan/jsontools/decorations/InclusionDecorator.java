package pl.put.poznan.jsontools.decorations;

import pl.put.poznan.jsontools.types.IJsonObject;
import pl.put.poznan.jsontools.types.JsonObject;

import java.util.List;
import java.util.Map;

public class InclusionDecorator extends JsonDecorator {
    public InclusionDecorator(IJsonObject jsonObject, List<String> keys) {
        super(jsonObject, keys);
    }
    @Override
    public void setValues(Map<String, Object> values) {
        super.setValues(filter(values));
    }
    private Map<String, Object> filter(Map<String, Object> values) {
        for (String key : keys) {
            values.remove(key);
        }
        return values;
    }
}
