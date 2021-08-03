package com.mobdeve.s11.pokeplan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RegisterStarterActivity extends AppCompatActivity {
    private ImageButton btnregisterstartback;
    private ArrayList<ImageButton> btnspkmn;

    private static final int[] BUTTON_IDS = {
            R.id.ib_pkmn1, R.id.ib_pkmn2, R.id.ib_pkmn3, R.id.ib_pkmn4, R.id.ib_pkmn5,
            R.id.ib_pkmn6, R.id.ib_pkmn7, R.id.ib_pkmn8, R.id.ib_pkmn9, R.id.ib_pkmn10,
            R.id.ib_pkmn11, R.id.ib_pkmn12, R.id.ib_pkmn13, R.id.ib_pkmn14, R.id.ib_pkmn15,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_starter);

        initBackBtn();
        initPkmnBtns();
    }

    private void initBackBtn() {
        btnregisterstartback = findViewById(R.id.ib_register2_back);
        btnregisterstartback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), RegisterActivity.class);
                view.getContext().startActivity(i);
            }
        });
    }

    private void initPkmnBtns() {
        btnspkmn = new ArrayList<ImageButton>();
        for(int id : BUTTON_IDS) {
            ImageButton button = (ImageButton)findViewById(id);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(), FocusTimerStartActivity.class);
                    view.getContext().startActivity(i);
                }
            });
            btnspkmn.add(button);
        }
    }
}
