package pl.put.poznan.jsontools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.jsontools.dto.JsonDto;

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
        logger.debug(inputJson.toString());

        return ResponseEntity.ok(new JsonDto(jsonToolsService.minify(inputJson.jsonString())));
    }

    @PostMapping("/format")
    public ResponseEntity<JsonDto> formatJson(@RequestBody JsonDto inputJson) {
        logger.debug(inputJson.toString());

        return ResponseEntity.ok(new JsonDto(jsonToolsService.format(inputJson.jsonString())));
    }
}
