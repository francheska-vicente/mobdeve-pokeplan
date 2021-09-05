package com.mobdeve.s11.pokeplan.viewholders;

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

    /**
     * Class constructor
     * @param itemView the layout of a specific item
     */
    public PokemonPartyViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        this.ivpkmnicon = itemView.findViewById(R.id.iv_pokemon_icon);
        this.tvpkmnnickname = itemView.findViewById(R.id.tv_pokemon_nickname);
        this.tvpkmnlevel = itemView.findViewById(R.id.tv_pokemon_level);
        this.layout = itemView.findViewById(R.id.cl_template_pokemon);
    }

    /**
     * Sets the pokemon's icon
     * @param dexnum the pokemon's pokedex number
     */
    public void setPkmnIcon(int dexnum) {
        int pic = getImageId("pkmn_" + dexnum);
        this.ivpkmnicon.setImageResource(pic);
    }

    /**
     * Sets the pokemon's nickname
     * @param name the pokemon's nickname
     */
    public void setPkmnNickname(String name) {
        this.tvpkmnnickname.setText(name);
    }

    /**
     * Sets the pokemon's level
     * @param level the pokemon's level
     */
    public void setPkmnLevel(int level) {
        String text = "Level " + level;
        this.tvpkmnlevel.setText(text);
    }

    /**
     * Helper function to get the image ID given the image name.
     * @param imageName the name of the image
     * @return the image id of the image
     */
    private int getImageId(String imageName) {
        return itemView.getContext().getResources().getIdentifier("drawable/" + imageName,
                null, itemView.getContext().getPackageName());
    }

    /**
     * @return the ConstraintLayout of the template
     */
    public ConstraintLayout getConstraintLayout() {
        return this.layout;
    }
}
