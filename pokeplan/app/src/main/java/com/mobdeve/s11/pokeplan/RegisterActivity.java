package com.mobdeve.s11.pokeplan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private ImageButton btnregisterback;
    private Button btnregistersubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initBackBtn();
        initSubmitBtn();
    }

    private void initBackBtn() {
        btnregisterback = findViewById(R.id.ib_register_back);
        btnregisterback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), MainActivity.class);
                view.getContext().startActivity(i);
            }
        });
    }

    private void initSubmitBtn() {
        btnregistersubmit = findViewById(R.id.btn_register_submit);
        btnregistersubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), RegisterStarterActivity.class);
                view.getContext().startActivity(i);
            }
        });
    }
}
