package com.example.rentalin.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentalin.R;
import com.example.rentalin.adapter.HistoryAdapter;
import com.example.rentalin.model.Booking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {
    private View view;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private RecyclerView historyRV;
    private HistoryAdapter adapter;
    private List<Booking> bookList;
    private TextView historyDataTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);

        setLayout();
        setVar();
        getDataFromFirebase();

        return view;
    }

    private void setLayout() {
        historyRV = view.findViewById(R.id.historyRV);
        historyDataTV = view.findViewById(R.id.historyDataTV);
    }

    private void setVar() {
        bookList = new ArrayList<>();
        historyRV.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new HistoryAdapter(bookList);
        historyRV.setAdapter(adapter);
    }

    private void getDataFromFirebase() {
        db.collection("users")
                .document(mAuth.getCurrentUser().getUid()) // Dokumen user yang login
                .collection("history") // Subkoleksi "history"
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            bookList.clear(); // Kosongkan list
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Konversi Firestore document ke Booking object
                                Booking booking = document.toObject(Booking.class);
                                bookList.add(booking);
                            }
                            adapter.notifyDataSetChanged(); // Refresh RecyclerView

                            if (bookList.isEmpty()) {
                                historyDataTV.setVisibility(View.VISIBLE); // Menampilkan pesan jika list kosong
                            } else {
                                historyDataTV.setVisibility(View.INVISIBLE); // Menyembunyikan pesan jika ada data
                            }
                        } else {
                            Toast.makeText(view.getContext(), "Cannot fetch data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}