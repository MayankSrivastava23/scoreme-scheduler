package com.example.scoreme.model;

import lombok.Data;

import java.util.Map;

@Data
public class ScheduleResult {
    private Map<String, Integer> assignment;
    private double penalty;
    private long runtimeMs;
    private boolean feasible;
    private String violationReason;
}
