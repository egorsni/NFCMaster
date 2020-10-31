package com.example.samsungproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungproject.R;

import java.util.ArrayList;

import static com.example.samsungproject.MainActivity.editor;
import static com.example.samsungproject.MainActivity.mSettings;
import static com.example.samsungproject.MainActivity.removeCharAt;
import static com.example.samsungproject.MainActivity.usedItems;


public class HomeFragment extends Fragment implements RecyclerViewAdapter.OnItemClickListener {
    static String type;
Button clearAll;
static boolean rewrite;
    public static RecyclerView lastList;
   public static RecyclerViewAdapter lastUsedAdapter;
    static boolean clear=false;

    public static String getType() {
        return type;
    }

    String [] values =
            {"Choose...","Text","Action","Link","Phone number"};
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

lastList=v.findViewById(R.id.lastItems);
if(usedItems==null) {
    usedItems = new ArrayList<>();
}
        lastUsedAdapter =new RecyclerViewAdapter(usedItems);
        Spinner spinner = (Spinner) v.findViewById(R.id.spinner);
lastList.setLayoutManager(new LinearLayoutManager(getContext()));

            lastList.setAdapter(lastUsedAdapter);
            lastUsedAdapter.setOnItemClickListener(this);
        lastList.invalidate();
clearAll=v.findViewById(R.id.clearListButton);

        if(!usedItems.isEmpty()){
    clear=false;
}
clearAll.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(editor!=null) {
            editor.clear();
        }
            usedItems.clear();
            lastList.getAdapter().notifyDataSetChanged();
            clear = true;

    }
});
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
    case 4:
        type="phone";
        loadFragment(WritePhoneNumberFragment.newInstance());
        break;
}

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        return v;
    }

    @Override
    public void onItemClick(int position) {
Intent intent2=new Intent(getContext(),Write.class);
String o=usedItems.get(position);
char one =o.charAt(0);
o=removeCharAt(o,0);
rewrite=true;
switch (one){
    case '1':
        type="text";
        break;
    case '3':
        String actions="";
        if(o.contains("Turn on Wifi")){
            actions=actions+"1";
        }
        if(o.contains("Turn off Wifi")){
            actions=actions+"2";
        }
        if(o.contains("Turn on Bluetooth")){
            actions=actions+"3";
        }
        if(o.contains("Turn off Bluetooth")){
            actions=actions+"4";
        }
        o=actions;
type="action";
        break;
    case '4':
        type="phone";
        break;
    case '5':
        type="url";
        break;
}
intent2.putExtra("1",o);
startActivity(intent2);
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
                case "Text":
                    icon.setImageResource(R.drawable.ic_text_fields_black_24dp);
                    break;
                case "Action":
                    icon.setImageResource(R.drawable.ic_action_black_24dp);
                    break;
                case "Link":
                    icon.setImageResource(R.drawable.ic_link_black_24dp);
                    break;
                case "Phone number":
                    icon.setImageResource(R.drawable.ic_local_phone_black_24dp);
                    break;
            }

            return row;
        }
    }
}