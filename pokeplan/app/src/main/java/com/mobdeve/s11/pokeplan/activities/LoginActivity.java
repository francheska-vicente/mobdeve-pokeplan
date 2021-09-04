package com.mobdeve.s11.pokeplan.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mobdeve.s11.pokeplan.R;
import com.mobdeve.s11.pokeplan.utils.Keys;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    private ImageButton btnloginback;
    private Button btnloginsubmit;

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword;

    private FirebaseAuth mAuth;
    private ProgressBar pbLoading;
    private Dialog forgotPasswordDialog;
    private Dialog emailSentDialog;
    private boolean checkerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkerEmail = false;
        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.et_login_email);
        etPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login_submit);
        pbLoading = findViewById(R.id.pb_login);
        tvForgotPassword = findViewById(R.id.tv_login_forgotpass);
        pbLoading.setVisibility(View.GONE);
        initBackBtn();
        initSubmitBtn();

        btnLogin.setOnClickListener(v -> userLogin ());

        tvForgotPassword.setOnClickListener(v -> forgotPassword (v));

        this.sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        this.spEditor = this.sp.edit();
    }

    private void forgotPassword (View v) {
        forgotPasswordDialog = new Dialog(v.getContext());

        forgotPasswordDialog.setContentView(R.layout.dialog_oneinput);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.40);

        forgotPasswordDialog.getWindow().setLayout(width, height);
        forgotPasswordDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tvdialogtitle = (TextView) forgotPasswordDialog.findViewById(R.id.iv_dialog_stringinput_title);
        tvdialogtitle.setText(R.string.forgot_title);
        EditText etEmail = (EditText)  forgotPasswordDialog.findViewById(R.id.et_dialog_stringinput);
        ImageView ivdialogicon = (ImageView) forgotPasswordDialog.findViewById(R.id.iv_dialog_stringinput_icon);
        ivdialogicon.setImageResource(R.drawable.warning);

        Button btndialogcancel = (Button) forgotPasswordDialog.findViewById(R.id.btn_dialog_stringinput_confirm);
        btndialogcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPasswordDialog.dismiss();
            }
        });

        Button btndialogconfirm = (Button) forgotPasswordDialog.findViewById(R.id.btn_dialog_stringinput_ok);
        btndialogconfirm.setText(R.string.forgot_button);
        btndialogconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword (etEmail);

                pbLoading.setVisibility(View.GONE);
            }
        });
        forgotPasswordDialog.show();
    }

    private void resetPassword (EditText etEmail) {
        String email = etEmail.getText().toString().trim();
        checkerEmail = false;
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
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    forgotPasswordDialog.dismiss();
                    Intent intent = new Intent(LoginActivity.this, InitActivity.class);
                    startActivity(intent);
                } else {
                    etEmail.setError("Please input a valid email address.");
                    etEmail.requestFocus();
                    return;
                }
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

        logInUser(email, password);

    }

    public void logInUser (String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                       spEditor.putString(Keys.KEY_EMAIL.name(), email);
                       spEditor.putString(Keys.KEY_PASSWORD.name(), password);
                       spEditor.apply();

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
