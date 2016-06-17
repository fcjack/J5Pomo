package me.jackson.j5pomo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericDao<T extends IModel> implements IDatabase<T> {
    private SQLiteDatabase db;
    private String tableName;

    public GenericDao(Context context, String tableName) {
        DataBaseHelper dbHelper = new DataBaseHelper(context);
        db = dbHelper.getWritableDatabase();
        this.tableName = tableName;
    }

    @Override
    public void insert(T obj) {
        db.insert(tableName, null, getContentValues(obj));
    }

    @Override
    public void update(T obj) {
        db.update(tableName, getContentValues(obj), "_id = ?", new String[]{obj.getId().toString()});
    }

    @Override
    public void delete(T obj) {
        db.delete(tableName, "_id = ?", new String[]{obj.getId().toString()});
    }

    @Override
    public T find(Integer id) {
        Cursor cursor = db.query(tableName, null, "_id = ?", new String[]{id.toString()}, null, null, "_id DESC");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return getObjectFromCursor(cursor);
        }

        return null;
    }

    @Override
    public List<T> findAll() {
        Cursor cursor = db.query(tableName, null, null, null, null, null, "_id ASC");
        List<T> list = null;

        if (cursor.getCount() > 0) {
            list = new ArrayList<>();
            cursor.moveToFirst();

            do {
                list.add(getObjectFromCursor(cursor));
            } while (cursor.moveToNext());
        }

        return list;

    }

    public abstract ContentValues getContentValues(T obj);

    public abstract T getObjectFromCursor(Cursor cursor);
}
