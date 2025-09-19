package org.example.scaleaidemo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class JsonService {

    private final ObjectMapper objectMapper;

    public JsonService() {
        this.objectMapper = new ObjectMapper();
    }

    public <T> void writeListToJsonFile(List<T> data, String filePath) throws IOException {
        ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
        writer.writeValue(new File(filePath), data);
    }

    public <T> List<T> readListFromJsonFile(String filePath, Class<T> clazz) throws IOException {
        return objectMapper.readValue(new File(filePath),
            objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
    }

    public <T> void writeObjectToJsonFile(T data, String filePath) throws IOException {
        ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
        writer.writeValue(new File(filePath), data);
    }

    public <T> T readObjectFromJsonFile(String filePath, Class<T> clazz) throws IOException {
        return objectMapper.readValue(new File(filePath), clazz);
    }
}