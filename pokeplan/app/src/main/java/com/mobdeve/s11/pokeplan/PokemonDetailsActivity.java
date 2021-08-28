package com.mobdeve.s11.pokeplan;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class PokemonDetailsActivity extends AppCompatActivity {
    private String sourceActivity;

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

    private Dialog editdialog;
    private Dialog releasePkmnDialog;
    private Dialog confirmDialog;

    private UserPokemon pkmn;
    private boolean checkerDeleted;

    FirebaseDatabase mDatabase;
    private DatabaseReference mUser;
    private DatabaseReference mPokemon;
    private String userID;
    private UserDetails user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_details);
        checkerDeleted = false;
        Intent intent = getIntent();
        String pkmnid = intent.getStringExtra(Keys.KEY_POKEMONID.name());
        sourceActivity = intent.getStringExtra(Keys.KEY_FROMWHERE.name());

        initDBInfo (pkmnid);

        initComponents();
        initButtons(pkmn);
        setAllComponents(pkmn);
        pkmn.getPokemonDetails().playPokemonCry();
    }

    private void initDBInfo (String pkmnid) {
        mDatabase = FirebaseDatabase.getInstance("https://pokeplan-8930c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        this.userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.mUser = mDatabase.getReference("Users").child(this.userID);
        this.mPokemon = mDatabase.getReference("UserPokemon").child(this.userID);

        this.getPokemonDetails(pkmnid, new FirebaseCallbackPokemon() {
            @Override
            public void onCallbackPokemon(ArrayList<UserPokemon> list) {
                pkmn = list.get(0);
            }
        });

        this.getUserDetails(new FirebaseCallbackUser() {
            @Override
            public void onCallbackUser(UserDetails userDetails) {
                user = userDetails;
            }
        });
    }

    private void initComponents() {
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
    }

    private void initButtons(UserPokemon pkmn) {
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

        setButtonListeners();

        this.btnpc = findViewById(R.id.btn_pkmndetails_pc);
        if (sourceActivity.equals("PARTY")) {
            this.btnpc.setOnClickListener(view -> movePokemonToPC());
            this.btnpc.setText("MOVE TO PC");
            if (UserSingleton.getUser().getUserPokemonParty().size() <= 1)
                btnpc.setVisibility(View.GONE);

            this.btnRelease.setVisibility(View.GONE);
        }
        else {
            this.btnpc.setOnClickListener(view -> movePokemonToParty());
            this.btnpc.setText("MOVE TO PARTY");
            if (UserSingleton.getUser().getUserPokemonParty().size() >= 6)
                btnpc.setVisibility(View.GONE);

            btnrare.setVisibility(View.GONE);
            btnsuper.setVisibility(View.GONE);
            llrare.setVisibility(View.GONE);
            llsuper.setVisibility(View.GONE);

            this.btnRelease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    releaseDialogInit (v, pkmn);
                }
            });
        }
    }

    private void releaseDialogInit (View view, UserPokemon pkmn) {
        releasePkmnDialog = new Dialog(this);
        releasePkmnDialog.setContentView(R.layout.dialog_confirm);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.40);

        releasePkmnDialog.getWindow().setLayout(width, height);
        releasePkmnDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tvdialogtitle = (TextView) releasePkmnDialog.findViewById(R.id.tv_dialog_confirm_title);
        tvdialogtitle.setText(R.string.pkmndetails_releasepkmndiag_title);
        TextView tvdialogtext = (TextView) releasePkmnDialog.findViewById(R.id.tv_dialog_confirm_text);
        tvdialogtext.setText(R.string.pkmndetails_releasepkmndiag_text);
        ImageView ivdialogicon = (ImageView) releasePkmnDialog.findViewById(R.id.iv_dialog_confirm_icon);
        ivdialogicon.setImageResource(getImageId(getApplicationContext(),
                "pkmn_"+ pkmn.getPokemonDetails().getDexNum()));

        Button btndialogcancel = (Button) releasePkmnDialog.findViewById(R.id.btn_dialog_confirm_cancel);
        btndialogcancel.setOnClickListener(v -> releasePkmnDialog.dismiss());

        Button btndialogconfirm = (Button) releasePkmnDialog.findViewById(R.id.btn_dialog_confirm);
        btndialogconfirm.setText(R.string.pkmndetails_releasepkmndiag_button);
        btndialogconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releasePkmnDialog.dismiss();
                UserSingleton.getUser().deletePokemon(pkmn);
                finish();
                checkerDeleted = true;
            }
        });
        releasePkmnDialog.show();
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

        this.tvRareCandyCtr.setText(Integer.toString(UserSingleton.getUser().getUserDetails().getRareCandy()));
        this.tvSuperCandyCtr.setText(Integer.toString(UserSingleton.getUser().getUserDetails().getSuperCandy()));
    }

    private void setButtonListeners() {
        this.btnback.setOnClickListener(view -> onBackPressed());
        this.btnedit.setOnClickListener(view -> editNickname());
        this.btnrare.setOnClickListener(view -> feedPokemon());
        this.btnsuper.setOnClickListener(view -> evolvePokemon());
    }

    private void movePokemonToPC () {
        confirmDialog = new Dialog(this);
        confirmDialog.setContentView(R.layout.dialog_confirm);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.40);

        confirmDialog.getWindow().setLayout(width, height);
        confirmDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tvdialogtitle = (TextView) confirmDialog.findViewById(R.id.tv_dialog_confirm_title);
        tvdialogtitle.setText(R.string.pkmndetails_movetopcdiag_title);
        TextView tvdialogtext = (TextView) confirmDialog.findViewById(R.id.tv_dialog_confirm_text);
        tvdialogtext.setText(R.string.pkmndetails_movetopcdiag_text);
        ImageView ivdialogicon = (ImageView) confirmDialog.findViewById(R.id.iv_dialog_confirm_icon);
        ivdialogicon.setImageResource(R.drawable.warning);

        Button btndialogcancel = (Button) confirmDialog.findViewById(R.id.btn_dialog_confirm_cancel);
        btndialogcancel.setOnClickListener(v -> confirmDialog.dismiss());

        Button btndialogconfirm = (Button) confirmDialog.findViewById(R.id.btn_dialog_confirm);
        btndialogconfirm.setText(R.string.pkmndetails_movetopcdiag_button);
        btndialogconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
                UserSingleton.getUser().movePokemon(pkmn.getUserPokemonID(), false);
                finish();
            }
        });
        confirmDialog.show();

    }

    private void movePokemonToParty () {
        confirmDialog = new Dialog(this);
        confirmDialog.setContentView(R.layout.dialog_confirm);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);

        confirmDialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
        confirmDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tvdialogtitle = (TextView) confirmDialog.findViewById(R.id.tv_dialog_confirm_title);
        tvdialogtitle.setText(R.string.pkmndetails_movetopartydiag_title);
        TextView tvdialogtext = (TextView) confirmDialog.findViewById(R.id.tv_dialog_confirm_text);
        tvdialogtext.setText(R.string.pkmndetails_movetopartydiag_text);
        ImageView ivdialogicon = (ImageView) confirmDialog.findViewById(R.id.iv_dialog_confirm_icon);
        ivdialogicon.setImageResource(R.drawable.warning);

        Button btndialogcancel = (Button) confirmDialog.findViewById(R.id.btn_dialog_confirm_cancel);
        btndialogcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
            }
        });

        Button btndialogconfirm = (Button) confirmDialog.findViewById(R.id.btn_dialog_confirm);
        btndialogconfirm.setText(R.string.pkmndetails_movetopartydiag_button);
        btndialogconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
                UserSingleton.getUser().movePokemon(pkmn.getUserPokemonID(), true);
                finish();
            }
        });
        confirmDialog.show();
    }

    private void editNickname() {
        editdialog = new Dialog(this);
        editdialog.setContentView(R.layout.dialog_oneinput);
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.40);

        editdialog.getWindow().setLayout(width, height);
        editdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tvdialogtitle = editdialog.findViewById(R.id.iv_dialog_stringinput_title);
        tvdialogtitle.setText(R.string.pokemondetails_rename_caption);
        EditText etdialoginput = editdialog.findViewById(R.id.et_dialog_stringinput);
        ImageView ivdialogicon = editdialog.findViewById(R.id.iv_dialog_stringinput_icon);
        ivdialogicon.setImageResource(getImageId(getApplicationContext(),
                "pkmn_"+ pkmn.getPokemonDetails().getDexNum()));

        Button btndialogok = editdialog.findViewById(R.id.btn_dialog_stringinput_ok);
        btndialogok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pkmn.setNickname(etdialoginput.getText().toString());
                tvPkmnNickname.setText(pkmn.getNickname());
                editdialog.dismiss();
                UserSingleton.getUser().editNickname(pkmn.getUserPokemonID(), pkmn.getNickname());
            }
        });

        Button btndialogcancel = editdialog.findViewById(R.id.btn_dialog_stringinput_confirm);
        btndialogcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editdialog.dismiss();
            }
        });

        editdialog.show();
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

        UserSingleton.getUser().getUserDetails().subtractRareCandy(1);
        this.tvRareCandyCtr.setText(Integer.toString(UserSingleton.getUser().getUserDetails().getRareCandy()));
        this.pbPkmnLevel.setProgress(pkmn.getPercentToNextLevel());

        if (!(UserSingleton.getUser().getUserDetails().getRareCandy() > 0 && pkmn.getLevel() < 100))
            btnrare.setEnabled(false);

    }

    private void evolvePokemon() {
        if (pkmn.getPokemonDetails().getEvolveLvl() <= pkmn.getLevel()
                && pkmn.getPokemonDetails().getEvolveLvl() != -1) {
            pkmn.evolvePokemon();
            pkmn.getPokemonDetails().playPokemonCry();

            UserSingleton.getUser().getUserDetails().subtractSuperCandy(1);
            this.tvSuperCandyCtr.setText(Integer.toString(UserSingleton.getUser().getUserDetails().getSuperCandy()));

            this.ivPkmnIcon.setImageResource(getImageId(getApplicationContext(),
                    "pkmn_"+ pkmn.getPokemonDetails().getDexNum()));
            this.tvPkmnSpecies.setText(pkmn.getPokemonDetails().getSpecies());
        }

        if (pkmn.getPokemonDetails().getEvolveLvl() > pkmn.getLevel()
                || pkmn.getPokemonDetails().getEvolveLvl() == -1)
            this.btnsuper.setEnabled(false);

    }

    private void getPokemonDetails (String pkmnid, FirebaseCallbackPokemon firebaseCallbackPokemon) {
        mPokemon.child(pkmnid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                UserPokemon userPokemon = new UserPokemon();
                userPokemon.setDetails(snapshot.getValue(Pokemon.class));
                userPokemon.setDMetDate(snapshot.getValue(Date.class));
                userPokemon.setFedCandy(snapshot.getValue(Integer.class));
                userPokemon.setInParty(snapshot.getValue(Boolean.class));
                userPokemon.setLevel(snapshot.getValue(Integer.class));
                userPokemon.setNature(snapshot.getValue(String.class));
                userPokemon.setNickname(snapshot.getValue(String.class));
                userPokemon.setUserPokemonID(snapshot.getValue(String.class));

                ArrayList<UserPokemon> temp = new ArrayList<>(1);
                firebaseCallbackPokemon.onCallbackPokemon(temp);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void getUserDetails (FirebaseCallbackUser firebaseCallbackUser) {
        mUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                UserDetails user = snapshot.getValue(UserDetails.class);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }

    protected void onStop() {
        super.onStop();

        if (!checkerDeleted)
            UserSingleton.getUser().updatePokemon (pkmn);
    }
}