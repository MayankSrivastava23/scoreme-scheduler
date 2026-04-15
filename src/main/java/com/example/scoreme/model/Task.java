package com.example.scoreme.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private String id;
    private ResourceVector resource;
    private double weight;
    private int startWindow;
    private int endWindow;
}
