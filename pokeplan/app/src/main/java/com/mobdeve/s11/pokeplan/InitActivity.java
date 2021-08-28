package com.mobdeve.s11.pokeplan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

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
            checkIfInDB (email, password);

        }
        else {
            setContentView(R.layout.activity_init);
            btninitregister = findViewById(R.id.btn_init_register);
            btninitregister.setOnClickListener(view -> {
                Intent i = new Intent(view.getContext(), RegisterActivity.class);
                view.getContext().startActivity(i);
            });

            btninitlogin = findViewById(R.id.btn_init_login);
            btninitlogin.setOnClickListener(view -> {
                Intent i = new Intent(view.getContext(), LoginActivity.class);
                view.getContext().startActivity(i);
            });
        }
    }

    private void checkIfInDB (String email, String password) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
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
        });
    }
}
