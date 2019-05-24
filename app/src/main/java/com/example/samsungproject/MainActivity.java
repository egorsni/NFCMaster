package com.example.samsungproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;

import static com.example.samsungproject.HomeFragment.clear;


public class MainActivity extends AppCompatActivity {
    public static ArrayList<String> usedItems;
    BottomNavigationView navigation;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_COUNTER = "lastItems";
    public static SharedPreferences mSettings;
   public static SharedPreferences.Editor editor;
static String usedMsg="";
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_write:
                   loadFragment(HomeFragment.newInstance());
                    return true;
                case R.id.navigation_read:
                    Intent intent = new Intent(MainActivity.this, ReadText.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_copy:
                    loadFragment(StartCopyFragment.newInstance());
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onPause() {
        super.onPause();
        // Запоминаем данные
        editor = mSettings.edit();

editor.putBoolean("clear",clear);
            for (int i = 0; i < usedItems.size(); i++) {
                editor.putString(String.valueOf(i), usedItems.get(i));
            }
            editor.apply();
        Log.i("UsedArray0", usedItems.toString());
    }
    @Override
    protected void onResume() {
        super.onResume();
        if ((mSettings.contains("0"))&&!mSettings.getBoolean("clear",false)) {
            // Получаем число из настроек

int t=0;


            if((usedItems.size()==0)) {
                while (mSettings.getString(String.valueOf(t), null) != null) {
                    usedItems.add(mSettings.getString(String.valueOf(t), null));
                    t++;
                }
            }

                Log.i("UsedArray1", usedItems.toString());
                // Выводим на экран данные из настроек
            }
        }
    public static String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1); // Возвращаем подстроку s, которая начиная с нулевой позиции переданной строки (0) и заканчивается позицией символа (pos), который мы хотим удалить, соединенную с другой подстрокой s, которая начинается со следующей позиции после позиции символа (pos + 1), который мы удаляем, и заканчивается последней позицией переданной строки.
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

         navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        findViewById(R.id.navigation_write).callOnClick();

    }

}
