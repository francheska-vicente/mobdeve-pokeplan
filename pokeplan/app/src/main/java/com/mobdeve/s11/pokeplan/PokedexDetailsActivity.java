package com.mobdeve.s11.pokeplan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class PokedexDetailsActivity extends AppCompatActivity {
    private ImageButton btnback;

    private ImageView ivPkmnIcon;
    private TextView tvPkmnSpecies;
    private TextView tvPkmnType;
    private TextView tvDexNum;

    private TextView tvPkmnInfo;

    private ImageView ivPkmnOGIcon;
    private TextView tvPkmnOGSpecies;
    private ImageView ivPkmnEvoIcon;
    private TextView tvPkmnEvoSpecies;
    private TextView tvEvoLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokedex_details);

        Intent intent = getIntent();
        int dexnum = intent.getIntExtra(Keys.KEY_POKEMONDEXNUM.name(), 1);
        Pokemon pkmn = Pokedex.getPokedex().getPokemon(dexnum);

        initComponents();
        setAllComponents(pkmn);
        pkmn.playPokemonCry();
    }

    /**
     * Initializes the layout's components
     */
    private void initComponents() {
        this.btnback = findViewById(R.id.ib_pkdexdetails_back);
        this.btnback.setOnClickListener(view -> onBackPressed());

        this.ivPkmnIcon = findViewById(R.id.iv_pkdexdetails_icon);
        this.tvPkmnSpecies = findViewById(R.id.tv_pkdexdetails_species);
        this.tvPkmnType = findViewById(R.id.tv_pkdexdetails_type);
        this.tvDexNum = findViewById(R.id.tv_pkdexdetails_dexnum);

        this.tvPkmnInfo = findViewById(R.id.tv_pkdexdetails_info);

        this.ivPkmnOGIcon = findViewById(R.id.iv_pkdexdetails_evolveto_ogicon);
        this.tvPkmnOGSpecies = findViewById(R.id.tv_pkdexdetails_evolveto_ogspecies);
        this.ivPkmnEvoIcon = findViewById(R.id.iv_pkdexdetails_evolveto_evoicon);
        this.tvPkmnEvoSpecies = findViewById(R.id.tv_pkdexdetails_evolveto_evospecies);
        this.tvEvoLevel = findViewById(R.id.tv_pkdexdetails_evolveto_level);
    }

    /**
     * Sets values for all TextViews and ImageViews
     */
    private void setAllComponents(Pokemon pkmn) {
        // sets pokemon icon
        this.ivPkmnIcon.setImageResource(getImageId(getApplicationContext(),
                "pkmn_"+ pkmn.getDexNum()));

        // sets pokemon species
        this.tvPkmnSpecies.setText(pkmn.getSpecies());

        // sets pokemon pokedex number
        String pkmndexnum = "#" + String.format(Locale.getDefault(),"%03d", pkmn.getDexNum());
        this.tvDexNum.setText(pkmndexnum);

        // sets pokemon type or types
        String pkmntype = pkmn.getType1();
        if (!pkmn.getType2().isEmpty())
            pkmntype = pkmntype + "/" + pkmn.getType2();
        this.tvPkmnType.setText(pkmntype);

        // sets pokemon pokedex entries
        String pkmninfo = Pokedex.getPokedex().getPokemonInfo(pkmn.getDexNum());
        this.tvPkmnInfo.setText(pkmninfo);

        // sets evolution info if pokemon can evolve
        if (pkmn.getEvolvesTo().equals("")) {
            TextView tvlabel = findViewById(R.id.tv_pkdexdetails_evolveto_label);
            ImageView ivarrow = findViewById(R.id.iv_evolveto_arrow);
            tvlabel.setVisibility(View.GONE);
            ivarrow.setVisibility(View.GONE);

            ivPkmnOGIcon.setVisibility(View.GONE);
            tvPkmnOGSpecies.setVisibility(View.GONE);
            ivPkmnEvoIcon.setVisibility(View.GONE);
            tvPkmnEvoSpecies.setVisibility(View.GONE);
            tvEvoLevel.setVisibility(View.GONE);
        }
        else {
            ivPkmnOGIcon.setImageResource(getImageId(getApplicationContext(),
                    "pkmn_"+ pkmn.getDexNum()));
            tvPkmnOGSpecies.setText(pkmn.getSpecies());
            ivPkmnEvoIcon.setImageResource(getImageId(getApplicationContext(),
                    "pkmn_"+ (pkmn.getDexNum()+1)));
            tvPkmnEvoSpecies.setText(Pokedex.getPokedex().getPokemon(pkmn.getDexNum()+1).getSpecies());

            String pkmnlevel = "Level " + pkmn.getEvolveLvl();
            tvEvoLevel.setText(pkmnlevel);
        }
    }

    /**
     * Helper function to get Image ID from Image name
     */
    private int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }
}