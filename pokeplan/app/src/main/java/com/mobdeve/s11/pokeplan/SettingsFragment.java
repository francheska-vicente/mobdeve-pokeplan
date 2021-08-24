package com.mobdeve.s11.pokeplan;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< Updated upstream
import android.widget.Button;
=======
>>>>>>> Stashed changes
import android.widget.Switch;

public class SettingsFragment extends Fragment {
    private Switch swDeepFocus;
    private Switch swDimScreen;
    private Switch swKeepScreenOn;
    private Switch swNotifs;
    private Switch swPkmnCries;

    private Button btnEditAcc;
    private Button btnFreqQues;
    private Button btnAbout;

    private Switch swPkmncries;
    private Switch swNotifs;

    private Switch swScreenOn;
    private Switch swDimScreen;
    private Switch swDeepFocus;

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
        setButtonListeners();

        this.swDeepFocus = view.findViewById(R.id.sw_deepfocus);
        this.swDimScreen = view.findViewById(R.id.sw_dimscreen);
        this.swNotifs = view.findViewById(R.id.sw_notifs);
        this.swPkmncries = view.findViewById(R.id.sw_pkmncries);
        this.swScreenOn = view.findViewById(R.id.sw_screenon);
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
    }

    private void initAbout (View v) {

    }

    private void setSwitchFont(View view) {
        swDeepFocus.setTypeface(ResourcesCompat.getFont(view.getContext(), R.font.raleway_medium));
        swDimScreen.setTypeface(ResourcesCompat.getFont(view.getContext(), R.font.raleway_medium));
        swKeepScreenOn.setTypeface(ResourcesCompat.getFont(view.getContext(), R.font.raleway_medium));
        swNotifs.setTypeface(ResourcesCompat.getFont(view.getContext(), R.font.raleway_medium));
        swPkmnCries.setTypeface(ResourcesCompat.getFont(view.getContext(), R.font.raleway_medium));
    }

}