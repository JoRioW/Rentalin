package com.example.rentalin.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.rentalin.R;
import com.example.rentalin.ui.EditProfileActivity;
import com.example.rentalin.ui.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;


public class SettingFragment extends Fragment {
    private View view;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ImageView editProfileIV, logoutIV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_setting, container, false);

        setLayout();
        setVar();

        return view;
    }

    private void setLayout() {
        editProfileIV = view.findViewById(R.id.editProfileIV);
        logoutIV = view.findViewById(R.id.logoutIV);
    }

    private void setVar() {
        editProfileIV.setOnClickListener(v -> {
            startActivity(new Intent(view.getContext(), EditProfileActivity.class));
        });

        logoutIV.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(view.getContext(), LoginActivity.class));
            getActivity().finish();
        });

    }
}