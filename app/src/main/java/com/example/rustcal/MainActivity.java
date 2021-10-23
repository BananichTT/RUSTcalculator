package com.example.rustcal;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.rustcal.ui.DatabaseHelper;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private DatabaseHelper DBHelper;
    private SQLiteDatabase Db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button read = (Button)findViewById(R.id.button);
        DBHelper = new DatabaseHelper(this);

        try {
            DBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            Db =DBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String items = "";

                Cursor cursor = Db.rawQuery("SELECT * FROM items", null);
                cursor.moveToFirst();

                while (!cursor.isAfterLast()){
                    items += cursor.getString(0) + " | ";
                    cursor.moveToNext();
                }
                cursor.close();

                Log.d("mLog", items);
            }
        });
    }
}