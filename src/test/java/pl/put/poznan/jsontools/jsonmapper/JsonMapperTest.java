package pl.put.poznan.jsontools.jsonmapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.put.poznan.jsontools.types.JsonDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JsonMapperTest {

    private ObjectMapper mockObjectMapper;

    @BeforeEach
    void setUp() {
        mockObjectMapper = mock(ObjectMapper.class);
    }

    @Test
    void shouldThrowIfStringIsNull() throws JsonProcessingException {
        when(mockObjectMapper.readValue(anyString(), any(TypeReference.class)))
                .thenThrow(JsonProcessingException.class);

        var jsonMapper = new JsonMapper(mockObjectMapper);

        var jsonDto = new JsonDto("{hejo", null, null);

        assertThrows(JsonProcessingException.class, () -> jsonMapper.toJsonObject(jsonDto));
        verify(mockObjectMapper, times(1)).readValue(eq(jsonDto.jsonString()), any(TypeReference.class));
    }

    @Test
    void shouldConvertWhenStringIsCorrect() throws JsonProcessingException {
        var jsonMapper = new JsonMapper(mockObjectMapper);

        var jsonDto = new JsonDto("{\n\t\"name\": \"Alice\",\n\t\"age\": 25,\n\t\"city\": \"Poznań\"\n}", null, null);

        assertDoesNotThrow(() -> jsonMapper.toJsonObject(jsonDto));
        verify(mockObjectMapper, times(1)).readValue(eq(jsonDto.jsonString()), any(TypeReference.class));
    }

    @Test
    void shouldChangeBehaviorWithInclusionDecorator() throws JsonProcessingException {
        when(mockObjectMapper.readValue(anyString(), any(TypeReference.class)))
                .thenReturn(Map.ofEntries(
                        Map.entry("name", "Alice"),
                        Map.entry("age", 15),
                        Map.entry("city", "Poznań")
                ));

        var jsonMapper = new JsonMapper(mockObjectMapper);

        var jsonDto = new JsonDto("{\n\t\"name\": \"Alice\",\n\t\"age\": 25,\n\t\"city\": \"Poznań\"\n}", List.of("name"), null);

        assertDoesNotThrow(() -> jsonMapper.toJsonObject(jsonDto));
        verify(mockObjectMapper, times(1)).readValue(eq(jsonDto.jsonString()), any(TypeReference.class));

        assertTrue(jsonMapper.toJsonObject(jsonDto).getValues().containsKey("name"));
        assertFalse(jsonMapper.toJsonObject(jsonDto).getValues().containsKey("age"));
        assertFalse(jsonMapper.toJsonObject(jsonDto).getValues().containsKey("city"));
    }

    @Test
    void shouldChangeBehaviorWithExclusionDecorator() throws JsonProcessingException {
        var returnMap = new HashMap<String, Object>();
        returnMap.put("name", "Alice");
        returnMap.put("age", 15);
        returnMap.put("city", "Poznań");
        when(mockObjectMapper.readValue(anyString(), any(TypeReference.class)))
                .thenReturn(returnMap);

        var jsonMapper = new JsonMapper(mockObjectMapper);

        var jsonDto = new JsonDto("{\n\t\"name\": \"Alice\",\n\t\"age\": 25,\n\t\"city\": \"Poznań\"\n}", null, List.of("name"));

        assertDoesNotThrow(() -> jsonMapper.toJsonObject(jsonDto));
        verify(mockObjectMapper, times(1)).readValue(eq(jsonDto.jsonString()), any(TypeReference.class));

        assertFalse(jsonMapper.toJsonObject(jsonDto).getValues().containsKey("name"));
        assertTrue(jsonMapper.toJsonObject(jsonDto).getValues().containsKey("age"));
        assertTrue(jsonMapper.toJsonObject(jsonDto).getValues().containsKey("city"));
    }

    @Test
    void shouldChangeBehaviorWithBothDecorators() throws JsonProcessingException {
        var returnMap = new HashMap<String, Object>();
        returnMap.put("name", "Alice");
        returnMap.put("age", 15);
        returnMap.put("city", "Poznań");
        when(mockObjectMapper.readValue(anyString(), any(TypeReference.class)))
                .thenReturn(returnMap);

        var jsonMapper = new JsonMapper(mockObjectMapper);

        var jsonDto = new JsonDto("{\n\t\"name\": \"Alice\",\n\t\"age\": 25,\n\t\"city\": \"Poznań\"\n}", List.of("name", "age"), List.of("name"));

        assertDoesNotThrow(() -> jsonMapper.toJsonObject(jsonDto));
        verify(mockObjectMapper, times(1)).readValue(eq(jsonDto.jsonString()), any(TypeReference.class));

        assertFalse(jsonMapper.toJsonObject(jsonDto).getValues().containsKey("name"));
        assertTrue(jsonMapper.toJsonObject(jsonDto).getValues().containsKey("age"));
        assertFalse(jsonMapper.toJsonObject(jsonDto).getValues().containsKey("city"));
    }

}
