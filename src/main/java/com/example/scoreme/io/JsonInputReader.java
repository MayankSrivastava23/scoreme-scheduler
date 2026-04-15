package com.example.scoreme.io;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class JsonInputReader {
    public static <T> T read(String path, Class<T> clazz) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(path), clazz);
    }
}
