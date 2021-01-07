package com.example.timer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "timerDB";
    public static final String TABLE_TIMERS = "timers";

    public static final String KEY_ID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_PREPARING = "preparing";
    public static final String KEY_WORK = "work";
    public static final String KEY_REST = "rest";
    public static final String KEY_CYCLES = "cycles";
    public static final String KEY_SETS = "sets";
    public static final String KEY_REST_BETWEEN_SETS = "between_sets";
    public static final String KEY_CALM_DOWN = "calm_down";
    public static final String COLOR = "color";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_TIMERS + "(" + KEY_ID
                + " integer primary key autoincrement," + KEY_TITLE + " text," + KEY_PREPARING
                + " integer," + KEY_WORK + " integer," + KEY_REST + " integer," + KEY_CYCLES
                + " integer," + KEY_SETS + " integer," + KEY_REST_BETWEEN_SETS
                + " integer," + KEY_CALM_DOWN + " integer," + COLOR + " integer"+ ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + TABLE_TIMERS);

        onCreate(db);
    }

    public int addTimer(TimerData timer) {
        SQLiteDatabase db = this.getWritableDatabase();
        return (int)db.insert(DbHelper.TABLE_TIMERS, null, getContentValues(timer));
    }

    public void deleteTimer(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("delete from " + TABLE_TIMERS + " where " +
                DbHelper.KEY_ID + " = " + id, null);
        db.delete(TABLE_TIMERS, KEY_ID + " = " + id, null);
        cursor.close();
    }

    public void updateTimer(TimerData timer) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(DbHelper.TABLE_TIMERS, getContentValues(timer), DbHelper.KEY_ID +
                " = " + timer.getId(), null);
    }


    public ArrayList<TimerData> getAllTimers() {
        Cursor cursor = getReadableDatabase().query(TABLE_TIMERS, null, null,
                null, null, null, null);

        ArrayList<TimerData> result = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                TimerData timer = new TimerData(
                    cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.KEY_TITLE)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_PREPARING)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_WORK)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_REST)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_CYCLES)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_SETS)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_REST_BETWEEN_SETS)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_CALM_DOWN)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.COLOR))
                );
                result.add(timer);
            } while(cursor.moveToNext());
        }

        cursor.close();
        return result;
    }

    private ContentValues getContentValues(TimerData timer){
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        if(timer.getColor() == 0){
            timer.setColor(color);
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.KEY_TITLE, timer.getTitle());
        contentValues.put(DbHelper.KEY_PREPARING, String.valueOf(timer.getPreparationTime()));
        contentValues.put(DbHelper.KEY_WORK, String.valueOf(timer.getWorkingTime()));
        contentValues.put(DbHelper.KEY_REST, String.valueOf(timer.getRestTime()));
        contentValues.put(DbHelper.KEY_CYCLES, String.valueOf(timer.getCyclesAmount()));
        contentValues.put(DbHelper.KEY_SETS, String.valueOf(timer.getSetsAmount()));
        contentValues.put(DbHelper.KEY_REST_BETWEEN_SETS, String.valueOf(timer.getBetweenSetsRest()));
        contentValues.put(DbHelper.KEY_CALM_DOWN, String.valueOf(timer.getCalmDown()));
        contentValues.put(DbHelper.COLOR, String.valueOf(timer.getColor()));
        return contentValues;
    }
}
