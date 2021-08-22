package com.mobdeve.s11.pokeplan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

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
            Intent i = new Intent(this, MainActivity.class);
            this.startActivity(i);
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
