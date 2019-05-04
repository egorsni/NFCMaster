package com.example.samsungproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class WriteActionFragment extends Fragment {
    public WriteActionFragment() {
    }
    String [] actionValues =
            {"Выберите...","Включение WiFi","Выключение WiFi"};

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }

    public static WriteActionFragment newInstance() {
        return new WriteActionFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.choose_action, container, false);
        Spinner actionSpinner = (Spinner) v.findViewById(R.id.aciton_spinner);

        NewCustomAdapter adapter = new NewCustomAdapter(getActivity(),
                R.layout.action_row, actionValues);
        actionSpinner.setAdapter(adapter);
       actionSpinner.performClick();
        actionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                switch (pos){
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:
//                        type="url";
//                        loadFragment(WriteTextFragment.newInstance());
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        return v;
    }
    public class NewCustomAdapter extends ArrayAdapter<String> {

        public NewCustomAdapter(Context context, int textViewResourceId,
                               String[] objects) {
            super(context, textViewResourceId, objects);

        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {

            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.action_row, parent, false);
            TextView label = (TextView) row.findViewById(R.id.action_textWrite);
            label.setText(actionValues[position]);

            ImageView icon = (ImageView) row.findViewById(R.id.action_icon);
            label.setText(actionValues[position]);

            switch(actionValues[position]){
                case "Включение WiFi":
                    icon.setImageResource(R.drawable.wifi_on);
                    break;
                case "Выключение WiFi":
                    icon.setImageResource(R.drawable.wifi_off);
                    break;

            }

            return row;
        }
    }
}
