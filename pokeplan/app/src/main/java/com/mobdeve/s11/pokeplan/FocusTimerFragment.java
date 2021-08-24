package com.mobdeve.s11.pokeplan;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class FocusTimerFragment extends Fragment {
    private SharedPreferences sp;

    private ImageView ivegg;

    private Button btnfocustimer;
    private ImageButton ibhoursup;
    private ImageButton ibhoursdown;
    private ImageButton ibminsup;
    private ImageButton ibminsdown;
    private ImageButton ibsecsup;
    private ImageButton ibsecsdown;
    private ImageButton ibinfo;
    private TextView tvhours;
    private TextView tvmins;
    private TextView tvsecs;
    private TextView tvcaption;

    private Timer timer;
    private CountDownTimer countdown;

    private CustomDialog infodialog;
    private CustomDialog timererrordialog;
    private CustomDialog confirmstoptimerdialog;
    private CustomDialog  hatcheggdialog;
    private CustomDialog stopTimerdialog;

    private boolean timerIsDone;
    private boolean timerIsStopped;

    public FocusTimerFragment() {
    }

    public static FocusTimerFragment newInstance() {
        return new FocusTimerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_focustimer, container, false);

        initComponents(view);
        timer = new Timer();
        this.timerIsStopped = false;
        return view;
    }

    private void initComponents(View view) {
        this.sp = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext());

        this.tvhours = view.findViewById(R.id.tv_focustimer_hours);
        this.tvmins = view.findViewById(R.id.tv_focustimer_mins);
        this.tvsecs = view.findViewById(R.id.tv_focustimer_secs);
        this.tvcaption = view.findViewById(R.id.tv_focustimer_tagline);

        this.ibinfo = view.findViewById(R.id.ib_focustimer_info);
        this.ibhoursup = view.findViewById(R.id.ib_timer_hours_up);
        this.ibhoursdown = view.findViewById(R.id.ib_timer_hours_down);
        this.ibminsup = view.findViewById(R.id.ib_timer_mins_up);
        this.ibminsdown = view.findViewById(R.id.ib_timer_mins_down);
        this.ibsecsup = view.findViewById(R.id.ib_timer_secs_up);
        this.ibsecsdown = view.findViewById(R.id.ib_timer_secs_down);

        this.ivegg = view.findViewById(R.id.iv_egg);

        this.btnfocustimer = view.findViewById(R.id.btn_focustimer_main);
        this.setInitialButtonListeners();
    }

    private void setInitialButtonListeners() {
        this.ibhoursup.setOnClickListener(view -> {
            if (timer.getHours() < 99) {
                timer.setHours(timer.getHours()+1);
                tvhours.setText(String.format(Locale.getDefault(), "%02d", timer.getHours()));
            }
        });
        ibhoursdown.setOnClickListener(view -> {
            if (timer.getHours() > 0) {
                timer.setHours(timer.getHours()-1);
                tvhours.setText(String.format(Locale.getDefault(), "%02d", timer.getHours()));
            }
        });
        ibminsup.setOnClickListener(view -> {
            if (timer.getMins() < 59) {
                timer.setMins(timer.getMins()+1);
                tvmins.setText(String.format(Locale.getDefault(), "%02d", timer.getMins()));
            }
        });
        ibminsdown.setOnClickListener(view -> {
            if (timer.getMins() > 0) {
                timer.setMins(timer.getMins()-1);
                tvmins.setText(String.format(Locale.getDefault(), "%02d", timer.getMins()));
            }
        });
        ibsecsup.setOnClickListener(view -> {
            if (timer.getSecs() < 59) {
                timer.setSecs(timer.getSecs()+1);
                tvsecs.setText(String.format(Locale.getDefault(), "%02d", timer.getSecs()));
            }
        });
        ibsecsdown.setOnClickListener(view -> {
            if (timer.getSecs() > 0) {
                timer.setSecs(timer.getSecs()-1);
                tvsecs.setText(String.format(Locale.getDefault(), "%02d", timer.getSecs()));
            }
        });

        ibinfo.setOnClickListener(view -> createInfoDialog());
        btnfocustimer.setOnClickListener(view -> {
            if (timeToMilliseconds(timer) >= (1000 * 60 * 5))
                startTimer();
            else createTimeErrorDialog();
        });
    }

    private void setStartComponents() {
        tvcaption.setText(R.string.focustimer_start_tagline);
        btnfocustimer.setText(R.string.focustimer_start_button);
        tvhours.setText("00");
        tvmins.setText("10");
        tvsecs.setText("00");
        ivegg.setImageResource(R.drawable.egg);
        setBottomBarEnabled(true);
        dimScreen(false);
    }

    private void setStartButtonListeners() {
        this.setTimerButtonsVisibility(true);
        this.btnfocustimer.setOnClickListener(view -> {
            if (timeToMilliseconds(timer) >= (1000 * 60 * 5))
                startTimer();
            else createTimeErrorDialog();
        });
    }

    private void setOngoingComponents() {
        setTimerButtonsVisibility(false);
        tvcaption.setText(getString(R.string.focustimer_ongoing_tagline));
        btnfocustimer.setText(getString(R.string.focustimer_stop_button));

        dimScreen(true);
        setBottomBarEnabled(false);
    }

    private void setOngoingButtonListeners() {
        btnfocustimer.setOnClickListener(view -> createConfirmStopTimerDialog());
    }

    private void setFinishComponents() {
        tvcaption.setText(R.string.focustimer_finish_tagline);
        btnfocustimer.setText(R.string.focustimer_finish_button);

        dimScreen(false);
        setBottomBarEnabled(true);
    }

    private void setFinishButtonListeners() {
        btnfocustimer.setOnClickListener(view -> resetTimer());
    }

    private void adjustTimerComponent(long fin) {
        int hours = (int) TimeUnit.MILLISECONDS.toHours(fin);
        int mins = (int) (TimeUnit.MILLISECONDS.toMinutes(fin) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(fin)));
        int secs = (int) (TimeUnit.MILLISECONDS.toSeconds(fin) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(fin)));

        tvhours.setText(String.format(Locale.getDefault(), "%02d", hours));
        tvmins.setText(String.format(Locale.getDefault(), "%02d", mins));
        tvsecs.setText(String.format(Locale.getDefault(), "%02d", secs));
    }

    private void startTimer() {
        timerIsDone = false;
        timerIsStopped = false;

        setOngoingComponents();
        setOngoingButtonListeners();

        // start countdown timer
        long time = timeToMilliseconds(timer) + 500;
        countdown = new CountDownTimer(time, 1000) {
            public void onTick(long fin) {
                adjustTimerComponent(fin);
            }
            public void onFinish() {
                finishTimer();
            }
        };
        countdown.start();
    }

    private void stopTimer() {
        timerIsStopped = false;
        countdown.cancel();
        resetTimer();
        stopTimerDialog();
    }

    private void finishTimer() {
        timerIsDone = true;
        timerIsStopped = false;

        setFinishComponents();
        setFinishButtonListeners();

        Egg egg = new Egg(timer);
        Pokemon hatch = egg.generatePokemon();
        UserSingleton.getUser().addPokemon(hatch, true);
        UserSingleton.getUser().getUserDetails().addHatchedPkmn();

        createHatchEggDialog(hatch);
    }

    private void resetTimer() {
        timer = new Timer();
        setStartComponents();
        setStartButtonListeners();
    }

    private void createConfirmStopTimerDialog() {
        confirmstoptimerdialog = new CustomDialog(getView().getContext());
        confirmstoptimerdialog.setDialogType(CustomDialog.CONFIRM);

        confirmstoptimerdialog.setConfirmComponents(
                getString(R.string.focustimer_dialog_confirm_title),
                getString(R.string.focustimer_dialog_confirm_text),
                R.drawable.sunny_side_up,
                getString(R.string.focustimer_dialog_confirm_title)
        );

        Button btndialogconfirm = confirmstoptimerdialog.findViewById(R.id.btn_dialog_confirm);
        btndialogconfirm.setOnClickListener(view -> {
            if (!timerIsDone)
                this.stopTimer();
            confirmstoptimerdialog.dismiss();
        });

        confirmstoptimerdialog.show();
    }

    private void createHatchEggDialog(Pokemon hatch) {
        hatcheggdialog = new CustomDialog(getView().getContext());
        hatcheggdialog.setDialogType(CustomDialog.OK);

        String text = getString(R.string.focustimer_dialog_hatch_text)
                + " " + hatch.getSpecies() + "!";
        hatcheggdialog.setOKComponents(
                getString(R.string.focustimer_dialog_hatch_title),
                text,
                getImageId(getContext(), "pkmn_" + hatch.getDexNum()));

        hatcheggdialog.show();

        MediaPlayer mediaPlayer = MediaPlayer.create(getView().getContext(), R.raw.levelup);
        mediaPlayer.start();
        hatch.playPokemonCry();
    }

    private void createTimeErrorDialog() {
        timererrordialog = new CustomDialog(getView().getContext());
        timererrordialog.setDialogType(CustomDialog.ERROR);
        timererrordialog.setErrorComponents(
                getString(R.string.focustimer_dialog_error_title),
                getString(R.string.focustimer_dialog_error_text));

        timererrordialog.show();
    }

    private void createInfoDialog() {
        infodialog = new CustomDialog(getView().getContext());
        infodialog.setDialogType(CustomDialog.OK);
        infodialog.setOKComponents(
                getString(R.string.focustimer_dialog_info_title),
                getString(R.string.focustimer_dialog_info_text),
                R.drawable.egg);

        infodialog.show();
    }

    public void stopTimerDialog() {
        stopTimerdialog = new CustomDialog(getView().getContext());
        stopTimerdialog.setDialogType(CustomDialog.OK);
        stopTimerdialog.setOKComponents(
                getString(R.string.focustimer_dialog_egg_break_title),
                getString(R.string.focustimer_dialog_egg_break_text),
                R.drawable.sunny_side_up);

        stopTimerdialog.show();
    }

    private void setTimerButtonsVisibility(boolean toggle) {
        if (toggle) {
            ibhoursup.setVisibility(View.VISIBLE);
            ibhoursdown.setVisibility(View.VISIBLE);
            ibminsup.setVisibility(View.VISIBLE);
            ibminsdown.setVisibility(View.VISIBLE);
            ibsecsup.setVisibility(View.VISIBLE);
            ibsecsdown.setVisibility(View.VISIBLE);
            ibinfo.setVisibility(View.VISIBLE);
        }
        else {
            ibhoursup.setVisibility(View.GONE);
            ibhoursdown.setVisibility(View.GONE);
            ibminsup.setVisibility(View.GONE);
            ibminsdown.setVisibility(View.GONE);
            ibsecsup.setVisibility(View.GONE);
            ibsecsdown.setVisibility(View.GONE);
            ibinfo.setVisibility(View.GONE);
        }
    }

    private void setBottomBarEnabled(boolean toggle){
        BottomNavigationView navView = getActivity().findViewById(R.id.bottom_nav_view);
        for (int i = 0; i < navView.getMenu().size(); i++) {
            navView.getMenu().getItem(i).setEnabled(toggle);
        }
    }

    private void dimScreen(boolean dimmed) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        if (dimmed)
            lp.screenBrightness = 0.01f;
        else
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;

        if (sp.getBoolean(Keys.KEY_DIMSCREEN.name(), true))
            getActivity().getWindow().setAttributes(lp);
    }

    private long timeToMilliseconds (Timer timer) {
        return TimeUnit.MILLISECONDS.convert(timer.getHours(), TimeUnit.HOURS) +
                TimeUnit.MILLISECONDS.convert(timer.getMins(), TimeUnit.MINUTES) +
                TimeUnit.MILLISECONDS.convert(timer.getSecs(), TimeUnit.SECONDS);
    }

    private int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timerIsStopped) stopTimer();
    }
}