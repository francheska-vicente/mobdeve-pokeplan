package com.mobdeve.s11.pokeplan.viewholders;

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

public class PokedexViewHolder extends RecyclerView.ViewHolder {
    private ImageView ivpkmnicon;
    private ImageView ivcirclebg;
    private TextView tvpkmndexnum;
    private ConstraintLayout layout;

    /**
     * Class constructor
     * @param itemView the layout of a specific item
     */
    public PokedexViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        this.ivpkmnicon = itemView.findViewById(R.id.iv_pokedex_icon);
        this.ivcirclebg = itemView.findViewById(R.id.iv_pokedex_circlebg);
        this.tvpkmndexnum = itemView.findViewById(R.id.tv_pokedex_dexnum);
        this.layout = itemView.findViewById(R.id.cl_template_pokedex);
    }

    /**
     * Sets the pokemon's icon; if not yet caught, the pokemon would be 'hidden'
     * @param dexnum the pokemon's pokedex number
     * @param caught if true, the user has caught the pokemon
     */
    public void setPkmnIcon(int dexnum, boolean caught) {
        int pic = getImageId("pkmn_" + dexnum);
        this.ivpkmnicon.setImageResource(pic);

        if (!caught) {
            ImageViewCompat.setImageTintList(ivpkmnicon,
                    ColorStateList.valueOf(
                            ContextCompat.getColor(itemView.getContext(), R.color.darkest_gray)));
            ImageViewCompat.setImageTintList(ivcirclebg,
                    ColorStateList.valueOf(
                            ContextCompat.getColor(itemView.getContext(), R.color.lighter_gray)));
        }
    }

    /**
     * Enables or disables the layout depending if the pokemon was caught before
     * @param caught if true, the user has caught the pokemon
     */
    public void setButtonEnabled(boolean caught) {
        layout.setEnabled(caught);
    }

    /**
     * Sets the pokedex number of the pokemon
     * @param dexnum the pokemon's pokedex number
     */
    public void setDexNum(int dexnum) {
        String text = "#" + dexnum;
        this.tvpkmndexnum.setText(text);
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
