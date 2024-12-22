package pl.put.poznan.jsontools;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.put.poznan.jsontools.exceptions.InvalidInputException;
import pl.put.poznan.jsontools.jsonmapper.JsonMapper;
import pl.put.poznan.jsontools.types.InputCompareDto;
import pl.put.poznan.jsontools.types.JsonDto;

import java.util.List;

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

        assertEquals(0, jsonToolsService.compare(input).differentLines().size());
    }

    @Test
    void compareShouldReturnOneDifferenceOneLineEqualLength() {
        var input = new InputCompareDto("olleh", "hello");

        var jsonToolsService = new JsonToolsService();

        assertEquals(List.of(1), jsonToolsService.compare(input).differentLines());
    }

    @Test
    void compareShouldReturnOneDifferenceIfOneLineIsEmpty() {
        var input = new InputCompareDto("abc\ncba", "\ncba");

        var jsonToolsService = new JsonToolsService();

        assertEquals(List.of(1), jsonToolsService.compare(input).differentLines());
    }

    @Test
    void compareShouldReturnTwoDifferencesIfLinesAreEmptyAlternately() {
        var input = new InputCompareDto("\ncba", "cba\n");

        var jsonToolsService = new JsonToolsService();

        assertEquals(List.of(1, 2), jsonToolsService.compare(input).differentLines());
    }

    @Test
    void compareShouldReturnEmptyListIfTwoStringsAreEmpty() {
        var input = new InputCompareDto(null, null);

        var jsonToolsService = new JsonToolsService();

        assertThrows(InvalidInputException.class, () -> jsonToolsService.compare(input));
    }
}
