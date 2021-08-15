package com.mobdeve.s11.pokeplan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private ImageButton btnloginback;
    private Button btnloginsubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initBackBtn();
        initSubmitBtn();
    }

    private void initBackBtn() {
        btnloginback = findViewById(R.id.ib_login_back);
        btnloginback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), MainActivity.class);
                view.getContext().startActivity(i);
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
