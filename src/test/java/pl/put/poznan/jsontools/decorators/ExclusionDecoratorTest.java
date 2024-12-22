package pl.put.poznan.jsontools.decorators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.put.poznan.jsontools.types.IJsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExclusionDecoratorTest {
    IJsonObject jsonObject;

    @BeforeEach
    void setUp() {
        jsonObject = mock(IJsonObject.class);
    }

    @Test
    void shouldExcludeTwoKeys() {
        List<String> excludedKeys = new ArrayList<>(List.of("a", "b"));

        HashMap<String, Object> map = new HashMap<>(Map.of("a", "vA", "b", "vB", "c", "vC"));
        when(jsonObject.getValues()).thenReturn(map);

        IJsonObject newJsonObject = new ExclusionDecorator(jsonObject, excludedKeys);

        assertEquals(newJsonObject.getValues(), Map.of("c", "vC"));
    }

    @Test
    void shouldNotExcludeAnyKeyWhichNotContains() {
        List<String> excludedKeys = new ArrayList<>(List.of("a", "b"));

        HashMap<String, Object> map = new HashMap<>(Map.of("c", "vC", "d", "vD", "e", "vE"));
        when(jsonObject.getValues()).thenReturn(map);

        IJsonObject newJsonObject = new ExclusionDecorator(jsonObject, excludedKeys);

        assertEquals(newJsonObject.getValues(), Map.of("c", "vC", "d", "vD", "e", "vE"));
    }
}