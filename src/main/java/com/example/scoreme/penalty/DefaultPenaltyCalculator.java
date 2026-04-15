package com.example.scoreme.penalty;

import com.example.scoreme.model.Assignment;
import com.example.scoreme.model.Instance;
import com.example.scoreme.model.Slot;
import com.example.scoreme.model.Task;

import java.util.List;

public class DefaultPenaltyCalculator implements PenaltyCalculator {

    private static final double LAMBDA = 10.0;
    private static final double MU = 5.0;
    private static final double GAMMA = 3.0;

    @Override
    public double calculate(Assignment assignment, Instance instance) {

        double penalty = 0.0;

        //Cost Delay
        for (Task task : instance.getTasks()) {
            Integer slot = assignment.getTaskToSlot().get(task.getId());
            if (slot == null) continue;
            penalty += task.getWeight() * slot;
        }

        //Load balancing
        List<Slot> slots = instance.getSlots();
        double[] util = new double[slots.size()];
        double total = 0;
        for (int i = 0; i < slots.size(); i++) {
            Slot s = slots.get(i);
            double cpu = safeDiv(s.getUsed().getCpu(), s.getCapacity().getCpu());
            double ram = safeDiv(s.getUsed().getRam(), s.getCapacity().getRam());
            double gpu = safeDiv(s.getUsed().getGpu(), s.getCapacity().getGpu());
            double net = safeDiv(s.getUsed().getNetwork(), s.getCapacity().getNetwork());
            util[i] = (cpu + ram + gpu + net) / 4.0;
            total += util[i];
        }

        double avg = total / slots.size();
        for (double u : util) {
            penalty += LAMBDA * Math.pow(u - avg, 2);
        }

        //SLA penalty
        for (Task task : instance.getTasks()) {
            Integer slot = assignment.getTaskToSlot().get(task.getId());
            if (slot == null) continue;
            int start = task.getStartWindow();
            int end = task.getEndWindow();
            int range = end - start + 1;
            double slaRisk = Math.max(0.0, (double)(slot - start) / range);
            penalty += MU * slaRisk;
        }

        //GPU fragmentation
        for (Slot s : slots) {
            double gpuUtil = safeDiv(s.getUsed().getGpu(), s.getCapacity().getGpu());
            penalty += GAMMA * (1 - gpuUtil) * (1 - gpuUtil);
        }

        return penalty;
    }

    private double safeDiv(double a, double b) {
        return b == 0 ? 0.0 : a / b;
    }
}
