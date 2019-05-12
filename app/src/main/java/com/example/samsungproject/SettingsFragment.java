package com.example.samsungproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.samsungproject.R;
import com.google.android.material.checkbox.MaterialCheckBox;

public class SettingsFragment extends Fragment {
    MaterialCheckBox service;



    static boolean serviceActivate;

    public static boolean isServiceActivate() {
        return serviceActivate;
    }



    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_COUNTER = "serviceactivate";
    private SharedPreferences mSettings;
    public SettingsFragment() {
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_COUNTER, serviceActivate);
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSettings.contains(APP_PREFERENCES_COUNTER)) {
            // Получаем число из настроек
            serviceActivate = mSettings.getBoolean(APP_PREFERENCES_COUNTER, false);
            service.setChecked(serviceActivate);
            // Выводим на экран данные из настроек
        }
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View v=  inflater.inflate(R.layout.fragment_settings, container, false);
        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        service=v.findViewById(R.id.service);
        service.setChecked(serviceActivate);
        service.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    serviceActivate = true;
                    getActivity().startService(new Intent(getActivity(),NFCReadService.class));
                    Toast.makeText(getContext(),"service started",Toast.LENGTH_LONG).show();
                }
                else {
                    serviceActivate=false;
                    getActivity().stopService(new Intent(getActivity(),NFCReadService.class));
                    Toast.makeText(getContext(),"service stopped",Toast.LENGTH_LONG).show();
                }

            }
        });
        return v;
    }

}
