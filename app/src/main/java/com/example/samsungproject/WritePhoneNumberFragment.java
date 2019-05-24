package com.example.samsungproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputLayout;

import static com.example.samsungproject.MainActivity.usedMsg;

public class WritePhoneNumberFragment extends Fragment {
    private TextInputLayout mTextInputLayout;
    private EditText mEditText;
    public WritePhoneNumberFragment() {
    }

    public static WritePhoneNumberFragment newInstance() {
        return new WritePhoneNumberFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.write_phone_number, container, false);
        mTextInputLayout = (TextInputLayout) v.findViewById(R.id.textInputLayout);
        mEditText = (EditText) v.findViewById(R.id.editTextName);

        ImageButton button = v.findViewById(R.id.imageButton12);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEditText.getText().toString();
                usedMsg="4"+text;
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
