package com.mobdeve.s11.pokeplan;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {
    private ImageButton btnregisterback;
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

        initBackBtn();
        initSubmitBtn();

        this.etName = findViewById(R.id.et_register_name);
        this.etEmail = findViewById(R.id.et_register_email);
        this.etUsername = findViewById(R.id.et_register_username);
        this.etPassword = findViewById(R.id.et_register_password);
        this.etBirthday = findViewById(R.id.et_register_birthday);

        initCalendar();
    }

    private void initCalendar () {
        Calendar calendarStart = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateStart = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month,
                                  int day) {
                calendarStart.set(Calendar.YEAR, year);
                calendarStart.set(Calendar.MONTH, month);
                calendarStart.set(Calendar.DAY_OF_MONTH, day);

                String myFormat = "dd.MM.yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ROOT);

                etBirthday.setText(sdf.format(calendarStart.getTime()));
            }
        };

        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int year = Calendar.getInstance().get(Calendar.YEAR);

        int finalSYear = year;
        int finalSDay = day;
        int finalSMonth = month;
        etBirthday.setOnClickListener(v -> new DatePickerDialog(
                RegisterActivity.this, dateStart, finalSYear, finalSMonth, finalSDay).show());
    }

    private void initBackBtn() {
        btnregisterback = findViewById(R.id.ib_register_back);
        btnregisterback.setOnClickListener(view -> onBackPressed());
    }

    private void registerUser (View view) {
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
        } else if (password.length() < 6) {
            etPassword.setError("Password length should be at least six characters.");
            etPassword.requestFocus();
            return;
        }

        if (birthday.isEmpty()) {
            etBirthday.setError("Birthday is a required field!");
            etBirthday.requestFocus();
            return;
        }

        Intent i = new Intent(view.getContext(), RegisterStarterActivity.class);
        i.putExtra(Keys.KEY_NAME.name(), name);
        i.putExtra(Keys.KEY_EMAIL.name(), email);
        i.putExtra(Keys.KEY_PASSWORD.name(), password);
        i.putExtra(Keys.KEY_USERNAME.name(), username);
        i.putExtra(Keys.KEY_BIRTHDAY.name(), birthday);

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
