package com.mobdeve.s11.pokeplan;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsFragment extends Fragment {
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

    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

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
        this.btnAbout = view.findViewById(R.id.btn_settings_about);
        this.btnEditAcc = view.findViewById(R.id.btn_settings_editacc);
        this.btnFreqQues = view.findViewById(R.id.btn_settings_faq);
        this.btnDeleteAcc = view.findViewById(R.id.btn_settings_delete_acc);
        setButtonListeners();

        this.swDeepFocus = view.findViewById(R.id.sw_deepfocus);
        this.swDimScreen = view.findViewById(R.id.sw_dimscreen);
        this.swNotifs = view.findViewById(R.id.sw_notifs);
        this.swPkmnCries = view.findViewById(R.id.sw_pkmncries);
        this.swKeepScreenOn = view.findViewById(R.id.sw_screenon);
        setSwitchFont(view);
    }

    private void setButtonListeners () {
        this.btnAbout.setOnClickListener(v -> initAbout (v));
        this.btnFreqQues.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), FaqsActivity.class);
            v.getContext().startActivity(i);
        });
        this.btnEditAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        this.btnDeleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccDialog (v);
            }
        });
    }

    private void deleteAccDialog (View v) {
        dialogDelete = new Dialog(v.getContext());
        dialogDelete.setContentView(R.layout.dialog_twoinputs);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);

        dialogDelete.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
        dialogDelete.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button btnDialogCancel = (Button) dialogDelete.findViewById(R.id.btn_delete_account_cancel);
        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDelete.dismiss();
            }
        });

        Button btnDialogConfirm = (Button) dialogDelete.findViewById(R.id.btn_delete_account_confirm);
        btnDialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEmail = (EditText) dialogDelete.findViewById(R.id.et_dialog_delete_email);
                etPassword = (EditText) dialogDelete.findViewById(R.id.et_dialog_delete_password);
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                Log.d("hello pare", email);
                Log.d("hello pare", password);
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

                Intent i = new Intent(v.getContext(), InitActivity.class);
                v.getContext().startActivity(i);
            }
        });

        dialogDelete.show();
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

        ImageView ivIcon = (ImageView)  dialogAbout.findViewById(R.id.iv_dialog_ok_icon);
        ivIcon.setImageResource(R.drawable.logo_xl);
        ivIcon.setAdjustViewBounds(true);

        Button btnDialogOk = (Button) dialogAbout.findViewById(R.id.btn_dialog_ok);
        btnDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAbout.dismiss();
            }
        });

        dialogAbout.show();
    }

    private void setSwitchFont(View view) {
        swDeepFocus.setTypeface(ResourcesCompat.getFont(view.getContext(), R.font.raleway_medium));
        swDimScreen.setTypeface(ResourcesCompat.getFont(view.getContext(), R.font.raleway_medium));
        swKeepScreenOn.setTypeface(ResourcesCompat.getFont(view.getContext(), R.font.raleway_medium));
        swNotifs.setTypeface(ResourcesCompat.getFont(view.getContext(), R.font.raleway_medium));
        swPkmnCries.setTypeface(ResourcesCompat.getFont(view.getContext(), R.font.raleway_medium));
    }

}