package com.task.solutiondeveloper.main.model;

public class Todo{
    private String task;
    private Boolean completed;

    public Todo(){

    }

    public Todo(String task, Boolean completed){
        this.task = task;
        this.completed = completed;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

}