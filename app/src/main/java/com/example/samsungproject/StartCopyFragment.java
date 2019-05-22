package com.example.samsungproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class StartCopyFragment extends Fragment {
Button but;
    FragmentManager myFragmentManager;

    final static String TAG_3 = "FRAGMENT_3";


    public StartCopyFragment() {
    }
    public static StartCopyFragment newInstance() {
        return new StartCopyFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View v=  inflater.inflate(R.layout.fragment_copy, container, false);
but=v.findViewById(R.id.start_copy_button);
but.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       Intent intent = new Intent(getActivity(),GetMessage.class);
       startActivity(intent);
    }
});
        return v;
    }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }
}
