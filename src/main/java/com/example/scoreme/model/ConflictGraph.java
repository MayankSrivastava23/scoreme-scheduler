package com.example.scoreme.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConflictGraph {
    private Map<String, Set<String>> adjacencyList;

    public boolean hasConflict(String t1, String t2) {
        return adjacencyList.getOrDefault(t1, Set.of()).contains(t2);
    }

    public int getDegree(String id) {
        return adjacencyList.getOrDefault(id, Set.of()).size();
    }
}
