package com.example.scoreme.penalty;

import com.example.scoreme.model.Assignment;
import com.example.scoreme.model.Instance;

public interface PenaltyCalculator {
    double calculate(Assignment assignment, Instance instance);
}
