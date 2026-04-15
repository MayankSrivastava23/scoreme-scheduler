package com.example.scoreme.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Assignment {
    private Map<String, Integer> taskToSlot = new HashMap<>();
}
