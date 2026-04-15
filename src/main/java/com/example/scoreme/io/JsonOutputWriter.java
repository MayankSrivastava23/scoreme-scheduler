package com.example.scoreme.io;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class JsonOutputWriter {
    public static void write(String path, Object obj) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), obj);
    }
}
