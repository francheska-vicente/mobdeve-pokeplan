package com.mobdeve.s11.pokeplan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mobdeve.s11.pokeplan.R;
import com.mobdeve.s11.pokeplan.data.DatabaseHelper;
import com.mobdeve.s11.pokeplan.models.CustomDate;
import com.mobdeve.s11.pokeplan.models.Pokedex;
import com.mobdeve.s11.pokeplan.models.Pokemon;
import com.mobdeve.s11.pokeplan.models.UserDetails;
import com.mobdeve.s11.pokeplan.utils.Keys;

import java.util.ArrayList;

public class RegisterStarterActivity extends AppCompatActivity {
    private ProgressBar pbLoading;

    private static final int[] BUTTON_IDS = {
            R.id.ib_pkmn1, R.id.ib_pkmn2, R.id.ib_pkmn3, R.id.ib_pkmn4, R.id.ib_pkmn5,
            R.id.ib_pkmn6, R.id.ib_pkmn7, R.id.ib_pkmn8, R.id.ib_pkmn9, R.id.ib_pkmn10,
            R.id.ib_pkmn11, R.id.ib_pkmn12, R.id.ib_pkmn13, R.id.ib_pkmn14, R.id.ib_pkmn15,
    };

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

        databaseHelper = new DatabaseHelper(false);

        pbLoading = findViewById(R.id.pb_register_starter);
        pbLoading.setVisibility(View.GONE);
        initValues ();
        initBackBtn();
        initPkmnBtns();
    }

    /**
     * Gets the values set by the user in the previous activity
     */
    private void initValues () {
        Intent intent = getIntent();
        this.name =  intent.getStringExtra(Keys.KEY_NAME.name());
        this.email = intent.getStringExtra(Keys.KEY_EMAIL.name());
        this.password = intent.getStringExtra(Keys.KEY_PASSWORD.name());
        this.username = intent.getStringExtra(Keys.KEY_USERNAME.name());
        this.birthday = intent.getStringExtra(Keys.KEY_BIRTHDAY.name());
    }

    /**
     * Initializes and sets the listener of the back button
     */
    private void initBackBtn() {
        ImageButton btnregisterstartback = findViewById(R.id.ib_register_s_back);
        btnregisterstartback.setOnClickListener(view -> onBackPressed());
    }

    /**
     * Initializes and sets the listeners of the starter pokemon buttons
     */
    private void initPkmnBtns() {
        ArrayList<ImageButton> btnspkmn = new ArrayList<>();
        for(int id : BUTTON_IDS) {
            ImageButton button = findViewById(id);
            button.setOnClickListener(view -> {
                int pokeNum = Integer.valueOf((String) view.getTag ());
                pbLoading.setVisibility(View.VISIBLE);
                registerUser (pokeNum);
            });
            btnspkmn.add(button);
        }
    }

    /**
     * Registers the user and adds the user information to the database
     * @param pokeNum the pokedex number of the chosen starter pokemon
     */
    private void registerUser (int pokeNum) {
        String [] stringBirthday = birthday.split("\\.");

        int month = Integer.parseInt(stringBirthday[1]);
        int day = Integer.parseInt(stringBirthday[0]);
        int year = Integer.parseInt(stringBirthday[2]);

        CustomDate customDate = new CustomDate(year, month, day, 0, 0);

        UserDetails user = new UserDetails (name, email, username, pokeNum, customDate);
        user.setCaught(pokeNum);

        databaseHelper.addUser((userDetails, isSuccessful, message) -> {
            if (isSuccessful) {
                Pokemon pokemon = Pokedex.getPokedex().getPokemon(pokeNum);

                databaseHelper.addPokemon((list, isSuccessful1, message1) -> {
                    if (isSuccessful1) {
                        Toast.makeText(RegisterStarterActivity.this, "User's information has been registered!",
                                Toast.LENGTH_LONG).show();
                        pbLoading.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(RegisterStarterActivity.this, "User's pokemon information has not been registered!",
                                Toast.LENGTH_LONG).show();
                        pbLoading.setVisibility(View.GONE);
                    }
                }, false, pokemon, user);
            } else {
                Toast.makeText(RegisterStarterActivity.this, "User's information has not been registered!",
                        Toast.LENGTH_LONG).show();
                pbLoading.setVisibility(View.GONE);
            }
        }, user, this.password);

        finish();
    }
}