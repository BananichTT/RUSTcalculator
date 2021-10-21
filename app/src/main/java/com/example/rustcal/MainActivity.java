package com.example.rustcal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button read = (Button)findViewById(R.id.button);
        Button insert = (Button)findViewById(R.id.button2);

        RustDB rustDB = new RustDB(this);

        SQLiteDatabase database = rustDB.getWritableDatabase();
        ContentValues contentValues = new ContentValues();



        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentValues.put(RustDB.KEY_NAME, "Rock");
                contentValues.put(RustDB.KEY_DAMAGE, 10);
                contentValues.put(RustDB.KEY_ITEM_STRENGTH, 500);

                database.insert(RustDB.TABLE_CONTACTS, null, contentValues);
            }
        });

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = database.query(RustDB.TABLE_CONTACTS, null, null,null,null, null,null);

                if(cursor.moveToFirst()){

                    int nameIndex = cursor.getColumnIndex(RustDB.KEY_NAME);
                    int damageIndex = cursor.getColumnIndex(RustDB.KEY_DAMAGE);
                    int itemsIndex = cursor.getColumnIndex(RustDB.KEY_ITEM_STRENGTH);
                    do {
                        Log.d("mLog",
                                ", name = " + cursor.getString(nameIndex) +
                                ", damage = " + cursor.getString(damageIndex) +
                                ", itemS = " + cursor.getInt(itemsIndex));
                    }while (cursor.moveToNext());
                }else{
                    Log.d("mLog", "0 rows");
                    cursor.close();
                }
            }
        });
    }
}