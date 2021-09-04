package com.mobdeve.s11.pokeplan.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s11.pokeplan.R;

import org.jetbrains.annotations.NotNull;

public class PokemonPartyViewHolder extends RecyclerView.ViewHolder {
    private ImageView ivpkmnicon;
    private TextView tvpkmnnickname;
    private TextView tvpkmnlevel;
    private ConstraintLayout layout;

    public PokemonPartyViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        this.ivpkmnicon = itemView.findViewById(R.id.iv_pokemon_icon);
        this.tvpkmnnickname = itemView.findViewById(R.id.tv_pokemon_nickname);
        this.tvpkmnlevel = itemView.findViewById(R.id.tv_pokemon_level);
        this.layout = itemView.findViewById(R.id.cl_template_pokemon);
    }

    public void setPkmnIcon(int dexnum) {
        int pic = getImageId(itemView.getContext(), "pkmn_" + dexnum);
        this.ivpkmnicon.setImageResource(pic);
    }

    public void setPkmnNickname(String name) {
        this.tvpkmnnickname.setText(name);
    }

    public void setPkmnLevel(int level) {
        String text = "Level " + level;
        this.tvpkmnlevel.setText(text);
    }

    private int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }

    public ConstraintLayout getConstraintLayout() {
        return this.layout;
    }
}
