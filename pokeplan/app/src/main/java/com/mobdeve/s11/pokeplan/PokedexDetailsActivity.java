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

        initComponents();
        setAllComponents();
    }

    private void initComponents() {
        this.btnback = findViewById(R.id.ib_pkdexdetails_back);
        this.btnback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });

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

    private void setAllComponents() {
        Intent intent = getIntent();
        int dexnum = intent.getIntExtra(PokedexAdapter.KEY_POKEMONDEXNUM, 1);
        Pokemon pkmn = new Pokedex().getPokemon(dexnum);

        this.ivPkmnIcon.setImageResource(getImageId(getApplicationContext(),
                "pkmn_"+ pkmn.getDexNum()));
        this.tvPkmnSpecies.setText(pkmn.getSpecies());

        String pkmndexnum = "#" + String.format("%03d", dexnum);
        this.tvDexNum.setText(pkmndexnum);

        String pkmntype = pkmn.getType1();
        if (!pkmn.getType2().isEmpty())
            pkmntype = pkmntype + "/" + pkmn.getType2();
        this.tvPkmnType.setText(pkmntype);

        String pkmninfo = new PokedexInfo().getPokemonInfo(pkmn.getDexNum());
        this.tvPkmnInfo.setText(pkmninfo);

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
            tvPkmnEvoSpecies.setText(new Pokedex().getPokemon(pkmn.getDexNum()+1).getSpecies());

            String pkmnlevel = "Level " + pkmn.getEvolveLvl();
            tvEvoLevel.setText(pkmnlevel);
        }
    }



    private int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }
}