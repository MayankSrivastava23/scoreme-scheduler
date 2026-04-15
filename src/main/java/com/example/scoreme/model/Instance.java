package com.example.scoreme.model;

import lombok.Data;

import java.util.List;

@Data
public class Instance {
    private List<Task> tasks;
    private ConflictGraph conflictGraph;
    private List<Slot> slots;
}
