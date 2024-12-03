package pl.put.poznan.jsontools;

import org.springframework.stereotype.Service;

@Service
public class JsonToolsService {

    public String minify(String inputJson) {
        return inputJson.toLowerCase();
    }

    public String format(String inputJson) {
        return inputJson.toUpperCase();
    }
}
