package com.mobdeve.s11.pokeplan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PokedexDetailsActivity extends AppCompatActivity {
    private ImageButton btnback;

    private ImageView ivPkmnIcon;
    private TextView tvPkmnSpecies;
    private TextView tvPkmnType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_details);

        initComponents();
        setAllComponents();
    }

    private void initComponents() {
        this.btnback = findViewById(R.id.ib_pkmndetails_back);
        this.btnback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });


        this.ivPkmnIcon = findViewById(R.id.iv_pkmndetails_icon);
        this.tvPkmnSpecies = findViewById(R.id.tv_pkmndetails_species);
        this.tvPkmnType = findViewById(R.id.tv_pkmndetails_type);
    }

    private void setAllComponents() {
        Intent intent = getIntent();
        String pkmnid = intent.getStringExtra(PokemonPartyAdapter.KEY_POKEMONID);
        UserPokemon pkmn = UserSingleton.getUser().getPokemonInParty(pkmnid);

        this.ivPkmnIcon.setImageResource(getImageId(getApplicationContext(),
                "pkmn_"+ pkmn.getPokemonDetails().getDexNum()));
        this.tvPkmnSpecies.setText(pkmn.getPokemonDetails().getSpecies());


        String pkmntype = pkmn.getPokemonDetails().getType1();
        if (pkmn.getPokemonDetails().getType2() != null)
            pkmntype = pkmntype + "/" + pkmn.getPokemonDetails().getType2();
        this.tvPkmnType.setText(pkmntype);
    }

    private int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }
}