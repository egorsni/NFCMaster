package com.example.samsungproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.samsungproject.R;
import com.google.android.material.textfield.TextInputLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class WriteTextFragment extends Fragment {


    private TextInputLayout mTextInputLayout;
    private EditText mEditText;
    public WriteTextFragment() {
    }

    public static WriteTextFragment newInstance() {
        return new WriteTextFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.write_text, container, false);
        mTextInputLayout = (TextInputLayout) v.findViewById(R.id.textInputLayout);
        mEditText = (EditText) v.findViewById(R.id.editTextName);
        if(HomeFragment.getType()=="url") {
            mEditText.setText("https://www.");
        }

ImageButton button = v.findViewById(R.id.imageButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEditText.getText().toString();
                Intent intent = new Intent(getActivity(), Write.class);
                intent.putExtra("1", text);
                startActivity(intent);
            }
        });
        return v;
    }
    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    loadFragment(HomeFragment.newInstance());
                    return true;
                }
                return false;
            }
        });

    }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }
}

