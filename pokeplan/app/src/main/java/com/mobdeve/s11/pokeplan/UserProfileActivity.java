package com.mobdeve.s11.pokeplan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UserProfileActivity extends AppCompatActivity {
    private ImageButton btnback;
    private ImageButton btnedit;

    private ImageView ivStarterIcon;
    private TextView tvUsername;
    private TextView tvEmail;
    private TextView tvName;

    private TextView tvEggHatchCtr;
    private TextView tvTaskCompleteCtr;

    private Dialog editdialog;
    private Dialog confirmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        initComponents();
        setAllComponents();
    }

    private void initComponents() {
        this.btnback = findViewById(R.id.ib_userprofile_back);
        this.btnback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // this.btnedit = findViewById(R.id.ib_pkmndetails_edit);

        this.ivStarterIcon = findViewById(R.id.iv_userprofile_starter);
        this.tvUsername = findViewById(R.id.tv_userprofile_username);
        this.tvEmail = findViewById(R.id.tv_userprofile_email);
        this.tvName = findViewById(R.id.tv_userprofile_name);

        this.tvEggHatchCtr = findViewById(R.id.tv_userprofile_egghatchctr);
        this.tvTaskCompleteCtr = findViewById(R.id.tv_userprofile_taskcompletedctr);

    }

    private void setAllComponents() {
        UserDetails user = UserSingleton.getUser().getUserDetails();

        this.ivStarterIcon.setImageResource(getImageId(getApplicationContext(),
                "pkmn_"+ user.getStarterDexNum()));
        this.tvUsername.setText(user.getUserName());
        this.tvEmail.setText(user.getEmail());
        this.tvName.setText(user.getUserName());

        String egg = user.getHatchedPkmnCount() + " Pokemon Hatched";
        this.tvEggHatchCtr.setText(egg);

        String task = user.getCompletedTaskCount() + " Tasks Completed";
        this.tvTaskCompleteCtr.setText(task);

//        this.btnedit.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//
//            }
//        });
    }


    private int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }
}