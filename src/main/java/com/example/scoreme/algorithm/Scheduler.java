package com.example.scoreme.algorithm;

import com.example.scoreme.model.Instance;
import com.example.scoreme.model.ScheduleResult;

public interface Scheduler {
    ScheduleResult schedule(Instance instance);
}
