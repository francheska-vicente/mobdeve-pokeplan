package com.mobdeve.s11.pokeplan.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.mobdeve.s11.pokeplan.R;
import com.mobdeve.s11.pokeplan.data.DatabaseHelper;
import com.mobdeve.s11.pokeplan.models.Pokemon;
import com.mobdeve.s11.pokeplan.models.UserDetails;
import com.mobdeve.s11.pokeplan.models.UserPokemon;
import com.mobdeve.s11.pokeplan.utils.Keys;
import com.mobdeve.s11.pokeplan.views.CustomDialog;

import java.util.ArrayList;

public class PokemonDetailsActivity extends AppCompatActivity {
    private String sourceActivity;

    private boolean soundEnabled;

    private ImageButton btnback;
    private ImageButton btnedit;

    private Button btnrare;
    private Button btnsuper;
    private Button btnpc;
    private Button btnRelease;

    private ConstraintLayout clComponents;
    private LinearLayout llrare;
    private LinearLayout llsuper;

    private ImageView ivPkmnIcon;
    private TextView tvPkmnNickname;
    private TextView tvPkmnSpecies;
    private TextView tvPkmnLevel;
    private TextView tvPkmnType;
    private TextView tvPkmnNature;
    private TextView tvPkmnMetDate;
    private ProgressBar pbPkmnLevel;

    private TextView tvRareCandyCtr;
    private TextView tvSuperCandyCtr;

    private CustomDialog editdialog;
    private CustomDialog releasePkmnDialog;
    private CustomDialog confirmDialog;

    private UserPokemon pkmn;
    private UserDetails user;
    private boolean pkmnWasDeleted;

    private ArrayList<UserPokemon> partyList;
    private DatabaseHelper databaseHelper;
    private ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_details);

        this.loadingScreen();
        this.initInfo();
    }

    /**
     * Sets the view components for when the data has not loaded yet
     */
    private void loadingScreen() {
        this.pbLoading = findViewById(R.id.pb_pokemon_details_loading);
        this.pbLoading.bringToFront();
        this.pbLoading.setVisibility(View.VISIBLE);
        this.clComponents = findViewById(R.id.cl_pkmndetails_components);
        this.clComponents.setVisibility(View.INVISIBLE);
    }

    /**
     * Sets the view components for when the data has finished loading
     */
    private void finishLoading() {
        this.initComponents(pkmn);
        this.setAllComponents(pkmn);
        this.initUserPreferences();

        this.pbLoading.setVisibility(View.GONE);
        this.clComponents.setVisibility(View.VISIBLE);
        this.playPkmnCry(pkmn.getDetails());
    }

    /**
     * Gets the current pokemon from the ArrayList of Pokemon; Also gets all pokemon in the party
     * @param list the list of all the User's UserPokemon
     * @param pkmnid the id of the UserPokemon to display
     */
    private void loadPokemonData(ArrayList<UserPokemon> list, String pkmnid) {
        this.partyList = new ArrayList<>();
        for(int i = 0; i < list.size(); i++) {
            if (list.get(i).getUserPokemonID().equalsIgnoreCase(pkmnid))
                pkmn = list.get(i);

            if (list.get(i).isInParty())
                partyList.add(list.get(i));
        }
    }

    /**
     * Retrieves information from the database.
     */
    private void initInfo () {
        this.pkmnWasDeleted = false;
        Intent intent = getIntent();
        String pkmnid = intent.getStringExtra(Keys.KEY_POKEMONID.name());
        sourceActivity = intent.getStringExtra(Keys.KEY_FROMWHERE.name());

        databaseHelper = new DatabaseHelper();
        databaseHelper.getUserDetails((userDetails, isSuccessful, message) -> {
            if(isSuccessful) {
                user = userDetails;
                databaseHelper.getPokemon((list, isSuccessful1, message1) -> {
                    loadPokemonData(list, pkmnid);
                    finishLoading();
                });
            }
        });
    }

    /**
     * Initializes the layout's components
     * @param pkmn the UserPokemon to display
     */
    private void initComponents(UserPokemon pkmn) {
        this.ivPkmnIcon = findViewById(R.id.iv_pkmndetails_icon);
        this.tvPkmnNickname = findViewById(R.id.tv_pkmndetails_nickname);
        this.tvPkmnSpecies = findViewById(R.id.tv_pkmndetails_species);
        this.tvPkmnLevel = findViewById(R.id.tv_pkmndetails_level);
        this.tvPkmnType = findViewById(R.id.tv_pkmndetails_type);
        this.tvPkmnNature = findViewById(R.id.tv_pkmndetails_nature);
        this.tvPkmnMetDate = findViewById(R.id.tv_pkmndetails_metdate);
        this.pbPkmnLevel = findViewById(R.id.pb_pkmndetails_level);
        this.btnRelease = findViewById(R.id.btn_pkmndetails_release);

        this.tvRareCandyCtr = findViewById(R.id.tv_pkmndetails_rarecandyctr);
        this.tvSuperCandyCtr = findViewById(R.id.tv_pkmndetails_supercandyctr);
        this.llrare = findViewById(R.id.ll_pkmndetails_rarecandy);
        this.llsuper = findViewById(R.id.ll_pkmndetails_supercandy);

        this.btnback = findViewById(R.id.ib_pkmndetails_back);
        this.btnedit = findViewById(R.id.ib_pkmndetails_edit);

        this.btnrare = findViewById(R.id.btn_pkmndetails_rarecandy);
        if (user.getRareCandy() <= 0 || pkmn.getLevel() >= 100)
            btnrare.setEnabled(false);

        this.btnsuper = findViewById(R.id.btn_pkmndetails_supercandy);
        if (user.getSuperCandy() <= 0
                || pkmn.getLevel() < pkmn.getDetails().getEvolveLvl()
                || pkmn.getDetails().getEvolvesTo().isEmpty())
            btnsuper.setEnabled(false);

        this.btnpc = findViewById(R.id.btn_pkmndetails_pc);
        this.setButtonListeners(pkmn);
    }

    /**
     * Sets the onClickListeners for all buttons
     */
    private void setButtonListeners(UserPokemon pkmn) {
        this.btnback.setOnClickListener(view -> onBackPressed());
        this.btnedit.setOnClickListener(view -> createEditNicknameDialog());
        this.btnrare.setOnClickListener(view -> feedPokemon());
        this.btnsuper.setOnClickListener(view -> evolvePokemon());
        this.btnRelease.setOnClickListener(v -> createReleasePokemonDialog (v, pkmn));

        // sets buttons depending if currently in party or pc
        if (sourceActivity.equals("PARTY")) {
            this.btnpc.setOnClickListener(view -> createMovePokemonToPCDialog());
            this.btnpc.setText("MOVE TO PC");

            // removes move to pc option if party has only one pokemon
            if (partyList.size() <= 1)
                btnpc.setVisibility(View.GONE);

            // removes release button
            this.btnRelease.setVisibility(View.GONE);
        }
        else {
            this.btnpc.setOnClickListener(view -> createMovePokemonToPartyDialog());
            this.btnpc.setText("MOVE TO PARTY");

            // removes move to party option if party already has 6 pokemon
            if (partyList.size() >= 6)
                btnpc.setVisibility(View.GONE);

            // removes all candy buttons
            btnrare.setVisibility(View.GONE);
            btnsuper.setVisibility(View.GONE);
            llrare.setVisibility(View.GONE);
            llsuper.setVisibility(View.GONE);


        }
    }

    /**
     * Initializes the user preferences for enabling pokemon cries.
     */
    private void initUserPreferences() {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        this.soundEnabled = sp.getBoolean(Keys.KEY_PKMNCRIES.name(), true);
    }

    /**
     * Sets values for all TextViews and ImageViews based on the UserPokemon
     * @param pkmn the UserPokemon to display
     */
    private void setAllComponents(UserPokemon pkmn) {
        // sets pokemon icon
        this.ivPkmnIcon.setImageResource(getImageId("pkmn_"+ pkmn.getDetails().getDexNum()));

        // sets pokemon details
        this.tvPkmnNickname.setText(pkmn.getNickname());
        this.tvPkmnSpecies.setText(pkmn.getDetails().getSpecies());
        this.tvPkmnNature.setText(pkmn.getNature());
        this.tvPkmnMetDate.setText(pkmn.getMetDate());

        // sets progress bar based on candies eaten
        this.pbPkmnLevel.setProgress(pkmn.getPercentToNextLevel());

        // sets pokemon level
        String level = "Level " + pkmn.getLevel();
        this.tvPkmnLevel.setText(level);

        // sets pokemon type
        String pkmntype = pkmn.getDetails().getType1();
        if (!pkmn.getDetails().getType2().isEmpty())
            pkmntype = pkmntype + "/" + pkmn.getDetails().getType2();
        this.tvPkmnType.setText(pkmntype);

        // sets user's candy count
        this.tvRareCandyCtr.setText(Integer.toString(user.getRareCandy()));
        this.tvSuperCandyCtr.setText(Integer.toString(user.getSuperCandy()));
    }

    /**
     * Feeds the pokemon 1 rare candy. Every 10 candies, the pokemon will gain 1 level.
     */
    private void feedPokemon() {
        // pokemon leveled up
        if (pkmn.feedCandy()) {

            //play level up sound
            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.levelup);
            mediaPlayer.start();

            // update level text
            String level = "Level " + pkmn.getLevel();
            this.tvPkmnLevel.setText(level);

            // enable super candy button if pokemon can evolve
            if (pkmn.getDetails().getEvolveLvl() <= pkmn.getLevel()
                    && pkmn.getDetails().getEvolveLvl() != -1)
                this.btnsuper.setEnabled(true);
        }

        // update rare candy amount and counter
        user.subtractRareCandy(1);
        this.tvRareCandyCtr.setText(Integer.toString(user.getRareCandy()));

        // update progress bar
        this.pbPkmnLevel.setProgress(pkmn.getPercentToNextLevel());

        // disable rare candy button
        if (user.getRareCandy() <= 0 || pkmn.getLevel() >= 100)
            btnrare.setEnabled(false);

        databaseHelper.updatePokemon((list, isSuccessful, message) -> {}, pkmn, user);
    }

    /**
     * Evolves the pokemon if all conditions are satisfied:
     * 1) Pokemon has an evolution,
     * 2) Pokemon has met the required level for evolution, and
     * 3) User has at least 1 Super Candy in the inventory
     */
    private void evolvePokemon() {
        if (pkmn.getDetails().getEvolveLvl() <= pkmn.getLevel()
                && pkmn.getDetails().getEvolveLvl() != -1) {
            pkmn.evolvePokemon();

            // update pokemon details
            this.tvPkmnNickname.setText(pkmn.getNickname());
            this.ivPkmnIcon.setImageResource(getImageId("pkmn_"+ pkmn.getDetails().getDexNum()));
            this.tvPkmnSpecies.setText(pkmn.getDetails().getSpecies());

            // play pokemon cry
            this.playPkmnCry(pkmn.getDetails());

            // update super candy amount and counter
            this.user.subtractSuperCandy(1);
            this.tvSuperCandyCtr.setText(Integer.toString(user.getSuperCandy()));

            // disable super candy button
            if (user.getSuperCandy() <= 0
                    || pkmn.getDetails().getEvolveLvl() > pkmn.getLevel()
                    || pkmn.getDetails().getEvolveLvl() == -1)
                this.btnsuper.setEnabled(false);

            this.databaseHelper.updatePokemon((list, isSuccessful, message) -> {}, pkmn, user);
        }
    }

    /**
     * Creates a confirm dialog for when the user wants to move the pokemon to the PC.
     */
    private void createMovePokemonToPCDialog () {
        confirmDialog = new CustomDialog(this);
        confirmDialog.setDialogType(CustomDialog.CONFIRM);
        confirmDialog.setConfirmComponents(
                getString(R.string.pkmndetails_movetopcdiag_title),
                getString(R.string.pkmndetails_movetopcdiag_text),
                R.drawable.warning,
                getString(R.string.pkmndetails_movetopcdiag_button)
        );

        Button btndialogconfirm = confirmDialog.findViewById(R.id.btn_dialog_confirm);
        btndialogconfirm.setOnClickListener(v -> {
            confirmDialog.dismiss();
                databaseHelper.movePokemon((list, isSuccessful, message) -> finish(),
                        pkmn.getUserPokemonID(), false);
        });
        confirmDialog.show();
    }

    /**
     * Creates a confirm dialog for when the user wants to move the pokemon to the party.
     */
    private void createMovePokemonToPartyDialog() {
        confirmDialog = new CustomDialog(this);
        confirmDialog.setDialogType(CustomDialog.CONFIRM);
        confirmDialog.setConfirmComponents(
                getString(R.string.pkmndetails_movetopartydiag_title),
                getString(R.string.pkmndetails_movetopartydiag_text),
                R.drawable.warning,
                getString(R.string.pkmndetails_movetopartydiag_button)
        );

        Button btndialogconfirm = confirmDialog.findViewById(R.id.btn_dialog_confirm);
        btndialogconfirm.setOnClickListener(v -> {
            confirmDialog.dismiss();
                databaseHelper.movePokemon((list, isSuccessful, message) -> finish(),
                        pkmn.getUserPokemonID(), true);
        });
        confirmDialog.show();
    }

    /**
     * Creates an input dialog for when the user wants to give a nickname to the pokemon.
     */
    private void createEditNicknameDialog() {
        editdialog = new CustomDialog(this);
        editdialog.setDialogType(CustomDialog.ONE_INPUT);

        this.editdialog.setOneInputComponents(
                getString(R.string.pokemondetails_rename_caption),
                getImageId("pkmn_"+ pkmn.getDetails().getDexNum())
        );

        EditText name = editdialog.findViewById(R.id.et_dialog_stringinput);

        Button btndialogok = editdialog.findViewById(R.id.btn_dialog_stringinput_ok);
        btndialogok.setOnClickListener(v -> {
            pkmn.setNickname(name.getText().toString());
                tvPkmnNickname.setText(pkmn.getNickname());
                editdialog.dismiss();
                databaseHelper.editNickname((list, isSuccessful, message) -> {},
                        pkmn.getUserPokemonID(),
                        pkmn.getNickname());
        });

        editdialog.show();
    }

    /**
     * Creates a confirm dialog for when the user wants to release the pokemon.
     */
    private void createReleasePokemonDialog (View view, UserPokemon pkmn) {
        releasePkmnDialog = new CustomDialog(this);
        releasePkmnDialog.setDialogType(CustomDialog.CONFIRM);

        releasePkmnDialog.setConfirmComponents(
                getString(R.string.pkmndetails_releasepkmndiag_title),
                getString(R.string.pkmndetails_releasepkmndiag_text),
                getImageId("pkmn_"+ pkmn.getDetails().getDexNum()),
                getString(R.string.pkmndetails_releasepkmndiag_button)
        );

        Button btndialogconfirm = releasePkmnDialog.findViewById(R.id.btn_dialog_confirm);
        btndialogconfirm.setOnClickListener(v -> {
            releasePkmnDialog.dismiss();
                databaseHelper.deletePokemon((list, isSuccessful, message) -> {
                    if(isSuccessful) {
                        finish();
                        pkmnWasDeleted = true;
                    }
                }, pkmn);
        });
        releasePkmnDialog.show();
    }

    /**
     * Helper function to get the image ID given the image name.
     * @param imageName the name of the image
     * @return the image id of the image
     */
    private int getImageId(String imageName) {
        return getApplicationContext().getResources().getIdentifier(
                "drawable/" + imageName, null,
                getApplicationContext().getPackageName());
    }

    /**
     * Plays a Pokemon cry if enabled in Settings.
     * @param pkmn the species of the pokemon.
     */
    private void playPkmnCry(Pokemon pkmn) {
        if (soundEnabled)
            pkmn.playPokemonCry();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (!pkmnWasDeleted)
            databaseHelper.updatePokemon((list, isSuccessful, message) -> {}, pkmn, user);
    }
}