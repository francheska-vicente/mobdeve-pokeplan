package com.mobdeve.s11.pokeplan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.mobdeve.s11.pokeplan.R;
import com.mobdeve.s11.pokeplan.utils.Keys;
import com.mobdeve.s11.pokeplan.views.CustomDatePicker;

public class RegisterActivity extends AppCompatActivity {
    private Button btnregistersubmit;

    private EditText etName;
    private EditText etEmail;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.initComponents();
    }

    /**
     * Initializes the components of the layout
     */
    private void initComponents() {
        this.btnregistersubmit = findViewById(R.id.btn_register_submit);
        this.setButtonListeners();

        this.etName = findViewById(R.id.et_register_name);
        this.etEmail = findViewById(R.id.et_register_email);
        this.etUsername = findViewById(R.id.et_register_username);
        this.etPassword = findViewById(R.id.et_register_password);
        this.etBirthday = findViewById(R.id.et_register_birthday);
        new CustomDatePicker().createDatePicker(this, etBirthday, "");
    }

    /**
     * Sets the OnClickListeners of all buttons
     */
    private void setButtonListeners() {
        ImageButton btnregisterback = findViewById(R.id.ib_register_back);
        btnregisterback.setOnClickListener(view -> onBackPressed());
        btnregistersubmit.setOnClickListener(view -> registerUser());
    }

    /**
     * Verifies the user input and registers the user
     */
    private void registerUser() {
        String name = this.etName.getText().toString().trim();
        String email = this.etEmail.getText().toString().trim();
        String username = this.etUsername.getText().toString().trim();
        String password = this.etPassword.getText().toString().trim();
        String birthday = this.etBirthday.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Name is required.");
            etName.requestFocus();
            return;
        }

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
        else if (password.length() < 6) {
            etPassword.setError("Password length should be at least six characters.");
            etPassword.requestFocus();
            return;
        }

        if (birthday.isEmpty()) {
            etBirthday.setError("Birthday is a required field!");
            etBirthday.requestFocus();
            return;
        }

        Intent i = new Intent(this, RegisterStarterActivity.class);
        i.putExtra(Keys.KEY_NAME.name(), name);
        i.putExtra(Keys.KEY_EMAIL.name(), email);
        i.putExtra(Keys.KEY_PASSWORD.name(), password);
        i.putExtra(Keys.KEY_USERNAME.name(), username);
        i.putExtra(Keys.KEY_BIRTHDAY.name(), birthday);

        this.startActivity(i);
    }
}
