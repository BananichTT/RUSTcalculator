package com.example.rustcal.ui.dashboard;

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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
    public String damage; // Урон и одновремено урон который получает инструмент за 1 удар
    public String pri; // Прочность инструмента
    public String pro; // Прочность объекта

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

        final Button calculate = binding.button4;
        final TextView text = binding.textView3;
        final Spinner spinner =binding.spinner;
        final Spinner spinner2 =binding.spinner2;

        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = "name = ?"; // Параметр по которому ведется поиск
                String[] selectionArgs = new String[]{(String)parent.getItemAtPosition(position)}; // Значение для параметра

                Cursor cursor = Db.query("items", null, selection, selectionArgs, null, null, null); // Запрос

                // Поиск значения вроде как
                if(cursor != null){
                    if(cursor.moveToFirst()){
                        do{
                            damage = cursor.getString(1); // Получаем тот столбец который нужен, в нашем случае урон инструмента
                            pri = cursor.getString(2); // Тут прочность инструмента
                            Log.d("mLog", "damage: " + damage + "; " + "prochnost: " + pri); // На всякий случай вывожу информацию в логи
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

        // Тоже самое только для второго спиннера

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
                            pro = cursor.getString(1); // Получаем тот столбец который нужен, в нашем случае урон инструмента
                            Log.d("mLog", "prochnost object: " + pro);
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

        // Вычисления происходят по нажатию кнопки

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double a = Integer.parseInt(damage); // урон инструмента и тот урон который получает он за 1 удар
                                                        // (если эти значения должны быть разные то можно разбить на 2 переменные)
                double pi = Integer.parseInt(pri);// прочность инструмента
                double po = Integer.parseInt(pro);// прочность объекта
                double n = (a / pi) * (po / a); // формула по которой происходит расчет

                text.setText(String.valueOf(n));
                Log.d("calculateing", String.valueOf(n));

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