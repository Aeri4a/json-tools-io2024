package pl.put.poznan.jsontools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.jsontools.exceptions.InvalidInputException;
import pl.put.poznan.jsontools.types.JsonDto;

@RestController
@RequestMapping("/json")
public class JsonToolsController {

    private static final Logger logger = LoggerFactory.getLogger(JsonToolsController.class);
    private final JsonToolsService jsonToolsService;

    public JsonToolsController(JsonToolsService jsonToolsService) {
        this.jsonToolsService = jsonToolsService;
    }

    @PostMapping("/minify")
    public ResponseEntity<JsonDto> minifyJson(@RequestBody JsonDto inputJson) {
        logger.info("localhost:8080/minify");
        logger.debug("request body: {}", inputJson.toString());

        JsonDto minified_json = jsonToolsService.minify(inputJson);
        return ResponseEntity.ok(minified_json);
    }

    @PostMapping("/format")
    public ResponseEntity<JsonDto> formatJson(@RequestBody JsonDto inputJson) {
        logger.info("localhost:8080/format");
        logger.debug("request body: {}", inputJson.toString());

        JsonDto formattedJson = jsonToolsService.format(inputJson);

        if (formattedJson == null)
            return new ResponseEntity<>((JsonDto) null, HttpStatus.BAD_REQUEST);

        return ResponseEntity.ok(jsonToolsService.format(inputJson));
    }
}
