package ui.primeq.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    private static final String CHARSET = "UTF-8";

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void serializeObjectToJsonFile(Path jsonFile, Config config) throws IOException{
        Files.write(jsonFile, toJsonString(config).getBytes(CHARSET));
    }

    public static String toJsonString(Config instance) throws JsonProcessingException{
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(instance);
    }

    public static Config deserializeObjectFromJsonFile(Path jsonFile, Config classOfObjectToDeserialize)
            throws IOException {
        return fromJsonString(readFromFile(jsonFile), classOfObjectToDeserialize);
    }

    public static String readFromFile(Path file) throws IOException {
        return new String(Files.readAllBytes(file), CHARSET);
    }

    public static Config fromJsonString(String json, Config config) throws IOException {
        return objectMapper.readValue(json, config.getClass());
    }
}