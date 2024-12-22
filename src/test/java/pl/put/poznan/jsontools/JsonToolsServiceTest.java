package pl.put.poznan.jsontools;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.put.poznan.jsontools.exceptions.InvalidInputException;
import pl.put.poznan.jsontools.jsonmapper.JsonMapper;
import pl.put.poznan.jsontools.types.JsonDto;

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

}
