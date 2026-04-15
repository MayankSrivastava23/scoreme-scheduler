package com.example.scoreme.algorithm;

import com.example.scoreme.model.*;

import java.util.*;

public class PRCDSATScheduler implements Scheduler{

    private static final double ALPHA = 10.0;
    private static final double BETA = 5.0;

    @Override
    public ScheduleResult schedule(Instance instance) {
        long startTime = System.currentTimeMillis();
        Assignment assignment = new Assignment();
        ScheduleResult result = new ScheduleResult();

        //initialize slots
        for (Slot s : instance.getSlots()) {
            s.getTasks().clear();
            s.setUsed(new ResourceVector(0,0,0,0));
        }

        List<Task> tasks = new ArrayList<>(instance.getTasks());
        tasks.sort((a, b) -> {
            int degDiff = instance.getConflictGraph().getDegree(b.getId())
                    - instance.getConflictGraph().getDegree(a.getId());

            if (degDiff != 0) return degDiff;
            int rangeA = a.getEndWindow() - a.getStartWindow();
            int rangeB = b.getEndWindow() - b.getStartWindow();
            if (rangeA != rangeB) return Integer.compare(rangeA, rangeB);
            return Double.compare(b.getWeight(), a.getWeight());
        });

        for (Task task : tasks) {
            int bestSlot = -1;
            double bestScore = Double.MAX_VALUE;

            for (int s = task.getStartWindow(); s <= task.getEndWindow(); s++) {
                if (s < 0 || s >= instance.getSlots().size()) continue;
                Slot slot = instance.getSlots().get(s);
                if (!slot.canAssign(task)) continue;
                if (hasConflict(instance, slot, task)) continue;
                double score = computeCost(task, slot, s);
                if (score < bestScore) {
                    bestScore = score;
                    bestSlot = s;
                }
            }

            if (bestSlot == -1) {
                result.setFeasible(false);
                result.setViolationReason("Could not assign task: " + task.getId());
                result.setAssignment(assignment.getTaskToSlot());
                result.setRuntimeMs(System.currentTimeMillis() - startTime);
                return result;
            }
            assign(instance, assignment, task, bestSlot);
        }

        improveAssignment(instance, assignment);
        result.setFeasible(true);
        result.setAssignment(assignment.getTaskToSlot());
        result.setPenalty(calculatePenalty(instance, assignment));
        result.setRuntimeMs(System.currentTimeMillis() - startTime);
        result.setViolationReason("");
        return result;
    }

    private void assign(Instance instance, Assignment assignment, Task task, int slotId) {
        Slot slot = instance.getSlots().get(slotId);
        slot.assign(task);
        assignment.getTaskToSlot().put(task.getId(), slotId);
    }

    private void unassign(Instance instance, Assignment assignment, Task task, int slotId) {
        Slot slot = instance.getSlots().get(slotId);
        slot.remove(task);
        assignment.getTaskToSlot().remove(task.getId());
    }

    //Check Conflict
    private boolean hasConflict(Instance instance, Slot slot, Task task) {
        for (Task t : slot.getTasks()) {
            if (instance.getConflictGraph().hasConflict(task.getId(), t.getId())) {
                return true;
            }
        }
        return false;
    }

    //Cost Function
    private double computeCost(Task task, Slot slot, int s) {
        double delayCost = task.getWeight() * s;
        double newCpu = slot.getUsed().getCpu() + task.getResource().getCpu();
        double cpuUtil = newCpu / slot.getCapacity().getCpu();
        double loadCost = Math.pow(cpuUtil - 0.7, 2);
        int range = task.getEndWindow() - task.getStartWindow() + 1;
        double slaRisk = (double) (s - task.getStartWindow()) / range;
        return delayCost + ALPHA * loadCost + BETA * slaRisk;
    }

    private void improveAssignment(Instance instance, Assignment assignment) {
        Random rand = new Random();
        List<Task> tasks = instance.getTasks();
        for (int i = 0; i < 100; i++) {

            Task t1 = tasks.get(rand.nextInt(tasks.size()));
            Task t2 = tasks.get(rand.nextInt(tasks.size()));
            Integer s1 = assignment.getTaskToSlot().get(t1.getId());
            Integer s2 = assignment.getTaskToSlot().get(t2.getId());

            if (s1 == null || s2 == null || s1.equals(s2)) continue;
            unassign(instance, assignment, t1, s1);
            unassign(instance, assignment, t2, s2);

            boolean ok1 = canPlace(instance, t1, s2);
            boolean ok2 = canPlace(instance, t2, s1);

            if (ok1 && ok2) {
                assign(instance, assignment, t1, s2);
                assign(instance, assignment, t2, s1);
                if (!isValid(instance, assignment)) {
                    unassign(instance, assignment, t1, s2);
                    unassign(instance, assignment, t2, s1);
                    assign(instance, assignment, t1, s1);
                    assign(instance, assignment, t2, s2);
                }

            } else {
                assign(instance, assignment, t1, s1);
                assign(instance, assignment, t2, s2);
            }
        }
    }

    private boolean canPlace(Instance instance, Task task, int slotId) {
        Slot slot = instance.getSlots().get(slotId);
        if (slotId < task.getStartWindow() || slotId > task.getEndWindow()) return false;
        if (!slot.canAssign(task)) return false;
        return !hasConflict(instance, slot, task);
    }

    //Validation
    private boolean isValid(Instance instance, Assignment assignment) {
        Map<Integer, List<Task>> slotMap = new HashMap<>();
        for (Task task : instance.getTasks()) {
            int s = assignment.getTaskToSlot().get(task.getId());
            if (s < task.getStartWindow() || s > task.getEndWindow()) return false;
            slotMap.computeIfAbsent(s, k -> new ArrayList<>()).add(task);
        }

        for (Map.Entry<Integer, List<Task>> entry : slotMap.entrySet()) {
            Slot slot = instance.getSlots().get(entry.getKey());
            ResourceVector used = new ResourceVector(0, 0, 0, 0);
            for (Task t : entry.getValue()) {
                used.add(t.getResource());
                if (!used.fits(slot.getCapacity())) return false;
                for (Task other : entry.getValue()) {
                    if (t == other) continue;
                    if (instance.getConflictGraph().hasConflict(t.getId(), other.getId())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //Penalty Calculation
    private double calculatePenalty(Instance instance, Assignment assignment) {
        double penalty = 0;
        for (Task task : instance.getTasks()) {
            int slot = assignment.getTaskToSlot().get(task.getId());
            penalty += task.getWeight() * slot;
        }
        for (Slot slot : instance.getSlots()) {

            double util = slot.getUsed().getCpu() / slot.getCapacity().getCpu();
            penalty += ALPHA * Math.pow(util - 0.7, 2);
        }
        for (Task task : instance.getTasks()) {

            int s = assignment.getTaskToSlot().get(task.getId());
            int range = task.getEndWindow() - task.getStartWindow() + 1;

            penalty += BETA * (double) (s - task.getStartWindow()) / range;
        }
        return penalty;
    }
}
