package com.mobdeve.s11.pokeplan;

import android.content.Intent;
import android.os.Debug;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PokedexAdapter extends RecyclerView.Adapter<PokedexViewHolder> {
    private ArrayList<Boolean> pokedex;
    private ArrayList<Pokemon> pokemonList;

    public static final String KEY_POKEMONDEXNUM = "KEY_POKEMONDEXNUM";

    public PokedexAdapter(ArrayList<Boolean> pokedex) {
        this.pokedex = pokedex;
        pokemonList = Pokedex.getPokedex().getAllPokemon();
    }

    @NonNull
    @NotNull
    @Override
    public PokedexViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.template_pokemonlist, parent, false);

        PokedexViewHolder vh = new PokedexViewHolder(view);

        vh.getConstraintLayout().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), PokedexDetailsActivity.class);
                i.putExtra(KEY_POKEMONDEXNUM,
                        pokemonList.get(vh.getBindingAdapterPosition()).getDexNum());
                view.getContext().startActivity(i);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PokedexViewHolder holder, int position) {
        holder.setPkmnIcon(this.pokemonList.get(position).getDexNum(), pokedex.get(position));
        holder.setDexNum(this.pokemonList.get(position).getDexNum());
        holder.setButtonEnabled(pokedex.get(position));
    }

    @Override
    public int getItemCount() {
        return this.pokemonList.size();
    }
}
