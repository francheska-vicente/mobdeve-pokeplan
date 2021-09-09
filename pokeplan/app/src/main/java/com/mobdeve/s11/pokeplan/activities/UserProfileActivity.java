package com.mobdeve.s11.pokeplan.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.mobdeve.s11.pokeplan.R;
import com.mobdeve.s11.pokeplan.data.DatabaseHelper;
import com.mobdeve.s11.pokeplan.models.UserDetails;
import com.mobdeve.s11.pokeplan.utils.Keys;

public class UserProfileActivity extends AppCompatActivity {
    private ConstraintLayout clComponents;
    private ProgressBar pbLoading;

    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    private ImageButton btnback;
    private Button btnlogout;

    private ImageView ivStarterIcon;
    private TextView tvUsername;
    private TextView tvEmail;
    private TextView tvName;
    private TextView tvBirthday;

    private TextView tvEggHatchCtr;
    private TextView tvTaskCompleteCtr;
    private UserDetails user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        initInfo();
    }

    /**
     * Sets the view components for when the data has not loaded yet
     */
    private void loadingScreen() {
        this.pbLoading = findViewById(R.id.pb_userprofile_loading);
        this.pbLoading.setVisibility(View.VISIBLE);
        this.clComponents = findViewById(R.id.cl_userprofile_components);
        this.clComponents.setVisibility(View.INVISIBLE);
    }

    /**
     * Sets the view components for when the data has finished loading
     */
    private void finishLoading() {
        pbLoading.setVisibility(View.GONE);
        clComponents.setVisibility(View.VISIBLE);
    }

    /**
     * Retrieves user information from the database
     */
    private void initInfo () {
        loadingScreen();
        DatabaseHelper databaseHelper = new DatabaseHelper(true);
        databaseHelper.getUserDetails((userDetails, isSuccessful, message) -> {
            if (isSuccessful) {
                user = userDetails;
                finishLoading();
                initComponents();
            }
        });
    }

    /**
     * Initializes the layout's components
     */
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
        this.tvBirthday = findViewById(R.id.tv_userprofile_bday);

        this.tvEggHatchCtr = findViewById(R.id.tv_userprofile_egghatchctr);
        this.tvTaskCompleteCtr = findViewById(R.id.tv_userprofile_taskcompletedctr);
        setAllComponents();
    }

    /**
     * Sets the onClickListeners for all buttons
     */
    private void setButtonListeners() {
        this.btnback.setOnClickListener(view -> onBackPressed());
        this.btnlogout.setOnClickListener(view -> logOutUser());
    }

    /**
     * Sets user data in the designated view components
     */
    private void setAllComponents() {
        // set user profile pic
        this.ivStarterIcon.setImageResource(getImageId(getApplicationContext(),
                "pkmn_"+ user.getStarterDexNum()));

        // set user details
        String username = "@" + user.getUserName();
        this.tvUsername.setText(username);
        this.tvEmail.setText(user.getEmail());
        this.tvName.setText(user.getFullName());
        this.tvBirthday.setText(user.getBirthday().printSpecificDate());

        // set user stats
        String egg = user.getHatchedPkmnCount() + " Pokemon Hatched";
        this.tvEggHatchCtr.setText(egg);
        String task = user.getCompletedTaskCount() + " Tasks Completed";
        this.tvTaskCompleteCtr.setText(task);
    }

    /**
     * Logs out the user from the application.
     */
    private void logOutUser() {
        // removes user credentials from shared preferences
        spEditor.remove(Keys.KEY_EMAIL.name());
        spEditor.remove(Keys.KEY_PASSWORD.name());
        spEditor.apply();

        // brings user to initial activity
        Intent intent = new Intent(UserProfileActivity.this, InitActivity.class);
        startActivity(intent);
    }

    /**
     * Helper function to get the image ID given the image name.
     * @param imageName the name of the image
     * @return the image id of the image
     */
    private int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }
}