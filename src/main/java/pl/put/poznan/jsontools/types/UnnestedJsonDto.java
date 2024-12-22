package pl.put.poznan.jsontools.types;

import java.util.List;

/**
 * Dodatkowy obiekt DTO wychodzący z aplikacji po użyciu endpointu /unnest
 *
 * @param UnnestedJsons pole wymagane zawierające listę struktur wyciągniętych z zagnieżdżonego JSONa
 */
public record UnnestedJsonDto(List<JsonDto> UnnestedJsons) {
}
