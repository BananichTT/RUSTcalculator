package com.example.rustcal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class RustDB extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "rustDb";
    public static final String TABLE_CONTACTS = "items";

    public static final String KEY_NAME = "name";
    public static final String KEY_DAMAGE = "damage";
    public static final String KEY_ITEM_STRENGTH = "items";

    public RustDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CONTACTS + "(" + KEY_NAME + " text primary key," + KEY_DAMAGE + "text," + KEY_ITEM_STRENGTH + "text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_CONTACTS);
    }
}
