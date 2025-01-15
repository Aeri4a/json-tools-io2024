package pl.put.poznan.jsontools;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.put.poznan.jsontools.exceptions.InvalidInputException;
import pl.put.poznan.jsontools.jsonmapper.JsonMapper;
import pl.put.poznan.jsontools.types.InputCompareDto;
import pl.put.poznan.jsontools.types.JsonDto;
import pl.put.poznan.jsontools.types.JsonObject;
import pl.put.poznan.jsontools.types.UnnestedJsonDto;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JsonToolsServiceTest {

    private JsonMapper mockJsonMapper;

    @BeforeEach
    void setUp() {
        this.mockJsonMapper = mock(JsonMapper.class);
    }

    @Test
    void minifyShouldThrowIfJsonIsIncorrect() throws JsonProcessingException {
        when(mockJsonMapper.toJsonObject(any(JsonDto.class)))
                .thenThrow(JsonProcessingException.class);

        var jsonDto = new JsonDto("{yooo", null, null);

        var jsonToolsService = new JsonToolsService(mockJsonMapper);

        assertThrows(InvalidInputException.class, () -> jsonToolsService.minify(jsonDto));
        verify(mockJsonMapper, times(1)).toJsonObject(eq(jsonDto));
        verify(mockJsonMapper, times(0)).toJsonDto(any());
    }

    @Test
    void minifyShouldProceedIfJsonIsCorrect() throws JsonProcessingException {
        var jsonDto = new JsonDto("{\"hello\": \"world\"}", null, null);

        var jsonToolsService = new JsonToolsService(mockJsonMapper);

        assertDoesNotThrow(() -> jsonToolsService.minify(jsonDto));
        verify(mockJsonMapper, times(1)).toJsonObject(eq(jsonDto));
        verify(mockJsonMapper, times(1)).toJsonDto(any());
    }

    @Test
    void formatShouldThrowIfJsonIsIncorrect() throws JsonProcessingException {
        when(mockJsonMapper.toJsonObject(any(JsonDto.class)))
                .thenThrow(JsonProcessingException.class);

        var jsonDto = new JsonDto("{yooo", null, null);

        var jsonToolsService = new JsonToolsService(mockJsonMapper);

        assertThrows(InvalidInputException.class, () -> jsonToolsService.format(jsonDto));
        verify(mockJsonMapper, times(1)).toJsonObject(eq(jsonDto));
        verify(mockJsonMapper, times(0)).toJsonDto(any());
    }

    @Test
    void formatShouldProceedIfJsonIsCorrect() throws JsonProcessingException {
        var jsonDto = new JsonDto("{\"hello\": \"world\"}", null, null);

        var jsonToolsService = new JsonToolsService(mockJsonMapper);

        assertDoesNotThrow(() -> jsonToolsService.format(jsonDto));
        verify(mockJsonMapper, times(1)).toJsonObject(eq(jsonDto));
        verify(mockJsonMapper, times(1)).toJsonDtoWithFormat(any());
    }

    @Test
    void compareShouldReturnEmptyListIfTwoStringAreEqual() {
        var input = new InputCompareDto("hello", "hello");

        var jsonToolsService = new JsonToolsService();

        assertTrue(jsonToolsService.compare(input).differences().isEmpty());
    }

    @Test
    void compareShouldReturnOneChangeOneLineEqualLength() {
        var input = new InputCompareDto("olleh", "hello");

        var jsonToolsService = new JsonToolsService();

        assertEquals(List.of("1c1", "< olleh", "---", "> hello"), jsonToolsService.compare(input).differences());
    }

    @Test
    void compareShouldReturnOneChangeIfOneLineIsEmpty() {
        var input = new InputCompareDto("abc\ncba", "\ncba");

        var jsonToolsService = new JsonToolsService();

        assertEquals(List.of("1c1", "< abc", "---", "> "), jsonToolsService.compare(input).differences());
    }

    @Test
    void compareShouldReturnOneDeleteAndOneAdditionIfLinesAreEmptyAlternately() {
        var input = new InputCompareDto("\ncba", "cba\n");

        var jsonToolsService = new JsonToolsService();

        assertEquals(List.of("1d0", "< ", "2a2", "> "), jsonToolsService.compare(input).differences());
    }

    @Test
    void compareShouldReturnOneChangeIfOneStringIsEmpty() {
        var input = new InputCompareDto("hello", "");

        var jsonToolsService = new JsonToolsService();

        assertEquals(List.of("1c1", "< hello", "---", "> "), jsonToolsService.compare(input).differences());
    }


    @Test
    void compareShouldReturnEmptyListIfTwoStringsAreEmpty() {
        var input = new InputCompareDto("", "");

        var jsonToolsService = new JsonToolsService();

        assertTrue(jsonToolsService.compare(input).differences().isEmpty());
    }

    @Test
    void shouldUnnestExtractNestedValues() throws JsonProcessingException {
        var jsonToolsService = new JsonToolsService();

        var jsonDto = new JsonDto("{\"first\":{\"f1\":\"vf1\",\"f2\":\"vf2\"},\"second\":{\"s1\":\"vs1\"}}", null, null);

        var resultDto = new UnnestedJsonDto(List.of(
                new JsonDto("{\"f1\":\"vf1\",\"f2\":\"vf2\"}", null, null),
                new JsonDto("{\"s1\":\"vs1\"}", null, null),
                new JsonDto("{}", null, null)
                ));

        assertEquals(resultDto, jsonToolsService.unnest(jsonDto));
    }

    @Test
    void shouldUnnestThrowInvalidInputExceptionIfJsonIsIncorrect() throws JsonProcessingException {
        var jsonDto = new JsonDto("{\"first\":{\"f1\":\"vf1,\"f2\":\"vf2\"},\"second\":{s1\":\"vs1\"}}", null, null);

        var jsonToolsService = new JsonToolsService();

        assertThrows(InvalidInputException.class, () -> jsonToolsService.unnest(jsonDto));
    }
}
