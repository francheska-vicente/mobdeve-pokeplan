package com.mobdeve.s11.pokeplan;

import android.content.Context;
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
    private boolean checkerDeleted;

    private ArrayList<UserPokemon> partyList;
    private String userID;
    private DatabaseHelper databaseHelper;
    private ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_details);
        checkerDeleted = false;
        Intent intent = getIntent();
        String pkmnid = intent.getStringExtra(Keys.KEY_POKEMONID.name());
        sourceActivity = intent.getStringExtra(Keys.KEY_FROMWHERE.name());
        databaseHelper = new DatabaseHelper();
        this.pbLoading = findViewById(R.id.pb_pokemon_details_loading);
        this.pbLoading.bringToFront();
        this.pbLoading.setVisibility(View.VISIBLE);
        partyList = new ArrayList<>();
        this.initInfo (pkmnid);
    }


    private void initInfo (String pkmnid) {
        databaseHelper.getUserDetails(new FirebaseCallbackUser() {
            @Override
            public void onCallbackUser(UserDetails userDetails, Boolean isSuccessful, String message) {
                if(isSuccessful) {
                    user = userDetails;
                    databaseHelper.getPokemon(new FirebaseCallbackPokemon() {
                        @Override
                        public void onCallbackPokemon(ArrayList<UserPokemon> list, Boolean isSuccessful, String message) {
                            for(int i = 0; i < list.size(); i++) {
                                if (list.get(i).getUserPokemonID().equalsIgnoreCase(pkmnid)) {
                                    pkmn = list.get(i);
                                }
                                if (list.get(i).isInParty()) {
                                    partyList.add(list.get(i));
                                }
                            }

                            initComponents(pkmn);
                            setAllComponents(pkmn);
                            pbLoading.setVisibility(View.GONE);
                            pkmn.getPokemonDetails().playPokemonCry();
                        }
                    });
                }
            }
        });
    }

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
        if (user.getRareCandy() <= 0 ||
                pkmn.getLevel() >= 100)
            btnrare.setEnabled(false);

        this.btnsuper = findViewById(R.id.btn_pkmndetails_supercandy);
        if (user.getSuperCandy() <= 0
                || pkmn.getLevel() < pkmn.getPokemonDetails().getEvolveLvl()
                || pkmn.getPokemonDetails().getEvolvesTo().isEmpty())
            btnsuper.setEnabled(false);

        this.btnpc = findViewById(R.id.btn_pkmndetails_pc);
        this.setButtonListeners(pkmn);
    }

    private void setButtonListeners(UserPokemon pkmn) {
        this.btnback.setOnClickListener(view -> onBackPressed());
        this.btnedit.setOnClickListener(view -> createEditNicknameDialog());
        this.btnrare.setOnClickListener(view -> feedPokemon());
        this.btnsuper.setOnClickListener(view -> evolvePokemon());

        if (sourceActivity.equals("PARTY")) {
            this.btnpc.setOnClickListener(view -> createMovePokemonToPCDialog());
            this.btnpc.setText("MOVE TO PC");
            if (partyList.size() <= 1)
                btnpc.setVisibility(View.GONE);

            this.btnRelease.setVisibility(View.GONE);
        }
        else {
            this.btnpc.setOnClickListener(view -> createMovePokemonToPartyDialog());
            this.btnpc.setText("MOVE TO PARTY");
            if (partyList.size() >= 6)
                btnpc.setVisibility(View.GONE);

            btnrare.setVisibility(View.GONE);
            btnsuper.setVisibility(View.GONE);
            llrare.setVisibility(View.GONE);
            llsuper.setVisibility(View.GONE);

            this.btnRelease.setOnClickListener(v -> createReleasePokemonDialog (v, pkmn));
        }
    }

    /**
     * Initializes the user preferences for enabling certain functions of the focus timer.
     */
    private void initUserPreferences() {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        this.soundEnabled = sp.getBoolean(Keys.KEY_PKMNCRIES.name(), true);
    }



    private void setAllComponents(UserPokemon pkmn) {
        this.ivPkmnIcon.setImageResource(getImageId(getApplicationContext(),
                "pkmn_"+ pkmn.getPokemonDetails().getDexNum()));
        this.tvPkmnNickname.setText(pkmn.getNickname());
        this.tvPkmnSpecies.setText(pkmn.getPokemonDetails().getSpecies());
        this.tvPkmnNature.setText(pkmn.getNature());
        this.tvPkmnMetDate.setText(pkmn.getMetDate());
        this.pbPkmnLevel.setProgress(pkmn.getPercentToNextLevel());

        String level = "Level " + pkmn.getLevel();
        this.tvPkmnLevel.setText(level);

        String pkmntype = pkmn.getPokemonDetails().getType1();
        if (!pkmn.getPokemonDetails().getType2().isEmpty())
            pkmntype = pkmntype + "/" + pkmn.getPokemonDetails().getType2();
        this.tvPkmnType.setText(pkmntype);

        this.tvRareCandyCtr.setText(Integer.toString(user.getRareCandy()));
        this.tvSuperCandyCtr.setText(Integer.toString(user.getSuperCandy()));
    }

    private void feedPokemon() {
        if (pkmn.feedCandy()) {
            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.levelup);
            mediaPlayer.start();
            String level = "Level " + pkmn.getLevel();
            this.tvPkmnLevel.setText(level);

            if (pkmn.getPokemonDetails().getEvolveLvl() <= pkmn.getLevel()
                    && pkmn.getPokemonDetails().getEvolveLvl() != -1)
                this.btnsuper.setEnabled(true);
        }

        user.subtractRareCandy(1);
        this.tvRareCandyCtr.setText(Integer.toString(user.getRareCandy()));
        this.pbPkmnLevel.setProgress(pkmn.getPercentToNextLevel());

        if (!(user.getRareCandy() > 0 && pkmn.getLevel() < 100))
            btnrare.setEnabled(false);

        databaseHelper.updatePokemon(new FirebaseCallbackPokemon() {
            @Override
            public void onCallbackPokemon(ArrayList<UserPokemon> list, Boolean isSuccessful, String message) {

            }
        }, pkmn, user);
    }

    private void evolvePokemon() {
        if (pkmn.getPokemonDetails().getEvolveLvl() <= pkmn.getLevel()
                && pkmn.getPokemonDetails().getEvolveLvl() != -1) {
            pkmn.evolvePokemon();
            pkmn.getPokemonDetails().playPokemonCry();

            user.subtractSuperCandy(1);
            this.tvSuperCandyCtr.setText(Integer.toString(user.getSuperCandy()));

            this.ivPkmnIcon.setImageResource(getImageId(getApplicationContext(),
                    "pkmn_"+ pkmn.getPokemonDetails().getDexNum()));
            this.tvPkmnSpecies.setText(pkmn.getPokemonDetails().getSpecies());
        }

        if (pkmn.getPokemonDetails().getEvolveLvl() > pkmn.getLevel()
                || pkmn.getPokemonDetails().getEvolveLvl() == -1)
            this.btnsuper.setEnabled(false);

        databaseHelper.updatePokemon(new FirebaseCallbackPokemon() {
            @Override
            public void onCallbackPokemon(ArrayList<UserPokemon> list, Boolean isSuccessful, String message) {

            }
        }, pkmn, user);
    }

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
                databaseHelper.movePokemon(new FirebaseCallbackPokemon() {
                    @Override
                    public void onCallbackPokemon(ArrayList<UserPokemon> list, Boolean isSuccessful, String message) {
                        finish();
                    }
                }, pkmn.getUserPokemonID(), false);
        });
        confirmDialog.show();
    }

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
                databaseHelper.movePokemon(new FirebaseCallbackPokemon() {
                    @Override
                    public void onCallbackPokemon(ArrayList<UserPokemon> list, Boolean isSuccessful, String message) {
                        finish();
                    }
                }, pkmn.getUserPokemonID(), true);
        });
        confirmDialog.show();
    }

    private void createEditNicknameDialog() {
        editdialog = new CustomDialog(this);
        editdialog.setDialogType(CustomDialog.ONE_INPUT);

        this.editdialog.setOneInputComponents(
                getString(R.string.pokemondetails_rename_caption),
                getImageId(getApplicationContext(),
                        "pkmn_"+ pkmn.getPokemonDetails().getDexNum())
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

    private void createReleasePokemonDialog (View view, UserPokemon pkmn) {
        releasePkmnDialog = new CustomDialog(this);
        releasePkmnDialog.setDialogType(CustomDialog.CONFIRM);

        releasePkmnDialog.setConfirmComponents(
                getString(R.string.pkmndetails_releasepkmndiag_title),
                getString(R.string.pkmndetails_releasepkmndiag_text),
                getImageId(getApplicationContext(),
                        "pkmn_"+ pkmn.getPokemonDetails().getDexNum()),
                getString(R.string.pkmndetails_releasepkmndiag_button)
        );

        Button btndialogconfirm = releasePkmnDialog.findViewById(R.id.btn_dialog_confirm);
        btndialogconfirm.setOnClickListener(v -> {
            releasePkmnDialog.dismiss();
                databaseHelper.deletePokemon(new FirebaseCallbackPokemon() {
                    @Override
                    public void onCallbackPokemon(ArrayList<UserPokemon> list, Boolean isSuccessful, String message) {
                        if(isSuccessful) {
                            finish();
                            checkerDeleted = true;
                        }
                    }
                }, pkmn);
        });
        releasePkmnDialog.show();
    }

    private int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }

    /**
     * Plays a Pokemon cry.
     * @param pkmn the species of the pokemon.
     */
    private void playPkmnCry(Pokemon pkmn) {
        if (soundEnabled)
            pkmn.playPokemonCry();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (!checkerDeleted)
            databaseHelper.updatePokemon(new FirebaseCallbackPokemon() {
                @Override
                public void onCallbackPokemon(ArrayList<UserPokemon> list, Boolean isSuccessful, String message) {

                }
            }, pkmn, user);
    }
}