package com.mobdeve.s11.pokeplan.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s11.pokeplan.R;
import com.mobdeve.s11.pokeplan.activities.PokemonDetailsActivity;
import com.mobdeve.s11.pokeplan.models.UserPokemon;
import com.mobdeve.s11.pokeplan.utils.Keys;
import com.mobdeve.s11.pokeplan.viewholders.PokemonPartyViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PokemonPartyAdapter extends RecyclerView.Adapter<PokemonPartyViewHolder> {
    private ArrayList<UserPokemon> party;

    public PokemonPartyAdapter() {
        this.party = new ArrayList<>();
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
                i.putExtra(Keys.KEY_POKEMONID.name(), party.get(vh.getBindingAdapterPosition()).getUserPokemonID());
                i.putExtra(Keys.KEY_FROMWHERE.name(), "PARTY");
                view.getContext().startActivity(i);
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PokemonPartyViewHolder holder, int position) {
        holder.setPkmnIcon(this.party.get(position).getDetails().getDexNum());
        holder.setPkmnNickname(this.party.get(position).getNickname());
        holder.setPkmnLevel(this.party.get(position).getLevel());
    }

    public void setPokemonParty (ArrayList<UserPokemon> userPokemons) {
        this.party = userPokemons;
        this.notifyItemRangeInserted(0, party.size());
    }

    @Override
    public int getItemCount() {
        return this.party.size();
    }
}
