package com.example.rentalin.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.rentalin.R;
import com.example.rentalin.model.Car;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CarDetailActivity extends AppCompatActivity {
    private Car car;

    private ImageView detailImgIV, backDetailBtn;
    private TextView detailNameTV, detailDescTV, detailModelTV, detailSeatsTV, detailTransTV, detailPriceDayTV, detailTotalPriceTV, detailStartTV, detailEndTV;
    private Button detailtPickDateBtn, detailBookBtn;

    private String startDate, endDate;
    private int totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_car_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getIntentFromHome();
        setLayout();
        setVar();
    }

    private void getIntentFromHome() {
        car = (Car) getIntent().getSerializableExtra("car");
    }

    private void setLayout() {
        detailImgIV = findViewById(R.id.detailImgIV);
        detailNameTV = findViewById(R.id.detailNameTV);
        detailDescTV = findViewById(R.id.detailDescTV);
        detailModelTV = findViewById(R.id.detailModelTV);
        detailSeatsTV = findViewById(R.id.detailSeatsTV);
        detailTransTV = findViewById(R.id.detailTransTV);
        detailPriceDayTV = findViewById(R.id.detailPriceDayTV);
        detailTotalPriceTV = findViewById(R.id.detailTotalPriceTV);
        detailtPickDateBtn = findViewById(R.id.detailtPickDateBtn);
        detailBookBtn = findViewById(R.id.detailBookBtn);
        detailStartTV = findViewById(R.id.detailStartTV);
        detailEndTV = findViewById(R.id.detailEndTV);
        backDetailBtn = findViewById(R.id.backDetailBtn);
    }

    private void setVar() {
        backDetailBtn.setOnClickListener(v -> finish());
        Glide.with(this)
                .load(car.getCar_image())
                .placeholder(R.drawable.ic_launcher_background)
                .into(detailImgIV);
        detailNameTV.setText(car.getCar_name());
        detailDescTV.setText(car.getDescription());
        detailModelTV.setText(car.getCar_model());
        detailSeatsTV.setText("" + car.getCar_seats());
        detailTransTV.setText(car.getTransmission());
        detailPriceDayTV.setText("" + car.getPrice_per_day());
        detailTotalPriceTV.setText("N/A");
        detailStartTV.setText("N/A");
        detailEndTV.setText("N/A");


        detailtPickDateBtn.setOnClickListener(v -> showDatePickerDialog());

        detailBookBtn.setOnClickListener(v -> {
            try{
                if(!startDate.equals("N/A")){
                    Intent intent = new Intent(this, PaymentActivity.class);
                    intent.putExtra("car_image", car.getCar_image());
                    intent.putExtra("car_name", car.getCar_name());
                    intent.putExtra("total_price", totalPrice);
                    intent.putExtra("start_date", startDate);
                    intent.putExtra("end_date", endDate);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this, "Please pick a date", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch(Exception e){
                e.printStackTrace();
            }

        });
    }

    private void showDatePickerDialog() {
        final Dialog dateDialog = new Dialog(this);
        dateDialog.setContentView(R.layout.start_end_date);
        dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button startButton = dateDialog.findViewById(R.id.startDateBtn);
        Button endButton = dateDialog.findViewById(R.id.endDateBtn);
        Button okButton = dateDialog.findViewById(R.id.okBtn);

        startButton.setOnClickListener(v -> {
            // Pilih Start Date
            showDatePickerDialog(date -> {
                startDate = date;
                startButton.setText("Start Date: " + startDate);
            });
        });

        endButton.setOnClickListener(v -> {
            // Pilih End Date
            showDatePickerDialog(date -> {
                endDate = date;
                endButton.setText("End Date: " + endDate);
            });
        });

        okButton.setOnClickListener(v -> {
            if (startDate != null && endDate != null) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date start = sdf.parse(startDate);
                    Date end = sdf.parse(endDate);

                    if (start != null && end != null) {
                        if (end.before(start)) {
                            // Jika endDate lebih kecil dari startDate, tampilkan Toast
                            Toast.makeText(this, "End date harus lebih besar dari Start date!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Jika valid, hitung total harga
                            calculateTotalPrice();
                            detailStartTV.setText(startDate);
                            detailEndTV.setText(endDate);
                            dateDialog.dismiss();
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        dateDialog.show();
    }

    private void calculateTotalPrice() {
        if (startDate != null && endDate != null) {
            try {
                // Format tanggal yang digunakan
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date start = sdf.parse(startDate);
                Date end = sdf.parse(endDate);

                if (start != null && end != null) {
                    // Hitung selisih hari
                    long diffInMillies = end.getTime() - start.getTime();
                    long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);

                    // Hitung total harga
                    totalPrice = (int) (diffInDays * car.getPrice_per_day());

                    // Tampilkan total harga
                    DecimalFormat decimalFormat = new DecimalFormat("#,###");
                    detailTotalPriceTV.setText("Rp" + decimalFormat.format(totalPrice));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void showDatePickerDialog(OnDateSetListener listener) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> listener.onDateSet(year + "-" + (month + 1) + "-" + dayOfMonth),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private interface OnDateSetListener {
        void onDateSet(String date);
    }
}