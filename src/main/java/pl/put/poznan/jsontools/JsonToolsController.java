package pl.put.poznan.jsontools;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<JsonDto> minifyJson(@RequestBody JsonDto inputJson, HttpServletRequest request) {
        logger.info("handled request at {}", request.getRequestURL().toString());
        logger.debug("request body at minifyJson: {}", inputJson.jsonString());

        JsonDto minifiedJSON = jsonToolsService.minify(inputJson);
        logger.debug("minified JSON: {}", minifiedJSON.jsonString());
        return ResponseEntity.ok(minifiedJSON);
    }

    @PostMapping("/format")
    public ResponseEntity<JsonDto> formatJson(@RequestBody JsonDto inputJson) {
        logger.info("localhost:8080/format");
        logger.debug("request body: {}", inputJson.toString());

        JsonDto formattedJson = jsonToolsService.format(inputJson);
        return ResponseEntity.ok(formattedJson);
    }
}
