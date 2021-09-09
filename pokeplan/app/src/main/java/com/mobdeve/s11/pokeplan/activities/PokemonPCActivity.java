package com.mobdeve.s11.pokeplan.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s11.pokeplan.R;
import com.mobdeve.s11.pokeplan.adapters.PokemonPCAdapter;
import com.mobdeve.s11.pokeplan.data.DatabaseHelper;
import com.mobdeve.s11.pokeplan.models.UserPokemon;

import java.util.ArrayList;

public class PokemonPCActivity extends AppCompatActivity {
    private ImageButton ibBack;

    private ArrayList<UserPokemon> pokemonPCList;
    private RecyclerView rvPokemonPC;
    private PokemonPCAdapter pcAdapter;
    private DatabaseHelper databaseHelper;

    private TextView tvNoPkmnPC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemonpc);
        databaseHelper = new DatabaseHelper(true);

        this.initComponents();
    }

    /**
     * Initializes all layout components
     */
    private void initComponents() {
        ibBack = findViewById(R.id.ib_pkmnpc_back);
        this.setButtonListeners();

        this.rvPokemonPC = findViewById(R.id.rv_pkmnpc);
        this.tvNoPkmnPC = findViewById(R.id.tv_pkmnpc_nopkmnpc);
        this.pcAdapter = new PokemonPCAdapter();

        this.initInfo();
    }

    /**
     * Retrieves pokemon information from the database
     */
    private void initInfo() {
        databaseHelper.getPokemon((list, isSuccessful, message) -> {
            pokemonPCList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).isInParty()) {
                    pokemonPCList.add(list.get(i));
                }
            }
            setLayoutComponents();
        });
    }

    /**
     * Sets all layout components
     */
    private void setLayoutComponents() {
        pcAdapter.setPc(pokemonPCList);
        if (this.pokemonPCList.size() > 0) {
            this.rvPokemonPC.setLayoutManager(new GridLayoutManager(this, 5));
            this.pcAdapter = new PokemonPCAdapter(this.pokemonPCList);
            this.rvPokemonPC.setAdapter(this.pcAdapter);

            this.rvPokemonPC.setVisibility(View.VISIBLE);
            this.tvNoPkmnPC.setVisibility(View.GONE);
        }
        else {
            this.rvPokemonPC.setVisibility(View.GONE);
            this.tvNoPkmnPC.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Sets the onClickListeners for all buttons
     */
    private void setButtonListeners() {
        ibBack.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void onResume() {
        super.onResume();
        initComponents();
    }
}
