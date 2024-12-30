package com.example.rentalin.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.rentalin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {
    private FirebaseAuth mAuth  = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private ImageView paymentIV, backPayBtn;
    private TextView paymentNameTV, paymentStartTV, paymentEndTV, paymentTotalTV;
    private Spinner paymentMethodS;
    private Button paymentBtn;

    private String car_image;
    private String car_name;
    private String start_date;
    private String end_date;
    private int total_price;
    private String[] paymentMethods;
    private String selectedMethod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getIntentFromActivity();
        setLayout();
        setVar();
    }

    private void getIntentFromActivity() {
        Intent intent = getIntent();
        car_image = intent.getStringExtra("car_image");
        car_name = intent.getStringExtra("car_name");
        start_date = intent.getStringExtra("start_date");
        end_date = intent.getStringExtra("end_date");
        total_price = intent.getIntExtra("total_price", 0);
    }

    private void setLayout() {
        backPayBtn = findViewById(R.id.backPayBtn);
        paymentIV = findViewById(R.id.paymentIV);
        paymentNameTV = findViewById(R.id.paymentNameTV);
        paymentStartTV = findViewById(R.id.paymentStartTV);
        paymentEndTV = findViewById(R.id.paymentEndTV);
        paymentTotalTV = findViewById(R.id.paymentTotalTV);
        paymentMethodS = findViewById(R.id.paymentMethodS);
        paymentBtn = findViewById(R.id.paymentBtn);
        paymentMethods = getResources().getStringArray(R.array.ddl_item);
    }

    private void setVar() {
        backPayBtn.setOnClickListener(v -> finish());

        Glide.with(this)
                .load(car_image)
                .placeholder(R.drawable.ic_launcher_background)
                .into(paymentIV);
        paymentNameTV.setText(car_name);
        paymentStartTV.setText(start_date);
        paymentEndTV.setText(end_date);
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        paymentTotalTV.setText("Rp" + decimalFormat.format(total_price));

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    // Set the hint text color to gray
                    textView.setTextColor(Color.GRAY);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        for (String item : paymentMethods) {
            adapter.add(item);
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentMethodS.setAdapter(adapter);

        paymentMethodS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Ambil item yang dipilih
                selectedMethod = parentView.getItemAtPosition(position).toString();
                Log.d("Selected Car", selectedMethod);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Tidak ada item yang dipilih
            }
        });

        paymentBtn.setOnClickListener(v -> {
            if(!selectedMethod.equals("Choose payment method")){
                Map<String, Object> booking = new HashMap<>();
                booking.put("car_image", car_image);
                booking.put("car_name", car_name);
                booking.put("total_price", total_price);
                booking.put("start_date", start_date);
                booking.put("end_date", end_date);
                booking.put("payment_method", selectedMethod);
                firestore.collection("users").document(mAuth.getCurrentUser().getUid())
                        .collection("history").add(booking)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
            else{
                Toast.makeText(this, "User must choose one of a kind of methods!", Toast.LENGTH_SHORT).show();
                return;
            }

        });
    }
}