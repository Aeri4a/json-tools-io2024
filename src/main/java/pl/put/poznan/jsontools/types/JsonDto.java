package pl.put.poznan.jsontools.types;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record JsonDto(String jsonString, List<String> includeKeys, List<String> excludeKeys) {
}
