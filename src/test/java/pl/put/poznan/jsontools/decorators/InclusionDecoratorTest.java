package pl.put.poznan.jsontools.decorators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.put.poznan.jsontools.exceptions.InvalidInputException;
import pl.put.poznan.jsontools.types.IJsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

class InclusionDecoratorTest {
    IJsonObject jsonObject;

    @BeforeEach
    void setUp() {
        jsonObject = mock(IJsonObject.class);
    }

    @Test
    void shouldThrowErrorIfKeyDoesNotExist() {
        List<String> includedKeys = new ArrayList<>(List.of("x", "z"));

        HashMap<String, Object> map = new HashMap<>(Map.of("a", "vA", "b", "vB"));
        when(jsonObject.getValues()).thenReturn(map);

        assertThrows(InvalidInputException.class, () -> new InclusionDecorator(jsonObject, includedKeys));
    }

    @Test
    void shouldRemoveNotMentionedKeys() {
        List<String> includedKeys = new ArrayList<>(List.of("a", "b"));

        HashMap<String, Object> map = new HashMap<>(Map.of("a", "vA", "b", "vB"));
        when(jsonObject.getValues()).thenReturn(map);

        IJsonObject newJsonObject = new InclusionDecorator(jsonObject, includedKeys);

        assertEquals(newJsonObject.getValues(), Map.of("a", "vA", "b", "vB"));
    }
}