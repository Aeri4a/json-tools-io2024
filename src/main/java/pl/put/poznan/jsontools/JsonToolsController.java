package pl.put.poznan.jsontools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/json")
public class JsonToolsController {

    private static final Logger logger = LoggerFactory.getLogger(JsonToolsController.class);
    private final JsonToolsService jsonToolsService;

    public JsonToolsController(JsonToolsService jsonToolsService) {
        this.jsonToolsService = jsonToolsService;
    }

    @PostMapping("/minify")
    public ResponseEntity<String> minifyJson(@RequestBody String inputJson) {
        logger.debug(inputJson);

        return ResponseEntity.ok(jsonToolsService.minify(inputJson));
    }

    @PostMapping("/format")
    public ResponseEntity<String> formatJson(@RequestBody String inputJson) {
        logger.debug(inputJson);

        return ResponseEntity.ok(jsonToolsService.format(inputJson));
    }
}
