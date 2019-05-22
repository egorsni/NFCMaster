package com.example.samsungproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.samsungproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class WriteActionFragment extends Fragment {
    static ArrayList action;
    LinearLayout layout;
    Spinner newSpinner;
    NewCustomAdapter adapter;
    Button start;
    String actions;
    int i=1;
    FloatingActionButton add;

    public static ArrayList<String> getAction() {
        return action;
    }

    public static void setAction(ArrayList<String> action) {
        WriteActionFragment.action = action;
    }

    public String[] getActionValues() {
        return actionValues;
    }

    public void setActionValues(String[] actionValues) {
        this.actionValues = actionValues;
    }

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

        start=v.findViewById(R.id.startwrite);
        action= new ArrayList();
        layout = v.findViewById(R.id.linearLayout1);

         Spinner actionSpinner = (Spinner) v.findViewById(R.id.aciton_spinner);

         adapter = new NewCustomAdapter(getActivity(),
                R.layout.action_row, actionValues);
        actionSpinner.setAdapter(adapter);
       actionSpinner.performClick();
start.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(action.size()>0){
            actions=action.toString();
            Intent intent = new Intent(getActivity(), Write.class);
            intent.putExtra("1", actions);
            startActivity(intent);
        }
        else{

        }
    }
});

        actionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                switch (pos){
                    case 1:
if(!action.contains("1")){
    action.add("1");
}
                        break;
                    case 2:
                        if(!action.contains("2")){
                            action.add("2");
                        }
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        add =  v.findViewById(R.id.addActionButton);




        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newSpinner = new Spinner(getActivity());
                newSpinner.setAdapter(adapter);
                newSpinner.performClick();

                newSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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
//                        loadFragment(WritePhoneFragment.newInstance());
                                break;
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });
                newSpinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                if(i<2) {
                    layout.addView(newSpinner);
                    i++;
                }
                if(i>=2){
                    add.hide();
                }
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
