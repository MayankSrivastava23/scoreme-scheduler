package com.example.scoreme.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Slot {
    private int id;
    private ResourceVector capacity;
    private ResourceVector used = new ResourceVector(0, 0, 0, 0);
    private List<Task> tasks = new ArrayList<>();

    public Slot(int id, ResourceVector capacity) {
        this.id = id;
        this.capacity = capacity;
    }

    public boolean canAssign(Task task) {
        ResourceVector temp = used.copy();
        temp.add(task.getResource());
        return temp.fits(capacity);
    }

    public void assign(Task task) {
        used.add(task.getResource());
        tasks.add(task);
    }

    public void remove(Task task) {
        used.subtract(task.getResource());
        tasks.remove(task);
    }
}
