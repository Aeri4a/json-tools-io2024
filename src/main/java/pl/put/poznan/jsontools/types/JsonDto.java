package pl.put.poznan.jsontools.types;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Główny obiekt DTO przychodzący do aplikacji i z niej wychodzący
 *
 * @param jsonString pole wymagane zawierające łańcuch znaków do dalszego przetwarzania
 * @param includeKeys pole opcjonalne zawierające listę wartości kluczy, które mają być uwzględniane przy wyniku
 * @param excludeKeys pole opcjonalne zawierające listę wartości kluczy, które nie mają być uwzględniane przy wyniku
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record JsonDto(String jsonString, List<String> includeKeys, List<String> excludeKeys) {
}
