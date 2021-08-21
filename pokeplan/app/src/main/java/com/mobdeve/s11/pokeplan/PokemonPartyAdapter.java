package com.mobdeve.s11.pokeplan;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PokemonPartyAdapter extends RecyclerView.Adapter<PokemonPartyViewHolder> {
    private ArrayList<UserPokemon> party;

    public static final String KEY_POKEMONID = "KEY_POKEMONID";

    public PokemonPartyAdapter(ArrayList<UserPokemon> party) {
        this.party = party;
    }

    @NonNull
    @NotNull
    @Override
    public PokemonPartyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.template_pokemon, parent, false);

        PokemonPartyViewHolder vh = new PokemonPartyViewHolder(view);

        vh.getConstraintLayout().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), PokemonDetailsActivity.class);
                i.putExtra(KEY_POKEMONID, party.get(vh.getBindingAdapterPosition()).getUserPokemonID());
                view.getContext().startActivity(i);
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PokemonPartyViewHolder holder, int position) {
        holder.setPkmnIcon(this.party.get(position).getPokemonDetails().getDexNum());
        holder.setPkmnNickname(this.party.get(position).getNickname());
        holder.setPkmnLevel(this.party.get(position).getLevel());
    }

    @Override
    public int getItemCount() {
        return this.party.size();
    }
}
