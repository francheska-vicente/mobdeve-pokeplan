package com.mobdeve.s11.pokeplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnfocus;
    private Button btntask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnfocus = findViewById(R.id.btn_focus);
        btnfocus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), FocusTimerActivity.class);
                view.getContext().startActivity(i);
            }
        });

        btntask = findViewById(R.id.btn_tasklist);
        btntask.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), TaskListActivity.class);
                view.getContext().startActivity(i);
            }
        });
    }
}