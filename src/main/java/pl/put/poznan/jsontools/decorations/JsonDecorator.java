package pl.put.poznan.jsontools.decorations;

import pl.put.poznan.jsontools.types.IJsonObject;

import java.util.List;
import java.util.Map;

public abstract class JsonDecorator implements IJsonObject {
    private IJsonObject jsonObject;
    protected List<String> keys;
    JsonDecorator(IJsonObject jsonObject, List<String> keys) {
        this.jsonObject = jsonObject;
        this.keys = keys;
    }
    @Override
    public Map<String, Object> getValues() {
        return jsonObject.getValues();
    }

    @Override
    public void setValues(Map<String, Object> values) {
        jsonObject.setValues(values);
    }

    @Override
    public boolean equals(Object o) {
       return jsonObject.equals(o);
    }

    @Override
    public String toString() {
        return jsonObject.toString();
    }
}
