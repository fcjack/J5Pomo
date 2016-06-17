package me.jackson.j5pomo.model;

import java.io.Serializable;

import me.jackson.j5pomo.database.IModel;

public class Task implements IModel, Serializable {

    private Integer id;
    private String name;
    private String description;
    private Integer pomodoro;
    private Boolean completed;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPomodoro() {
        return pomodoro;
    }

    public void setPomodoro(Integer pomodoro) {
        this.pomodoro = pomodoro;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
