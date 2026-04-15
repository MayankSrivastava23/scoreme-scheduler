# 🚀 scoreme-scheduler

## 📌 Project Description

**scoreme-scheduler** is a Spring Boot-based advanced scheduling system designed to solve a complex **multi-constraint task assignment problem** in a distributed compute environment.

The system schedules tasks into fixed time slots while satisfying:

- Conflict constraints (graph-based task conflicts)
- Resource constraints (CPU, RAM, GPU, Network)
- SLA time windows
- Load-aware scheduling via penalty-based optimization
- Optimization of a weighted penalty function

It uses a **custom heuristic scheduling algorithm (PRCDSATScheduler)** inspired by DSATUR + greedy local search optimization.

---

## ✨ Features

📊 Conflict-aware task scheduling using graph constraints  
⚙️ Multi-dimensional resource allocation (CPU, RAM, GPU, Network)  
⏱ SLA window enforcement for each task  
🧠 Heuristic-based scheduling algorithm (degree, weight, SLA window priority)  
🔁 Local search optimization (swap-based improvement)  
📉 Custom penalty function:
- Delay cost minimization  
- Load balancing across slots  
- SLA risk reduction  
- GPU utilization efficiency 

---

## 🛠 Tech Stack

- Java 21
- Spring Boot 3.x
- Maven
- Lombok
- Jackson (JSON processing)

---

## ⚙️ Installation Instructions


2. Build the project
```
mvn clean install
```
4. Run the application
```
mvn spring-boot:run

```

## Folder Structure

```
com.example.scoreme
│
├── algorithm
│   ├── Scheduler.java
│   └── PRCDSATScheduler.java
│
├── model
│   ├── Task.java
│   ├── Slot.java
│   ├── Instance.java
│   ├── Assignment.java
│   ├── ConflictGraph.java
│   ├── ResourceVector.java
│   └── ScheduleResult.java
│
├── penalty
│   ├── PenaltyCalculator.java
│   └── DefaultPenaltyCalculator.java
│
├── io
│   ├── JsonInputReader.java
│   └── JsonOutputWriter.java
│
└── ScoremeApplication.java
```

### 1. Clone the repository
```bash
git clone https://github.com/your-username/scoreme-scheduler.git
cd scoreme-scheduler
