package com.example.rentalin.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.rentalin.R;
import com.example.rentalin.fragment.HistoryFragment;
import com.example.rentalin.fragment.HomeFragment;
import com.example.rentalin.fragment.SettingFragment;

public class HomeActivity extends AppCompatActivity {
    private LinearLayout homeBtn, historyBtn, profileBtn;
    private TextView homeBtnTV, historyBtnTV, profileBtnTV;
    private ImageView homeBtnIV, historyBtnIV, profileBtnIV;
    private boolean isFragmentActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setLayout();
        setVar();
    }

    private void setLayout(){
        homeBtn = findViewById(R.id.homeBtn);
        historyBtn = findViewById(R.id.historyBtn);
        profileBtn = findViewById(R.id.profileBtn);

        homeBtnTV = findViewById(R.id.homeBtnTV);
        historyBtnTV = findViewById(R.id.historyBtnTV);
        profileBtnTV = findViewById(R.id.profileBtnTV);
        homeBtnIV = findViewById(R.id.homeBtnIV);
        historyBtnIV = findViewById(R.id.historyBtnIV);
        profileBtnIV = findViewById(R.id.profileBtnIV);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameContainer, new HomeFragment()).commit();
        updateButtonState("home");
    }

    private void loadFragment(Fragment fg, String tag){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.frameContainer, fg);
        transaction.commit();

        updateButtonState(tag);
    }

    private void setVar(){
        homeBtn.setOnClickListener(v -> {
            if (!isFragmentActive) {
                loadFragment(new HomeFragment(), "home");
            }
        });

        historyBtn.setOnClickListener(v -> {
            if (!isFragmentActive) {
                loadFragment(new HistoryFragment(), "history");
            }
        });

        profileBtn.setOnClickListener(v -> {
            if (!isFragmentActive) {
                loadFragment(new SettingFragment(), "profile");
            }
        });
    }

    private void updateButtonState(String activeFragment) {
        // Reset semua tombol agar bisa diklik
        homeBtn.setEnabled(true);
        historyBtn.setEnabled(true);
        profileBtn.setEnabled(true);

        // Mengubah warna tombol berdasarkan fragment yang aktif
        if (activeFragment.equals("home")) {
            homeBtn.setEnabled(false); // Nonaktifkan Home button
            homeBtnIV.setImageTintList(ContextCompat.getColorStateList(this, R.color.darkerpink));
            homeBtnTV.setTextColor(getResources().getColor(R.color.darkerpink));
        } else {
            homeBtnIV.setImageTintList(ContextCompat.getColorStateList(this, R.color.gray));
            homeBtnTV.setTextColor(getResources().getColor(R.color.gray));
        }

        if (activeFragment.equals("history")) {
            historyBtn.setEnabled(false); // Nonaktifkan History button
            historyBtnIV.setImageTintList(ContextCompat.getColorStateList(this, R.color.darkerpink));
            historyBtnTV.setTextColor(getResources().getColor(R.color.darkerpink));
        } else {
            historyBtnIV.setImageTintList(ContextCompat.getColorStateList(this, R.color.gray));
            historyBtnTV.setTextColor(getResources().getColor(R.color.gray));
        }

        if (activeFragment.equals("profile")) {
            profileBtn.setEnabled(false); // Nonaktifkan History button
            profileBtnIV.setImageTintList(ContextCompat.getColorStateList(this, R.color.darkerpink));
            profileBtnTV.setTextColor(getResources().getColor(R.color.darkerpink));
        } else {
            profileBtnIV.setImageTintList(ContextCompat.getColorStateList(this, R.color.gray));
            profileBtnTV.setTextColor(getResources().getColor(R.color.gray));
        }
    }

}