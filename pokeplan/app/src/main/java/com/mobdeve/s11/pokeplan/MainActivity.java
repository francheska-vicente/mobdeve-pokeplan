package com.mobdeve.s11.pokeplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private Button btnfocus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnfocus = findViewById(R.id.btn_focus);
        btnfocus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), FocusTimerStartActivity.class);
                view.getContext().startActivity(i);
            }
        });
    }
}