package com.mobdeve.s11.pokeplan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UserProfileActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    private ImageButton btnback;
    private Button btnlogout;

    private ImageView ivStarterIcon;
    private TextView tvUsername;
    private TextView tvEmail;
    private TextView tvName;

    private TextView tvEggHatchCtr;
    private TextView tvTaskCompleteCtr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        initComponents();
        setAllComponents();
    }

    private void initComponents() {
        this.sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        this.spEditor = this.sp.edit();

        this.btnback = findViewById(R.id.ib_userprofile_back);
        this.btnlogout = findViewById(R.id.btn_userprofile_logout);
        setButtonListeners();

        this.ivStarterIcon = findViewById(R.id.iv_userprofile_starter);
        this.tvUsername = findViewById(R.id.tv_userprofile_username);
        this.tvEmail = findViewById(R.id.tv_userprofile_email);
        this.tvName = findViewById(R.id.tv_userprofile_name);
        this.tvEggHatchCtr = findViewById(R.id.tv_userprofile_egghatchctr);
        this.tvTaskCompleteCtr = findViewById(R.id.tv_userprofile_taskcompletedctr);
        setAllComponents();
    }

    private void setAllComponents() {
        UserDetails user = UserSingleton.getUser().getUserDetails();

        // set user profile pic
        this.ivStarterIcon.setImageResource(getImageId(getApplicationContext(),
                "pkmn_"+ user.getStarterDexNum()));

        // set user details
        String username = "@" + user.getUserName();
        this.tvUsername.setText(username);
        this.tvEmail.setText(user.getEmail());
        this.tvName.setText(user.getFullName());

        // set user stats
        String egg = user.getHatchedPkmnCount() + " Pokemon Hatched";
        this.tvEggHatchCtr.setText(egg);
        String task = user.getCompletedTaskCount() + " Tasks Completed";
        this.tvTaskCompleteCtr.setText(task);
    }

    private void setButtonListeners() {
        this.btnback.setOnClickListener(view -> onBackPressed());
        this.btnlogout.setOnClickListener(view -> logOutUser());
    }

    private void logOutUser() {
        spEditor.remove(Keys.KEY_EMAIL.name());
        spEditor.remove(Keys.KEY_PASSWORD.name());
        spEditor.apply();
        UserSingleton.removeUser();
        //UserSingleton.getUser().logoutUser();
        Intent intent = new Intent(UserProfileActivity.this, InitActivity.class);
        startActivity(intent);
    }


    private int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }

    @Override
    public void onStart () {
        super.onRestart();

        final Handler handler = new Handler();
        handler.postDelayed(() -> setAllComponents(), 500);
    }
}