package com.mobdeve.s11.pokeplan.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.mobdeve.s11.pokeplan.R;
import com.mobdeve.s11.pokeplan.activities.FaqsActivity;
import com.mobdeve.s11.pokeplan.activities.InitActivity;
import com.mobdeve.s11.pokeplan.activities.UserProfileActivity;
import com.mobdeve.s11.pokeplan.data.DatabaseHelper;
import com.mobdeve.s11.pokeplan.models.CustomDate;
import com.mobdeve.s11.pokeplan.models.UserDetails;
import com.mobdeve.s11.pokeplan.utils.Keys;
import com.mobdeve.s11.pokeplan.views.CustomDatePicker;
import com.mobdeve.s11.pokeplan.views.CustomDialog;

import java.util.HashMap;

public class SettingsFragment extends Fragment {
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    private Switch swDeepFocus;
    private Switch swDimScreen;
    private Switch swKeepScreenOn;
    private Switch swNotifs;
    private Switch swPkmnCries;

    private Button btnEditAcc;
    private Button btnFreqQues;
    private Button btnAbout;
    private Button btnDeleteAcc;

    private CustomDialog aboutUsDialog;
    private CustomDialog deleteAccDialog;
    private CustomDialog editAccDialog;

    private DatabaseHelper databaseHelper;
    private UserDetails user;

    public SettingsFragment() {
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        this.databaseHelper = new DatabaseHelper(true);
        initInfo(view);

        this.sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        this.spEditor = this.sp.edit();

        return view;
    }

    /**
     * Retrieves user information from the database
     * @param view the view of the fragment
     */
    private void initInfo(View view) {
        databaseHelper.getUserDetails((userDetails, isSuccessful, message) -> {
            user = userDetails;
            initComponents(view);
        });
    }

    /**
     * Initializes all layout components
     * @param view the view of the fragment
     */
    private void initComponents (View view) {
        this.btnAbout = view.findViewById(R.id.btn_settings_about);
        this.btnEditAcc = view.findViewById(R.id.btn_settings_editacc);
        this.btnFreqQues = view.findViewById(R.id.btn_settings_faq);
        this.btnDeleteAcc = view.findViewById(R.id.btn_settings_delete_acc);
        setButtonListeners();

        this.swDeepFocus = view.findViewById(R.id.sw_deepfocus);
        this.swDimScreen = view.findViewById(R.id.sw_dimscreen);
        this.swKeepScreenOn = view.findViewById(R.id.sw_screenon);
        this.swNotifs = view.findViewById(R.id.sw_notifs);
        this.swPkmnCries = view.findViewById(R.id.sw_pkmncries);
        setSwitchFont(view);
        setSwitchValues();
    }

    /**
     * Sets OnClickListeners for all buttons
     */
    private void setButtonListeners () {
        this.btnAbout.setOnClickListener(view -> createAboutUsDialog());
        this.btnFreqQues.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), FaqsActivity.class);
            v.getContext().startActivity(i);
        });

        this.btnEditAcc.setOnClickListener(view -> createEditAccountDialog());
        this.btnDeleteAcc.setOnClickListener(view -> createDeleteAccountDialog());
    }

    /**
     * Helper function to change switch fonts programatically
     * @param view the view of the fragment
     */
    private void setSwitchFont(View view) {
        swDeepFocus.setTypeface(ResourcesCompat.getFont(view.getContext(), R.font.raleway_medium));
        swDimScreen.setTypeface(ResourcesCompat.getFont(view.getContext(), R.font.raleway_medium));
        swKeepScreenOn.setTypeface(ResourcesCompat.getFont(view.getContext(), R.font.raleway_medium));
        swNotifs.setTypeface(ResourcesCompat.getFont(view.getContext(), R.font.raleway_medium));
        swPkmnCries.setTypeface(ResourcesCompat.getFont(view.getContext(), R.font.raleway_medium));
    }

    /**
     * Sets switch values based on saved user preferences, if any
     */
    private void setSwitchValues () {
        boolean deepfocus = this.sp.getBoolean(Keys.KEY_DEEPFOCUS.name(), false);
        boolean dimScreen = this.sp.getBoolean(Keys.KEY_DIMSCREEN.name(), true);
        boolean keepscreenon = this.sp.getBoolean(Keys.KEY_KEEPSCREENON.name(), true);
        boolean notifs = this.sp.getBoolean(Keys.KEY_NOTIFS.name(), true);
        boolean pkmncries = this.sp.getBoolean(Keys.KEY_PKMNCRIES.name(), true);

        swDeepFocus.setChecked(deepfocus);
        swDimScreen.setChecked(dimScreen);
        swKeepScreenOn.setChecked(keepscreenon);
        swNotifs.setChecked(notifs);
        swPkmnCries.setChecked(pkmncries);
    }

    /**
     * Creates the input dialog for when the user wants to edit their account
     */
    private void createEditAccountDialog() {
        editAccDialog = new CustomDialog(getView().getContext());
        editAccDialog.setDialogType(CustomDialog.FOUR_INPUT);
        editAccDialog.setFourInputComponents(
                "Edit your profile!",
                "Full Name",
                "Username",
                "Birthday",
                "Enter your password:",
                "Edit Profile"
        );

        EditText etUsername = editAccDialog.findViewById(R.id.et_dialog_fourinput_two);
        EditText etBirthday = editAccDialog.findViewById(R.id.et_dialog_fourinput_three);
        EditText etPassword = editAccDialog.findViewById(R.id.et_dialog_fourinput_four);
        EditText etFullName = editAccDialog.findViewById(R.id.et_dialog_fourinput_one);

        new CustomDatePicker().createDatePicker(getView().getContext(), etBirthday, "");

        Button btnDialogConfirm = editAccDialog.findViewById(R.id.btn_dialog_fourinput_confirm);
        btnDialogConfirm.setOnClickListener(v1 -> {
            String password = etPassword.getText().toString().trim();

            if (password.isEmpty()) {
                etPassword.setError("Password is required to modify your information.");
                etPassword.requestFocus();
                return;
            }

            HashMap<String, Object> hash =
                    hashEditInfo(etUsername, etBirthday, etFullName);

            databaseHelper.modifyUserOnDB((userDetails, isSuccessful, message) -> {
                editAccDialog.dismiss();

                if (isSuccessful) {
                    Toast.makeText(getContext(), "Your information was successfully modified.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Your information was not modified.", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(v1.getContext(), UserProfileActivity.class);
                startActivity(intent);
            }, hash, user.getEmail(), password);
        });

        editAccDialog.show();
    }

    /**
     * Creates the input dialog for when the user wants to delete their account
     */
    private void createDeleteAccountDialog() {
        deleteAccDialog = new CustomDialog(getView().getContext());
        deleteAccDialog.setDialogType(CustomDialog.TWO_INPUT);
        deleteAccDialog.setTwoInputComponents(
                "We're sad to see you go!",
                "Enter your email:",
                "Enter your password:",
                "Delete Account"
        );

        EditText etEmail = deleteAccDialog.findViewById(R.id.et_dialog_twoinputs_one);
        EditText etPassword = deleteAccDialog.findViewById(R.id.et_dialog_twoinputs_two);

        Button btnDialogConfirm = deleteAccDialog.findViewById(R.id.btn_dialog_twoinputs_confirm);
        btnDialogConfirm.setOnClickListener(v1 -> {
            verifyDeleteInfo(etEmail, etPassword);
            databaseHelper.deleteUser((userDetails, isSuccessful, message) -> {
                if (isSuccessful)
                    deleteAccount();
                else {
                    Toast.makeText(v1.getContext(), message, Toast.LENGTH_LONG);
                    deleteAccDialog.dismiss();
                }
            }, etEmail.getText().toString().trim(), etPassword.getText().toString().trim());
        });

        deleteAccDialog.show();
    }

    /**
     * Hashes the user's information for modifying information in the db
     * @param etUsername the EditText for the user's username
     * @param etBirthday the EditText for the user's birthday
     * @param etFullName the EditText for the user's full name
     * @return the hashed information
     */
    private HashMap<String, Object> hashEditInfo(EditText etUsername,
                                                   EditText etBirthday,
                                                   EditText etFullName) {
        HashMap<String, Object> hash = new HashMap<>();
        String username = etUsername.getText().toString().trim();
        String birthday = etBirthday.getText().toString().trim();
        String name = etFullName.getText().toString().trim();

        if (!username.isEmpty())
            hash.put("userName", username);

        if (!name.isEmpty())
            hash.put("fullName", name);

        CustomDate customBirthday;
        if (!birthday.isEmpty()) {
            String [] temp = birthday.split("\\.");

            int month = Integer.parseInt(temp[1]);
            int day = Integer.parseInt(temp[0]);
            int year = Integer.parseInt(temp[2]);

            customBirthday = new CustomDate(year, month, day, 0, 0);

            hash.put("birthday", customBirthday);
        }

        return hash;
    }

    /**
     * Verifys the information entered by the user
     * @param etEmail the EditText for the user's email
     * @param etPassword the EditText for the user's password
     */
    private void verifyDeleteInfo(EditText etEmail, EditText etPassword) {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if(email.isEmpty()) {
            etEmail.setError("Email is required.");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please provide a valid e-mail address.");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required.");
            etPassword.requestFocus();
            return;
        }
    }

    /**
     * Deletes the user's account from the device and the database
     */
    private void deleteAccount() {
        spEditor.remove(Keys.KEY_EMAIL.name());
        spEditor.remove(Keys.KEY_PASSWORD.name());
        spEditor.apply();

        Toast.makeText(getView().getContext(), "Your account was successfully deleted.", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(getView().getContext(), InitActivity.class);
        getView().getContext().startActivity(i);
    }

    /**
     * Creates the OK dialog for displaying the About Us information
     */
    private void createAboutUsDialog() {
        aboutUsDialog = new CustomDialog(getView().getContext());
        aboutUsDialog.setDialogType(CustomDialog.OK);
        aboutUsDialog.setOKComponents(
                getString(R.string.about_title),
                getString(R.string.about_text),
                R.drawable.logo_xl
        );

        ImageView ivIcon = aboutUsDialog.findViewById(R.id.iv_dialog_ok_icon);
        ivIcon.setAdjustViewBounds(true);

        aboutUsDialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        savePreferences();
    }

    /**
     * Saves user preferences based on switch states
     */
    private void savePreferences() {
        boolean deepfocus = this.swDeepFocus.isChecked();
        boolean dimScreen = this.swDimScreen.isChecked();
        boolean keepscreenon = this.swKeepScreenOn.isChecked();
        boolean notifs = this.swNotifs.isChecked();
        boolean pkmncries = this.swPkmnCries.isChecked();

        this.spEditor.putBoolean(Keys.KEY_DEEPFOCUS.name(), deepfocus);
        this.spEditor.putBoolean(Keys.KEY_DIMSCREEN.name(), dimScreen);
        this.spEditor.putBoolean(Keys.KEY_KEEPSCREENON.name(), keepscreenon);
        this.spEditor.putBoolean(Keys.KEY_NOTIFS.name(), notifs);
        this.spEditor.putBoolean(Keys.KEY_PKMNCRIES.name(), pkmncries);
        this.spEditor.apply();
    }
}