package me.jackson.j5pomo.services;

/**
 * Created by bruno on 18/05/2016.
 */
public interface ServiceNotifier {

    void add(ListenValue obj);

    void notifyValue(Integer value);

}
