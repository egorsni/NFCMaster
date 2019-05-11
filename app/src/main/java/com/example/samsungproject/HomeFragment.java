package com.example.samsungproject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.samsungproject.R;


public class HomeFragment extends Fragment {
    static String type;

    public static String getType() {
        return type;
    }

    public static void setType(String type) {
        HomeFragment.type = type;
    }

    String [] values =
            {"Выберите...","Текст","Действие","Ссылка"};
    public HomeFragment() {
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_write, container, false);


        Spinner spinner = (Spinner) v.findViewById(R.id.spinner);

        MyCustomAdapter adapter = new MyCustomAdapter(getActivity(),
                R.layout.row, values);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
switch (pos){
    case 1:
        type="text";
        loadFragment(WriteTextFragment.newInstance());
        break;
    case 2:
        type="action";
        loadFragment(WriteActionFragment.newInstance());
        break;
    case 3:
        type="url";
        loadFragment(WriteTextFragment.newInstance());
        break;
}

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        return v;
    }
    public class MyCustomAdapter extends ArrayAdapter<String> {

        public MyCustomAdapter(Context context, int textViewResourceId,
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
            View row = inflater.inflate(R.layout.row, parent, false);
            TextView label = (TextView) row.findViewById(R.id.textWrite);
            label.setText(values[position]);

            ImageView icon = (ImageView) row.findViewById(R.id.icon);
            label.setText(values[position]);

            switch(values[position]){
                case "Текст":
                    icon.setImageResource(R.drawable.ic_text_fields_black_24dp);
                    break;
                case "Действие":
                    icon.setImageResource(R.drawable.ic_action_black_24dp);
                    break;
                case "Ссылка":
                    icon.setImageResource(R.drawable.ic_link_black_24dp);
                    break;
            }

            return row;
        }
    }
}