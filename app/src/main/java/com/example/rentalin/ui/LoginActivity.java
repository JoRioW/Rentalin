package com.example.rentalin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient gsc;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private EditText emailLoginET, passwordLoginET;
    private Button loginBtn, googleLoginBtn, registerRedirectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setLayout();
        setVar();
    }

    private void setLayout() {
        emailLoginET = findViewById(R.id.emailLoginET);
        passwordLoginET = findViewById(R.id.passwordLoginET);
        loginBtn = findViewById(R.id.loginBtn);
        googleLoginBtn = findViewById(R.id.googleLoginBtn);
        registerRedirectBtn = findViewById(R.id.registerRedirectBtn);
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
        loginBtn.setOnClickListener(v -> loginUser());
        registerRedirectBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
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
            Log.w("GoogleSignIn", "signInResult:failed code=" + e.getStatusCode());
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

    private void loginUser() {
        String email = emailLoginET.getText().toString();
        String password = passwordLoginET.getText().toString();

        if (!email.endsWith("@gmail.com")) {
            Toast.makeText(this, "Email must ends with @gmail.com", Toast.LENGTH_SHORT).show();
            return;
        }else if(password.isEmpty()) {
            Toast.makeText(this, "Password empty", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                checkUserProfile(user);
            }else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}