# 🚀Scoreme Task Scheduler

## 📌 Project Description

**scoreme-scheduler** is a Spring Boot-based scheduling system that assigns tasks to fixed time slots while satisfying multiple constraints.
The system is designed to solve a constrained task assignment problem using a custom heuristic scheduling approach implemented in Java.

It handles:

- Task conflict constraints using a graph-based model
- Resource constraints (CPU, RAM, GPU, Network capacity)
- SLA time window constraints for each task
- Slot capacity limitations
- Cost-based optimization using a penalty function

It uses a **custom heuristic scheduling algorithm (PRCDSATScheduler)** inspired by DSATUR + greedy local search optimization.

---

## ✨ Features

📊 Conflict-aware scheduling using adjacency list graph model  
⚙️ Multi-dimensional resource allocation (CPU, RAM, GPU, Network)  
⏱ SLA window-based task assignment  
🧠 Greedy priority-based scheduling (degree, SLA window, weight)  
🔁 Local search optimization using task swaps  
📉 Custom penalty function including:
- Delay cost (weight × slot index)
- Load balancing across slots
- SLA risk penalty
- Resource utilization imbalance  


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
