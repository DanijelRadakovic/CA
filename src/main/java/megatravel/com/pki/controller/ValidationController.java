package megatravel.com.pki.controller;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * JSON Schema validator
 */
public class ValidationController {

    private final String SCHEMA_PATH_PREFIX = "/static/schemes/";

    void validateJSON(String jsonString, String schema_file) throws IOException, ValidationException {
        InputStream inputStream = new ClassPathResource(SCHEMA_PATH_PREFIX + schema_file).getInputStream();
        Schema schema = SchemaLoader.load(new JSONObject(new JSONTokener(inputStream)));
        schema.validate(new JSONObject(jsonString));
    }

    public String getSCHEMA_PATH_PREFIX() {
        return SCHEMA_PATH_PREFIX;
    }
}