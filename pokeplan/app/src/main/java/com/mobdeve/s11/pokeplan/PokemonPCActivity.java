package com.mobdeve.s11.pokeplan;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PokemonPCActivity extends AppCompatActivity {
    private ArrayList<UserPokemon> pokemonPCList;
    private RecyclerView rvPokemonPC;
    private PokemonPCAdapter pcAdapter;

    private TextView tvNoPkmnPC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemonpc);
        this.initComponents();
    }

    private void initComponents() {
        this.pokemonPCList = UserSingleton.getUser().getUserPokemonPC();
        Log.d("rororo", Integer.toString(pokemonPCList.size()));
        this.rvPokemonPC = findViewById(R.id.rv_pkmnpc);
        this.tvNoPkmnPC = findViewById(R.id.tv_pkmnpc_nopkmnpc);

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

    @Override
    public void onResume() {
        super.onResume();

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initComponents();
            }
        }, 200);
    }
}
