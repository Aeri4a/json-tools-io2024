package pl.put.poznan.jsontools.types;

import java.util.Map;

public interface IJsonObject {
    Map<String, Object> getValues();
    void setValues(Map<String, Object> values);
    boolean equals(Object o);
    String toString();
}
