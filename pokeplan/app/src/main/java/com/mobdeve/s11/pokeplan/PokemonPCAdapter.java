package com.mobdeve.s11.pokeplan;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PokemonPCAdapter extends RecyclerView.Adapter<PokemonPCViewHolder> {
    private ArrayList<UserPokemon> pc;

    public static final String KEY_POKEMONID = "KEY_POKEMONID";

    public PokemonPCAdapter(ArrayList<UserPokemon> party) {
        this.pc = pc;
    }

    @NonNull
    @NotNull
    @Override
    public PokemonPCViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.template_pokemonlist, parent, false);

        PokemonPCViewHolder vh = new PokemonPCViewHolder(view);

        vh.getConstraintLayout().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), PokemonDetailsActivity.class);
                i.putExtra(KEY_POKEMONID, pc.get(vh.getBindingAdapterPosition()).getUserPokemonID());
                view.getContext().startActivity(i);
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PokemonPCViewHolder holder, int position) {
        holder.setPkmnIcon(this.pc.get(position).getPokemonDetails().getDexNum());
    }

    @Override
    public int getItemCount() {
        return this.pc.size();
    }
}
