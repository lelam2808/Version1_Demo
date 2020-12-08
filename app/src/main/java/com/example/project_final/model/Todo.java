package com.example.project_final.model;

public class Todo {
    private int id, status;
    private String task;

    private String deadline;

    public Todo() {
    }

    public Todo(int id, int status, String task, String deadline) {
        this.id = id;
        this.status = status;
        this.task = task;
        this.deadline = deadline;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}

