package com.mobdeve.s11.pokeplan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class RegisterActivity extends AppCompatActivity {
    private ImageButton btnregisterback;
    private Button btnregistersubmit;

    private FirebaseAuth mAuth;
    private EditText etName;
    private EditText etEmail;
    private EditText etUsername;
    private EditText etPassword;

    public static final String KEY_NAME = "KEY_NAME";
    public static final String KEY_EMAIL = "KEY_EMAIL";
    public static final String KEY_PASSWORD = "KEY_PASSWORD";
    public static final String KEY_USERNAME = "KEY_USERNAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initBackBtn();
        initSubmitBtn();
        mAuth = FirebaseAuth.getInstance();

        this.etName = findViewById(R.id.et_register_name);
        this.etEmail = findViewById(R.id.et_register_email);
        this.etUsername = findViewById(R.id.et_register_username);
        this.etPassword = findViewById(R.id.et_register_password);

    }

    private void initBackBtn() {
        btnregisterback = findViewById(R.id.ib_register_back);
        btnregisterback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void registerUser (View view) {
        String name = this.etName.getText().toString();
        String email = this.etEmail.getText().toString();
        String username = this.etUsername.getText().toString();
        String password = this.etPassword.getText().toString();

        if (name.isEmpty()) {
            etName.setError("Name is required.");
            etName.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please provide a valid e-mail address.");
            etEmail.requestFocus();
            return;
        }

        if (username.isEmpty()) {
            etUsername.setError("Username is required.");
            etUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required.");
            etPassword.requestFocus();
            return;
        }

        Intent i = new Intent(view.getContext(), RegisterStarterActivity.class);
        i.putExtra(KEY_NAME, name);
        i.putExtra(KEY_EMAIL, email);
        i.putExtra(KEY_PASSWORD, password);
        i.putExtra(KEY_USERNAME, username);
        view.getContext().startActivity(i);
    }

    private void initSubmitBtn() {
        btnregistersubmit = findViewById(R.id.btn_register_submit);
        btnregistersubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                registerUser(view);
            }
        });
    }
}
