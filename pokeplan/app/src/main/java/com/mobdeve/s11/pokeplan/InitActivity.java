package com.mobdeve.s11.pokeplan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InitActivity extends AppCompatActivity {
    private Button btninitregister;
    private Button btninitlogin;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String email = this.sp.getString(Keys.KEY_EMAIL.name(), null);
        String password = this.sp.getString(Keys.KEY_PASSWORD.name(), null);

        if (email != null && password != null) {
            LoginActivity login = new LoginActivity();

            if (login.logInUser(email, password)) {
                Intent intent = new Intent(InitActivity.this, MainActivity.class);

                startActivity(intent);
            } else {
                Toast.makeText(InitActivity.this, "Saved user does not exist in the database.", Toast.LENGTH_LONG).show();

                sp.edit().remove(Keys.KEY_EMAIL.name()).apply();
                sp.edit().remove(Keys.KEY_PASSWORD.name()).apply();
                Intent intent = new Intent(InitActivity.this, InitActivity.class);
                startActivity(intent);
            }
        }
        else {
            setContentView(R.layout.activity_init);
            btninitregister = findViewById(R.id.btn_init_register);
            btninitregister.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(), RegisterActivity.class);
                    view.getContext().startActivity(i);
                }
            });

            btninitlogin = findViewById(R.id.btn_init_login);
            btninitlogin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(), LoginActivity.class);
                    view.getContext().startActivity(i);
                }
            });
        }
    }
}
