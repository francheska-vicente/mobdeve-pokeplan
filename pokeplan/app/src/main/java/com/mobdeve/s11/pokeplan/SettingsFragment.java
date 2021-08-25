package com.mobdeve.s11.pokeplan;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.auth.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

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

    private Dialog dialogAbout;
    private Dialog dialogDelete;
    private Dialog dialogEdit;


    private EditText etEmail;
    private EditText etPassword;

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
        initComponents(view);
        return view;
    }

    private void initComponents (View view) {
        this.sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        this.spEditor = this.sp.edit();

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

    private void setButtonListeners () {
        this.btnAbout.setOnClickListener(this::initAbout);
        this.btnFreqQues.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), FaqsActivity.class);
            v.getContext().startActivity(i);
        });
        this.btnEditAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog (v);
            }
        });

        this.btnDeleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccDialog (v);
            }
        });
    }

    private void editDialog (View v) {
        dialogEdit = new Dialog(v.getContext());
        dialogEdit.setContentView(R.layout.dialog_threeinputs);

        EditText etUsername = dialogEdit.findViewById(R.id.et_dialog_edit_username);
        EditText etBirthday = dialogEdit.findViewById(R.id.et_dialog_edit_birthday);
        EditText etPassword = dialogEdit.findViewById(R.id.et_dialog_edit_password);
        EditText etFullName = dialogEdit.findViewById(R.id.et_dialog_edit_name);

        etBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendarStart = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener dateStart = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month,
                                          int day) {
                        calendarStart.set(Calendar.YEAR, year);
                        calendarStart.set(Calendar.MONTH, month);
                        calendarStart.set(Calendar.DAY_OF_MONTH, day);

                        String myFormat = "dd.MM.yy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ROOT);

                        etBirthday.setText(sdf.format(calendarStart.getTime()));
                    }
                };

                int month = Calendar.getInstance().get(Calendar.MONTH);
                int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                int year = Calendar.getInstance().get(Calendar.YEAR);

                int finalSYear = year;
                int finalSDay = day;
                int finalSMonth = month;
                etBirthday.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(v.getContext(), dateStart, finalSYear, finalSMonth,
                                finalSDay).show();
                    }
                });
            }
        });

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);

        dialogEdit.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
        dialogEdit.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button btnDialogCancel = (Button) dialogEdit.findViewById(R.id.btn_edit_account_cancel);
        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEdit.dismiss();
            }
        });

        Button btnDialogConfirm = (Button) dialogEdit.findViewById(R.id.btn_edit_account_confirm);
        btnDialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString().trim();
                String username = etUsername.getText().toString().trim();
                String birthday = etBirthday.getText().toString().trim();
                String name = etFullName.getText().toString().trim();

                if (password.isEmpty()) {
                    etPassword.setError("Password is required to modify your information.");
                    etPassword.requestFocus();
                    return;
                }

                HashMap<String, Object> hash = new HashMap<>();

                if (!username.isEmpty()) {
                    hash.put("userName", username);
                }

                CustomDate customBirthday = null;
                if (!birthday.isEmpty()) {
                    String [] temp = birthday.split("\\.");

                    int month = Integer.parseInt(temp[1]);
                    int day = Integer.parseInt(temp[0]);
                    int year = Integer.parseInt(temp[2]);

                    customBirthday = new CustomDate(year, month, day, 0, 0);

                    hash.put("birthday", customBirthday);
                }

                if (!name.isEmpty()) {
                    hash.put("fullName", name);
                }

                UserSingleton.getUser().updateUserOnDB(hash, password, customBirthday);
                dialogEdit.dismiss();
                Intent intent = new Intent(v.getContext(), UserProfileActivity.class);
                startActivity(intent);
            }
        });

        dialogEdit.show();
    }

    private void deleteAccDialog (View v) {
        dialogDelete = new Dialog(v.getContext());
        dialogDelete.setContentView(R.layout.dialog_twoinputs);

        EditText etEmail = (EditText) dialogDelete.findViewById(R.id.et_dialog_delete_email);
        EditText etPassword = (EditText) dialogDelete.findViewById(R.id.et_dialog_delete_password);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);

        dialogDelete.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
        dialogDelete.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button btnDialogCancel = dialogDelete.findViewById(R.id.btn_delete_account_cancel);
        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDelete.dismiss();
            }
        });

        Button btnDialogConfirm = dialogDelete.findViewById(R.id.btn_delete_account_confirm);
        btnDialogConfirm.setOnClickListener(v1 -> {
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

            UserSingleton.getUser().deleteUser(email, password);
            spEditor.remove(Keys.KEY_EMAIL.name());
            spEditor.remove(Keys.KEY_PASSWORD.name());
            spEditor.apply();

            Intent i = new Intent(v1.getContext(), InitActivity.class);
            v1.getContext().startActivity(i);
        });

        dialogDelete.show();
    }


    private void setSwitchFont(View view) {
        swDeepFocus.setTypeface(ResourcesCompat.getFont(view.getContext(), R.font.raleway_medium));
        swDimScreen.setTypeface(ResourcesCompat.getFont(view.getContext(), R.font.raleway_medium));
        swKeepScreenOn.setTypeface(ResourcesCompat.getFont(view.getContext(), R.font.raleway_medium));
        swNotifs.setTypeface(ResourcesCompat.getFont(view.getContext(), R.font.raleway_medium));
        swPkmnCries.setTypeface(ResourcesCompat.getFont(view.getContext(), R.font.raleway_medium));
    }

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

    private void initAbout (View v) {
        dialogAbout = new Dialog(v.getContext());
        dialogAbout.setContentView(R.layout.dialog_ok);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        dialogAbout.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
        dialogAbout.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tvdialogtitle = (TextView) dialogAbout.findViewById(R.id.tv_dialog_ok_title);
        tvdialogtitle.setText(R.string.about_title);
        TextView tvdialogtext = (TextView) dialogAbout.findViewById(R.id.tv_dialog_ok_text);
        tvdialogtext.setText(R.string.about_text);

        ImageView ivIcon = (ImageView) dialogAbout.findViewById(R.id.iv_dialog_ok_icon);
        ivIcon.setImageResource(R.drawable.logo_xl);
        ivIcon.setAdjustViewBounds(true);

        Button btnDialogOk = (Button) dialogAbout.findViewById(R.id.btn_dialog_ok);
        btnDialogOk.setOnClickListener(v1 -> dialogAbout.dismiss());

        dialogAbout.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        savePreferences();
    }
}