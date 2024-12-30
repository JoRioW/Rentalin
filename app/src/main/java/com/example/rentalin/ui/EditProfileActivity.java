package com.example.rentalin.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.rentalin.R;
import com.example.rentalin.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EditProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView profileIV;
    private EditText nameProfileET, phoneProfileET, addressProfileET;
    private Button saveProfileBtn;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setLayout();
        setVar();
        loadImage();
    }

    private void setLayout() {
        nameProfileET = findViewById(R.id.nameProfileET);
        phoneProfileET = findViewById(R.id.phoneProfileET);
        addressProfileET = findViewById(R.id.addressProfileET);
        saveProfileBtn = findViewById(R.id.saveProfileBtn);
        profileIV = findViewById(R.id.profileIV);
    }

    private void setVar() {
        profileIV.setOnClickListener(v -> openFileChooser());
        saveProfileBtn.setOnClickListener(v -> saveProfile());
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            profileIV.setImageURI(imageUri);
        }
    }

    private void saveProfile() {
        String username = nameProfileET.getText().toString();
        String phone = phoneProfileET.getText().toString();
        String address = addressProfileET.getText().toString();

        // Cek apakah semua field terisi
        if (username.isEmpty() || phone.isEmpty() || address.isEmpty() ) {
            Toast.makeText(this, "All fields must be filled!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cek apakah user sudah login
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User is not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        // If there's an image URI selected
        if (imageUri != null) {
            // Jika ada gambar baru yang dipilih, upload gambar tersebut ke Firebase Storage
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("users/" + user.getUid() + "/profileImage");

            // Upload gambar ke Firebase Storage
            storageReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                // Ambil URL gambar yang diupload
                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Buat objek Users dengan URL gambar yang baru
                    Users profile = new Users(username, phone, address, uri.toString());
                    db.collection("users").document(user.getUid()).set(profile)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(this, "Profile Saved!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(this, HomeActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(this, "Save failed!", Toast.LENGTH_SHORT).show();
                                }
                            });
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Image upload failed!", Toast.LENGTH_SHORT).show();
            });
        } else {
            // Jika tidak ada gambar yang dipilih, simpan profil tanpa gambar
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Users profile = new Users(username, phone, address, "");  // Mengirimkan string kosong untuk gambar
            db.collection("users").document(user.getUid()).set(profile)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Profile Saved!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, HomeActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Save failed!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
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
                                nameProfileET.setText(profile.getUsername());
                                phoneProfileET.setText(profile.getPhone());
                                addressProfileET.setText(profile.getAddress());
                                imageUri = Uri.parse(profile.getImage());

                                if (profile.getImage() != null && !profile.getImage().isEmpty()) {
                                    loadImageFromStorage(imageUri);
                                } else {
                                    profileIV.setImageResource(R.drawable.baseline_account_circle_24);
                                }
                            }
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed To Load Profile!", Toast.LENGTH_SHORT).show());
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
                            .into(profileIV);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load image!", Toast.LENGTH_SHORT).show();
                });
    }
}