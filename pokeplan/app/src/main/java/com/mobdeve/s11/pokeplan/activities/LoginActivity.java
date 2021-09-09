package com.mobdeve.s11.pokeplan.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;
import com.mobdeve.s11.pokeplan.R;
import com.mobdeve.s11.pokeplan.utils.Keys;
import com.mobdeve.s11.pokeplan.views.CustomDialog;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword;

    private FirebaseAuth mAuth;
    private ProgressBar pbLoading;
    private CustomDialog forgotPasswordDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        this.initComponents();

        this.sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        this.spEditor = this.sp.edit();
    }

    /**
     * Initializes the layout's components
     */
    private void initComponents() {
        etEmail = findViewById(R.id.et_login_email);
        etPassword = findViewById(R.id.et_login_password);

        pbLoading = findViewById(R.id.pb_login);
        pbLoading.setVisibility(View.GONE);

        btnLogin = findViewById(R.id.btn_login_submit);
        tvForgotPassword = findViewById(R.id.tv_login_forgotpass);
        setButtonListeners();
    }

    /**
     * Sets the onClickListeners for all buttons
     */
    private void setButtonListeners() {
        ImageButton btnloginback = findViewById(R.id.ib_login_back);
        btnloginback.setOnClickListener(view -> onBackPressed());

        btnLogin.setOnClickListener(v -> checkUserInput());
        tvForgotPassword.setOnClickListener(v -> createForgotPasswordDialog());
    }

    /**
     * Creates the input dialog for when the user wants to reset their password
     */
    private void createForgotPasswordDialog() {
        forgotPasswordDialog = new CustomDialog(this);
        forgotPasswordDialog.setDialogType(CustomDialog.ONE_INPUT);
        forgotPasswordDialog.setOneInputComponents(
                getString(R.string.forgot_title),
                R.drawable.email
        );

        EditText etEmail = forgotPasswordDialog.findViewById(R.id.et_dialog_oneinput);
        Button btndialogconfirm = forgotPasswordDialog.findViewById(R.id.btn_dialog_oneinput_ok);
        btndialogconfirm.setOnClickListener(v1 -> {
            resetPassword (etEmail);
            pbLoading.setVisibility(View.GONE);
        });

        forgotPasswordDialog.show();
    }

    /**
     * Resets the password of the user if email is valid
     * @param etEmail the EditText containing the user's email
     */
    private void resetPassword (EditText etEmail) {
        String email = etEmail.getText().toString().trim();
        if (email.isEmpty()) {
            etEmail.setError("Email is required.");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please input a valid email address.");
            etEmail.requestFocus();
            return;
        }

        pbLoading.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                forgotPasswordDialog.dismiss();
                Intent intent = new Intent(LoginActivity.this, InitActivity.class);
                startActivity(intent);
            } else {
                etEmail.setError("Email address does not match a registered user.");
                etEmail.requestFocus();
                return;
            }
        });
    }

    /**
     * Checks if email and password is valid
     */
    private void checkUserInput() {
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

        logInUser(email, password);

    }

    /**
     * Logs in the user if email is in the database and the password is correct
     */
    public void logInUser (String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                spEditor.putString(Keys.KEY_EMAIL.name(), email);
                spEditor.putString(Keys.KEY_PASSWORD.name(), password);
                spEditor.apply();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(LoginActivity.this, "Login failed. Please check your email and password.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }


}
