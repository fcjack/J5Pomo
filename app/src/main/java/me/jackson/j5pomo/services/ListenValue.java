package me.jackson.j5pomo.services;

/**
 * Created by bruno on 18/05/2016.
 */
public interface ListenValue {

    void newValue(String value);

    void notifyEndPomodoro(Integer taskId);

    void notifyCompleteTask(Integer taskId);

}
