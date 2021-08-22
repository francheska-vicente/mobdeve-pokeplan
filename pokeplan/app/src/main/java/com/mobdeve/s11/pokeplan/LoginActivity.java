package com.mobdeve.s11.pokeplan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {
    private ImageButton btnloginback;
    private Button btnloginsubmit;

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;

    private FirebaseAuth mAuth;
    private ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();


        etEmail = findViewById(R.id.et_login_email);
        etPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login_submit);
        pbLoading = findViewById(R.id.pb_login);
        pbLoading.setVisibility(View.GONE);
        initBackBtn();
        initSubmitBtn();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin ();
            }
        });
    }

    private void userLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if(email.isEmpty()) {
            etEmail.setError("Email is required.");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please provide a valid e-mail address.");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required.");
            etPassword.requestFocus();
            return;
        } else if (password.length() < 6) {
            etPassword.setError("Password length should be at least six characters.");
            etPassword.requestFocus();
            return;
        }

        pbLoading.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    UserSingleton.getUser();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed. Please check your email and password.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    private void initBackBtn() {
        btnloginback = findViewById(R.id.ib_login_back);
        btnloginback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initSubmitBtn() {
        btnloginsubmit = findViewById(R.id.btn_login_submit);
        btnloginsubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), RegisterStarterActivity.class);
                view.getContext().startActivity(i);
            }
        });
    }
}
