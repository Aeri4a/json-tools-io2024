package pl.put.poznan.jsontools.decorators;

import pl.put.poznan.jsontools.types.IJsonObject;

import java.util.List;
import java.util.Map;

public abstract class JsonDecorator implements IJsonObject {
    protected IJsonObject jsonObject;
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
}
