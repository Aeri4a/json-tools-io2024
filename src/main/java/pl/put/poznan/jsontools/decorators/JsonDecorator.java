package pl.put.poznan.jsontools.decorators;

import pl.put.poznan.jsontools.types.IJsonObject;

import java.util.List;
import java.util.Map;

/**
 * Klasa abstrakcyjna reprezentująca podstawowy dekorator dla obiektów JSON.
 * <p>
 * Implementuje interfejs {@link IJsonObject} i umożliwia rozszerzanie funkcjonalności
 * obiektów JSON poprzez wzorzec projektowy Decorator.
 * Dekorator przechowuje referencję do dekorowanego obiektu JSON oraz listę kluczy
 * potrzebnych do określonej operacji (np. filtrowania).
 *
 * <p>
 * Klasy dziedziczące muszą implementować własne specyficzne operacje na danych JSON.
 */
public abstract class JsonDecorator implements IJsonObject {
    /**
     * Obiekt JSON, który jest dekorowany.
     */
    protected IJsonObject jsonObject;
    /**
     * Lista kluczy uwzględniona do filtracji w konkretnych dekoratorach.
     */
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
