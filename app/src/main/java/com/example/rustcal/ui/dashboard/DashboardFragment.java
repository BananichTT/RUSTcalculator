package com.example.rustcal.ui.dashboard;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.rustcal.DatabaseHelper;
import com.example.rustcal.R;
import com.example.rustcal.databinding.FragmentDashboardBinding;

import java.io.IOException;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase Db;
    public String damage;
    public String pi;
    public String po;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        DBHelper = new DatabaseHelper(getActivity());

        try {
            DBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            Db = DBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }


        final Spinner spinner =binding.spinner;
        final Spinner spinner2 =binding.spinner2;

        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = "name = ?";
                String[] selectionArgs = new String[]{(String)parent.getItemAtPosition(position)};
                Cursor cursor = Db.query("items", null, selection, selectionArgs, null, null, null);

                if(cursor != null){
                    if(cursor.moveToFirst()){
                        do{
                            damage = cursor.getString(1); // Получаем тот столбец который нужен, в нашем случае урон инструмента
                            pi = cursor.getString(2);
                            Log.d("mLog", "damage: " + damage + "; " + "prochnost: " + pi);
                        }while (cursor.moveToNext());
                    }
                    cursor.close();
                }else {
                    Log.d("mLog", "Cursor is null");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<?> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.objects, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = "name = ?";
                String[] selectionArgs = new String[]{(String)parent.getItemAtPosition(position)};
                Cursor cursor = Db.query("objects", null, selection, selectionArgs, null, null, null);

                if(cursor != null){
                    if(cursor.moveToFirst()){
                        do{
                            po = cursor.getString(1); // Получаем тот столбец который нужен, в нашем случае урон инструмента
                            Log.d("mLog", "prochnost object: " + po);
                        }while (cursor.moveToNext());
                    }
                    cursor.close();
                }else {
                    Log.d("mLog", "Cursor is null");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}