package com.example.sql_lite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "UserInfo";
    private static final String TABLE_NAME = "tbl_user";
    private static final String KEY_NAME = "name";
    private static final String KEY_AGE = "age";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE " + TABLE_NAME + "(" +
                KEY_NAME + " TEXT PRIMARY KEY, " +
                KEY_AGE + " INTEGER)";
        db.execSQL(createUserTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insert(PersonBean personBean) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, personBean.getName());
        values.put(KEY_AGE, personBean.getAge());
        db.insert(TABLE_NAME, null, values);
        db.close(); // tambahkan ini untuk menghindari memory leaks
    }

    public List<PersonBean> selectUserData() {
        ArrayList<PersonBean> userList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {KEY_NAME, KEY_AGE};
        Cursor c = db.query(TABLE_NAME, columns, null, null, null, null, null);
        if (c != null && c.moveToFirst()) {
            do {
                String name = c.getString(c.getColumnIndex(KEY_NAME));
                int age = c.getInt(c.getColumnIndex(KEY_AGE));
                PersonBean personBean = new PersonBean();
                personBean.setName(name);
                personBean.setAge(age);
                userList.add(personBean);
            } while (c.moveToNext());
            c.close(); // tambahkan ini untuk menghindari memory leaks
        }
        db.close(); // tambahkan ini untuk menghindari memory leaks
        return userList;
    }

    public void delete(String name) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = KEY_NAME + "=?";
        String[] whereArgs = {name};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close(); // tambahkan ini untuk menghindari memory leaks
    }

    public void update(PersonBean personBean) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_AGE, personBean.getAge());
        String whereClause = KEY_NAME + "=?";
        String[] whereArgs = {personBean.getName()};
        db.update(TABLE_NAME, values, whereClause, whereArgs);
        db.close(); // tambahkan ini untuk menghindari memory leaks
    }
}
