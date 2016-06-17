package me.jackson.j5pomo.database;

import java.util.List;

public interface IDatabase<T> {
    void insert(T obj);

    void update(T obj);

    void delete(T obj);

    T find(Integer id);

    List<T> findAll();
}
