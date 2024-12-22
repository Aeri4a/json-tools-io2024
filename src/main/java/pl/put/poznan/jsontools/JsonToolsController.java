package pl.put.poznan.jsontools;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.jsontools.types.InputCompareDto;
import pl.put.poznan.jsontools.types.JsonDto;
import pl.put.poznan.jsontools.types.OutputCompareDto;
import pl.put.poznan.jsontools.types.UnnestedJsonDto;

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
    public ResponseEntity<JsonDto> formatJson(@RequestBody JsonDto inputJson, HttpServletRequest request) {
        logger.info("handled request at {}", request.getRequestURL().toString());
        logger.debug("request body at formatJson: {}", inputJson.jsonString());

        JsonDto formattedJson = jsonToolsService.format(inputJson);
        logger.debug("formatted JSON:\n{}", formattedJson.jsonString());
        return ResponseEntity.ok(formattedJson);
    }

    @PostMapping("/compare")
    public ResponseEntity<OutputCompareDto> compareJson(@RequestBody InputCompareDto inputJsons, HttpServletRequest request) {
        logger.info("handled request at {}", request.getRequestURL().toString());
        logger.debug("request body at compareJson: json1={}, json2={}", inputJsons.string1(), inputJsons.string2());

        OutputCompareDto comparisonResult = jsonToolsService.compare(inputJsons);
        logger.debug("comparison result: {}", comparisonResult);
        return ResponseEntity.ok(comparisonResult);
    }

    @PostMapping("/unnest")
    public ResponseEntity<UnnestedJsonDto> unnestJson(@RequestBody JsonDto inputJson, HttpServletRequest request) {
        logger.info("handled request at {}", request.getRequestURL().toString());
        logger.debug("request body at unnestJson: {}", inputJson.jsonString());

        // do it
        UnnestedJsonDto unnestedJson = jsonToolsService.unnest(inputJson);

        return  ResponseEntity.ok(unnestedJson);
    }
}
