package pl.put.poznan.jsontools.decorations;


import pl.put.poznan.jsontools.types.IJsonObject;

import java.util.List;
import java.util.Map;

public class ExclusionDecorator extends JsonDecorator {
    public ExclusionDecorator(IJsonObject jsonObject, List<String> keys) {
        super(jsonObject, keys);
        filter();
    }

    private void filter() {
        Map<String, Object> map = this.jsonObject.getValues();

        for (String key : keys) {
            map.remove(key);
        }
    }
}
