package com.mobdeve.s11.pokeplan;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class PokemonPCViewHolder extends RecyclerView.ViewHolder {
    private ImageView ivpkmnicon;
    private ImageView ivcirclebg;
    private ConstraintLayout layout;

    public PokemonPCViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        this.ivpkmnicon = itemView.findViewById(R.id.iv_pokedex_icon);
        this.ivcirclebg = itemView.findViewById(R.id.iv_pokedex_circlebg);
        this.layout = itemView.findViewById(R.id.cl_template_pokedex);
    }

    public void setPkmnIcon(int dexnum, boolean caught) {
        int pic = getImageId(itemView.getContext(), "pkmn_" + dexnum);
        this.ivpkmnicon.setImageResource(pic);
    }

    private int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName,
                null, context.getPackageName());
    }

    public ConstraintLayout getConstraintLayout() {
        return this.layout;
    }
}