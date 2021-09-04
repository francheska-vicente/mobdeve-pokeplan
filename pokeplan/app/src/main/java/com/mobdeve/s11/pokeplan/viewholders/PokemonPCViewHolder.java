package com.mobdeve.s11.pokeplan.viewholders;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s11.pokeplan.R;

import org.jetbrains.annotations.NotNull;

public class PokemonPCViewHolder extends RecyclerView.ViewHolder {
    private ImageView ivpkmnicon;
    private ImageView ivcirclebg;
    private TextView tvpkmndexnum;
    private ConstraintLayout layout;

    public PokemonPCViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        this.ivpkmnicon = itemView.findViewById(R.id.iv_pokedex_icon);
        this.ivcirclebg = itemView.findViewById(R.id.iv_pokedex_circlebg);
        this.tvpkmndexnum = itemView.findViewById(R.id.tv_pokedex_dexnum);
        this.layout = itemView.findViewById(R.id.cl_template_pokedex);
    }

    public void setPkmnIcon(int dexnum) {
        int pic = getImageId(itemView.getContext(), "pkmn_" + dexnum);
        this.ivpkmnicon.setImageResource(pic);

        ImageViewCompat.setImageTintList(ivcirclebg,
                ColorStateList.valueOf(
                        ContextCompat.getColor(itemView.getContext(), R.color.lighter_gray)));
    }

    public void setDexNum() {
        this.tvpkmndexnum.setVisibility(View.GONE);
    }

    private int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName,
                null, context.getPackageName());
    }

    public ConstraintLayout getConstraintLayout() {
        return this.layout;
    }
}
