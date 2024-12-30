package com.example.rentalin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.rentalin.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient gsc;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    private EditText emailRegisterET, passwordRegisterET, confirmPasswordRegisterET;
    private Button registerBtn, googleLoginBtn, loginRedirectBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setLayout();
        setVar();
    }

    private void setLayout() {
        emailRegisterET = findViewById(R.id.emailRegisterET);
        passwordRegisterET = findViewById(R.id.passwordRegisterET);
        confirmPasswordRegisterET = findViewById(R.id.confirmPasswordRegisterET);
        registerBtn = findViewById(R.id.registerBtn);
        googleLoginBtn = findViewById(R.id.googleLoginBtn);
        loginRedirectBtn = findViewById(R.id.loginRedirectBtn);
    }

    private void setVar() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_id))
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        googleLoginBtn.setOnClickListener(v -> signInWithGoogle());
        registerBtn.setOnClickListener(v -> registerUser());
        loginRedirectBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    checkUserProfile(user);
                }else {
                    Toast.makeText(this, "Google Sign In Failed", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (ApiException e) {
            Toast.makeText(this, "Google Sign In Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkUserProfile(FirebaseUser user) {
        db.collection("users").document(user.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(this, EditProfileActivity.class));
                }
            }
        });
    }

    private void registerUser() {
        String email = emailRegisterET.getText().toString();
        String password = passwordRegisterET.getText().toString();
        String confirmpassword = confirmPasswordRegisterET.getText().toString();


        if (!email.endsWith("@gmail.com")) {
            Toast.makeText(this, "Email must ends with @gmail.com", Toast.LENGTH_SHORT).show();
            return;
        }else if(!password.equals(confirmpassword)) {
            Toast.makeText(this, "Password not match", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Ambil daftar metode sign-in yang terdaftar untuk email ini
                        List<String> signInMethods = task.getResult().getSignInMethods();

                        if (signInMethods != null && !signInMethods.isEmpty()) {
                            // Jika ada metode sign-in yang terdaftar, artinya email sudah terdaftar
                            Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show();
                        } else {
                            // Jika email belum terdaftar, lanjutkan dengan registrasi
                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            mAuth.getCurrentUser();
                                            startActivity(new Intent(this, EditProfileActivity.class));
                                        } else {
                                            Toast.makeText(this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(this, "Error checking email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}