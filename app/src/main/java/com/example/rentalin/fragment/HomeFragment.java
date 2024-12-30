package com.example.rentalin.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rentalin.R;
import com.example.rentalin.adapter.HomeListAdapter;
import com.example.rentalin.model.Car;
import com.example.rentalin.model.Users;
import com.example.rentalin.ui.CarDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private View view;

    private DatabaseReference databaseReference ;
    private List<Car> carList, filteredCarList;

    private ImageView homeUserImg;
    private TextView welcomeHomeTV;
    private HomeListAdapter adapter;
    private RecyclerView homeRV;

    private Spinner cityFilterBtn;
    private String[] cities;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        loadImage();
        setLayout();
        setVar();
        fetchCars();
        return view;
    }

    private void setLayout(){
        welcomeHomeTV = view.findViewById(R.id.welcomeHomeTV);
        cityFilterBtn = view.findViewById(R.id.cityFilterBtn);
        homeRV = view.findViewById(R.id.homeRV);
        homeUserImg = view.findViewById(R.id.homeUserImg);
    }

    private void setVar(){
        databaseReference = FirebaseDatabase.getInstance().getReference("cars");

        cities = getResources().getStringArray(R.array.cities);

        // Set up Spinner adapter
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cityFilterBtn.setAdapter(cityAdapter);

        // Spinner item selection listener
        cityFilterBtn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity = cities[position];
                applyCityFilter(selectedCity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });

        carList = new ArrayList<>();
        filteredCarList = new ArrayList<>();
        homeRV.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new HomeListAdapter(filteredCarList, car -> {
            Intent intent = new Intent(view.getContext(), CarDetailActivity.class);
            intent.putExtra("car", car);
            startActivity(intent);
        });
        homeRV.setAdapter(adapter);
    }

    private void fetchCars() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Car car = data.getValue(Car.class);
                    if (car != null) {
                        carList.add(car);
                    }
                }
                applyCityFilter(cities[0]); // Default: "All Cities"
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(view.getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyCityFilter(String city) {
        filteredCarList.clear();
        if (city.equals("All Cities")) {
            filteredCarList.addAll(carList);
        } else {
            for (Car car : carList) {
                if (car.getCity().equalsIgnoreCase(city)) {
                    filteredCarList.add(car);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void loadImage() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Users profile = documentSnapshot.toObject(Users.class);
                            if (profile != null) {
                                Uri imageuri = Uri.parse(profile.getImage());
                                welcomeHomeTV.setText("Welcome,\n" + profile.getUsername());


                                if (profile.getImage() != null && !profile.getImage().isEmpty()) {
                                    loadImageFromStorage(imageuri);
                                }else {
                                    homeUserImg.setImageResource(R.drawable.baseline_account_circle_24);
                                }
                            }
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(view.getContext(), "Failed To Load Image!", Toast.LENGTH_SHORT).show());

        }
    }

    private void loadImageFromStorage(Uri imageuri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(imageuri.toString());

        storageReference.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    Glide.with(this)
                            .load(uri)
                            .placeholder(R.drawable.baseline_account_circle_24)
                            .into(homeUserImg);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(view.getContext(), "Failed to load image!", Toast.LENGTH_SHORT).show();
                });
    }
}