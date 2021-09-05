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
import com.mobdeve.s11.pokeplan.viewholders.PokemonPCViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PokemonPCAdapter extends RecyclerView.Adapter<PokemonPCViewHolder> {
    private ArrayList<UserPokemon> pc;

    public PokemonPCAdapter () {
        pc = new ArrayList<>();
    }
    public PokemonPCAdapter(ArrayList<UserPokemon> pc) {
        this.pc = pc;
    }

    @NonNull
    @NotNull
    @Override
    public PokemonPCViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.template_pokemonlist, parent, false);

        PokemonPCViewHolder vh = new PokemonPCViewHolder(view);

        vh.getConstraintLayout().setOnClickListener(view1 -> {
            Intent i = new Intent(view1.getContext(), PokemonDetailsActivity.class);
            i.putExtra(Keys.KEY_POKEMONID.name(), pc.get(vh.getBindingAdapterPosition()).getUserPokemonID());
            i.putExtra(Keys.KEY_FROMWHERE.name(), "PC");
            view1.getContext().startActivity(i);
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PokemonPCViewHolder holder, int position) {
        holder.setPkmnIcon(this.pc.get(position).getDetails().getDexNum());
    }

    public void setPc (ArrayList<UserPokemon> userPokemons) {
        this.pc = userPokemons;
    }

    @Override
    public int getItemCount() {
        if(this.pc != null)
            return this.pc.size();
        return 0;
    }
}
