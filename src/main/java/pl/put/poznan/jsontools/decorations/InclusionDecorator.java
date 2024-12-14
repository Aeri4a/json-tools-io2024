package pl.put.poznan.jsontools.decorations;

import pl.put.poznan.jsontools.types.IJsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InclusionDecorator extends JsonDecorator {
    public InclusionDecorator(IJsonObject jsonObject, List<String> keys) {
        super(jsonObject, keys);
        filter();
    }

    private void filter() {
        Map<String, Object> map = this.jsonObject.getValues();
        Map<String, Object> filteredMap = new HashMap<>();

        for (String key : keys) {
            if (!map.containsKey(key)) {
                // TODO: throw error
            } else {
                filteredMap.put(key, map.get(key));
            }
        }

        this.jsonObject.setValues(filteredMap);
    }
}
