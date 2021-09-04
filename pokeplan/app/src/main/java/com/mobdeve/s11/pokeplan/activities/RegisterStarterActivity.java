package com.mobdeve.s11.pokeplan.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mobdeve.s11.pokeplan.R;
import com.mobdeve.s11.pokeplan.data.DatabaseHelper;
import com.mobdeve.s11.pokeplan.data.FirebaseCallbackPokemon;
import com.mobdeve.s11.pokeplan.data.FirebaseCallbackUser;
import com.mobdeve.s11.pokeplan.models.CustomDate;
import com.mobdeve.s11.pokeplan.models.Pokedex;
import com.mobdeve.s11.pokeplan.models.Pokemon;
import com.mobdeve.s11.pokeplan.models.UserDetails;
import com.mobdeve.s11.pokeplan.models.UserPokemon;
import com.mobdeve.s11.pokeplan.utils.Keys;

import java.util.ArrayList;

public class RegisterStarterActivity extends AppCompatActivity {
    private ImageButton btnregisterstartback;
    private ArrayList<ImageButton> btnspkmn;
    private ProgressBar pbLoading;

    private static final int[] BUTTON_IDS = {
            R.id.ib_pkmn1, R.id.ib_pkmn2, R.id.ib_pkmn3, R.id.ib_pkmn4, R.id.ib_pkmn5,
            R.id.ib_pkmn6, R.id.ib_pkmn7, R.id.ib_pkmn8, R.id.ib_pkmn9, R.id.ib_pkmn10,
            R.id.ib_pkmn11, R.id.ib_pkmn12, R.id.ib_pkmn13, R.id.ib_pkmn14, R.id.ib_pkmn15,
    };

    private Dialog successDialog;

    private String name;
    private String email;
    private String password;
    private String username;
    private String birthday;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_starter);

        databaseHelper = new DatabaseHelper();

        pbLoading = findViewById(R.id.pb_register_starter);
        pbLoading.setVisibility(View.GONE);
        initValues ();
        initBackBtn();
        initPkmnBtns();
    }

    private void initValues () {
        Intent intent = getIntent();
        this.name =  intent.getStringExtra(Keys.KEY_NAME.name());
        this.email = intent.getStringExtra(Keys.KEY_EMAIL.name());
        this.password = intent.getStringExtra(Keys.KEY_PASSWORD.name());
        this.username = intent.getStringExtra(Keys.KEY_USERNAME.name());
        this.birthday = intent.getStringExtra(Keys.KEY_BIRTHDAY.name());
    }

    private void initBackBtn() {
        btnregisterstartback = findViewById(R.id.ib_register_s_back);
        btnregisterstartback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), RegisterActivity.class);
                view.getContext().startActivity(i);
            }
        });
    }

    private void registerUser (int pokeNum) {
        String [] stringBirthday = birthday.split("\\.");

        int month = Integer.parseInt(stringBirthday[1]);
        int day = Integer.parseInt(stringBirthday[0]);
        int year = Integer.parseInt(stringBirthday[2]);

        CustomDate customDate = new CustomDate(year, month, day, 0, 0);

        UserDetails user = new UserDetails (name, email, username, pokeNum, customDate);
        user.setCaught(pokeNum);

        databaseHelper.addUser(new FirebaseCallbackUser() {
            @Override
            public void onCallbackUser(UserDetails userDetails, Boolean isSuccessful, String message) {
                if (isSuccessful) {
                    Pokemon pokemon = Pokedex.getPokedex().getPokemon(pokeNum);

                    databaseHelper.addPokemon(new FirebaseCallbackPokemon() {
                        @Override
                        public void onCallbackPokemon(ArrayList<UserPokemon> list, Boolean isSuccessful, String message) {
                            if (isSuccessful) {
                                Toast.makeText(RegisterStarterActivity.this, "User's information has been registered!",
                                        Toast.LENGTH_LONG).show();
                                pbLoading.setVisibility(View.GONE);
                                finish();

                                Intent intent = new Intent(RegisterStarterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(RegisterStarterActivity.this, "User's pokemon information has not been registered!",
                                        Toast.LENGTH_LONG).show();
                                pbLoading.setVisibility(View.GONE);
                            }
                        }
                    }, false, pokemon, user);
                } else {
                    Toast.makeText(RegisterStarterActivity.this, "User's information has not been registered!",
                            Toast.LENGTH_LONG).show();
                    pbLoading.setVisibility(View.GONE);
                }
            }
        }, user, this.password);

        Intent intent = new Intent(RegisterStarterActivity.this, InitActivity.class);
        startActivity(intent);
    }

    private void initPkmnBtns() {
        btnspkmn = new ArrayList<>();
        for(int id : BUTTON_IDS) {
            ImageButton button = findViewById(id);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    int pokeNum = Integer.valueOf((String) view.getTag ());
                    pbLoading.setVisibility(View.VISIBLE);
                    registerUser (pokeNum);
                }
            });
            btnspkmn.add(button);
        }
    }
}