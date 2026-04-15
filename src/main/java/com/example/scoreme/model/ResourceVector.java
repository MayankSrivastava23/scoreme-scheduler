package com.example.scoreme.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceVector {
    private double cpu;
    private double ram;
    private double gpu;
    private double network;

    public boolean fits(ResourceVector capacity) {
        return cpu <= capacity.cpu &&
                ram <= capacity.ram &&
                gpu <= capacity.gpu &&
                network <= capacity.network;
    }

    public void add(ResourceVector other) {
        cpu += other.cpu;
        ram += other.ram;
        gpu += other.gpu;
        network += other.network;
    }

    public void subtract(ResourceVector other) {
        cpu -= other.cpu;
        ram -= other.ram;
        gpu -= other.gpu;
        network -= other.network;
    }

    public ResourceVector copy() {
        return new ResourceVector(cpu, ram, gpu, network);
    }
}
