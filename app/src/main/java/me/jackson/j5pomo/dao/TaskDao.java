package me.jackson.j5pomo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import me.jackson.j5pomo.database.GenericDao;
import me.jackson.j5pomo.model.Task;

public class TaskDao extends GenericDao<Task> {

    private static final String TABLE_NAME = "tasks";

    public TaskDao(Context context) {
        super(context, TABLE_NAME);
    }

    @Override
    public ContentValues getContentValues(Task obj) {
        ContentValues cv = new ContentValues();
        cv.put("_id", obj.getId());
        cv.put("name", obj.getName());
        cv.put("description", obj.getDescription());
        cv.put("pomodoro", obj.getPomodoro());
        if (obj.getCompleted()) {
            cv.put("completed", 1);
        } else {
            cv.put("completed", 0);
        }

        return cv;
    }

    @Override
    public Task getObjectFromCursor(Cursor cursor) {
        Task task = new Task();
        if (cursor != null) {
            task.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            task.setName(cursor.getString(cursor.getColumnIndex("name")));
            task.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            task.setPomodoro(cursor.getInt(cursor.getColumnIndex("pomodoro")));
            int completed = cursor.getInt(cursor.getColumnIndex("completed"));
            task.setCompleted((completed == 1));
        }

        return task;
    }
}
