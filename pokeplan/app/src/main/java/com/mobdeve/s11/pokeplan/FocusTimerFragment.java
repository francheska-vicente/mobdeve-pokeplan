package com.mobdeve.s11.pokeplan;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class FocusTimerFragment extends Fragment implements SensorEventListener {
    private boolean dimmedEnabled;
    private boolean deepFocusEnabled;
    private boolean screenOnEnabled;
    private boolean soundEnabled;

    private SensorManager sensorManager;
    private Sensor sensor;
    private float accel;
    private float accelCurrent;
    private float accelLast;

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

    private CustomDialog confirmstoptimerdialog;

    private boolean timerIsDone;
    private boolean timerIsStopped;
    private DatabaseHelper databaseHelper;
    private UserDetails user;

    public FocusTimerFragment() {
    }

    public FocusTimerFragment() {}
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
        databaseHelper = new DatabaseHelper();

        databaseHelper.getUserDetails(new FirebaseCallbackUser() {
            @Override
            public void onCallbackUser(UserDetails userDetails, Boolean isSuccessful, String message) {
                user = userDetails;
            }
        });

        this.initComponents(view);
        this.setInitialButtonListeners();
        this.initMotionSensor();
        this.initUserPreferences();

        this.timer = new Timer();
        this.timerIsOngoing = false;
        this.timerIsFinished = false;

        return view;
    }

    /**
     * Initializes the layout's components.
     * @param view the view of the fragment
     */
    private void initComponents(View view) {
        this.ivegg = view.findViewById(R.id.iv_egg);
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

        this.btnfocustimer = view.findViewById(R.id.btn_focustimer_main);
    }

    /**
     * Initializes the user preferences for enabling certain functions of the focus timer.
     */
    private void initUserPreferences() {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext());

        this.dimmedEnabled = sp.getBoolean(Keys.KEY_DIMSCREEN.name(), false);
        this.deepFocusEnabled = sp.getBoolean(Keys.KEY_DEEPFOCUS.name(), true);
        this.screenOnEnabled = sp.getBoolean(Keys.KEY_KEEPSCREENON.name(), true);
        this.soundEnabled = sp.getBoolean(Keys.KEY_PKMNCRIES.name(), true);
    }

    /**
     * Initializes the motion sensor for the Deep Focus Mode option.
     */
    private void initMotionSensor() {
        this.sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        this.accel = 0.00f;
        this.accelCurrent = SensorManager.GRAVITY_EARTH;
        this.accelLast = SensorManager.GRAVITY_EARTH;
    }

    /**
     * Sets the onClickListeners for all buttons when the timer is in the initial state
     */
    private void setInitialButtonListeners() {
        this.ibhoursup.setOnClickListener(view -> {
            if (timer.getHours() < 99) {
                timer.setHours(timer.getHours()+1);
                tvhours.setText(String.format(Locale.getDefault(), "%02d", timer.getHours()));
            }
        });
        this.ibhoursdown.setOnClickListener(view -> {
            if (timer.getHours() > 0) {
                timer.setHours(timer.getHours()-1);
                tvhours.setText(String.format(Locale.getDefault(), "%02d", timer.getHours()));
            }
        });
        this.ibminsup.setOnClickListener(view -> {
            if (timer.getMins() < 59) {
                timer.setMins(timer.getMins()+1);
                tvmins.setText(String.format(Locale.getDefault(), "%02d", timer.getMins()));
            }
        });
        this.ibminsdown.setOnClickListener(view -> {
            if (timer.getMins() > 0) {
                timer.setMins(timer.getMins()-1);
                tvmins.setText(String.format(Locale.getDefault(), "%02d", timer.getMins()));
            }
        });
        this.ibsecsup.setOnClickListener(view -> {
            if (timer.getSecs() < 59) {
                timer.setSecs(timer.getSecs()+1);
                tvsecs.setText(String.format(Locale.getDefault(), "%02d", timer.getSecs()));
            }
        });
        this.ibsecsdown.setOnClickListener(view -> {
            if (timer.getSecs() > 0) {
                timer.setSecs(timer.getSecs()-1);
                tvsecs.setText(String.format(Locale.getDefault(), "%02d", timer.getSecs()));
            }
        });

        this.ibinfo.setOnClickListener(view -> createInfoDialog());

        // checks if timer is set to more than 5 minutes
        this.btnfocustimer.setOnClickListener(view -> {
            if (timer.convertToMilliseconds() >= (1000 * 60 * 5))
                startTimer();
            else createTimeErrorDialog();
        });
    }

    /**
     * Sets the view components for when the timer is in the start state
     */
    private void setStartComponents() {
        this.tvcaption.setText(R.string.focustimer_start_tagline);
        this.btnfocustimer.setText(R.string.focustimer_start_button);
        this.ivegg.setImageResource(R.drawable.egg);

        this.tvhours.setText("00");
        this.tvmins.setText("10");
        this.tvsecs.setText("00");

        this.setBottomBarEnabled(true);
        this.dimScreen(false);
        this.keepScreenOn(false);
        this.deepFocusMode(false);
    }

    /**
     * Sets the onClickListeners for all buttons for when the timer is in the start state
     */
    private void setStartButtonListeners() {
        this.setTimerButtonsVisibility(true);

        // checks if timer is set to more than 5 minutes
        this.btnfocustimer.setOnClickListener(view -> {
            if (timer.convertToMilliseconds() >= (1000 * 60 * 5))
                startTimer();
            else createTimeErrorDialog();
        });
    }

    /**
     * Sets the view components for when the timer is ongoing
     */
    private void setOngoingComponents() {
        this.setTimerButtonsVisibility(false);
        this.tvcaption.setText(getString(R.string.focustimer_ongoing_tagline));
        this.btnfocustimer.setText(getString(R.string.focustimer_stop_button));

        this.setBottomBarEnabled(false);
        this.dimScreen(true);
        this.keepScreenOn(true);
        this.deepFocusMode(true);
    }

    /**
     * Sets the onClickListeners for all buttons for when the timer is ongoing
     */
    private void setOngoingButtonListeners() {
        this.btnfocustimer.setOnClickListener(view -> createConfirmStopTimerDialog());
    }

    /**
     * Sets the view components for when the timer was successfully finished
     */
    private void setFinishComponents() {
        this.tvcaption.setText(R.string.focustimer_finish_tagline);
        this.btnfocustimer.setText(R.string.focustimer_finish_button);

        this.setBottomBarEnabled(true);
        this.dimScreen(false);
        this.keepScreenOn(false);
        this.deepFocusMode(false);
    }

    /**
     * Sets the onClickListeners for all buttons for when the timer was successfully finished
     */
    private void setFinishButtonListeners() {
        this.btnfocustimer.setOnClickListener(view -> resetTimer());
    }

    /**
     * Adjusts the timer view based on the time given
     * @param time the time left in milliseconds
     */
    private void adjustTimerComponent(long time) {
        // converts milliseconds to hours, mins, secs
        int hours = (int) TimeUnit.MILLISECONDS.toHours(time);
        int mins = (int) (TimeUnit.MILLISECONDS.toMinutes(time) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)));
        int secs = (int) (TimeUnit.MILLISECONDS.toSeconds(time) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));

        this.tvhours.setText(String.format(Locale.getDefault(), "%02d", hours));
        this.tvmins.setText(String.format(Locale.getDefault(), "%02d", mins));
        this.tvsecs.setText(String.format(Locale.getDefault(), "%02d", secs));
    }

    /**
     * Shows or hides all timer buttons.
     * @param toggle if true, buttons are visible
     */
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

    /**
     * Enables or disables the bottom navigation bar.
     * @param toggle if true, bottom navigation bar is enabled.
     */
    private void setBottomBarEnabled(boolean toggle){
        BottomNavigationView navView = getActivity().findViewById(R.id.bottom_nav_view);
        for (int i = 0; i < navView.getMenu().size(); i++) {
            navView.getMenu().getItem(i).setEnabled(toggle);
        }
    }

    /**
     * Helper function to get the image ID given the image name.
     * @param imageName the name of the image
     * @return the image id of the image
     */
    private int getImageId(String imageName) {
        return getContext().getResources().getIdentifier("drawable/" +
                imageName, null, getContext().getPackageName());
    }

    /**
     * Starts the Focus Timer according to the time inputted by the user.
     */
    private void startTimer() {
        this.timerIsFinished = false;
        this.timerIsOngoing = true;

        this.setOngoingComponents();
        this.setOngoingButtonListeners();

        // starts countdown timer
        long time = timer.convertToMilliseconds() + 500;
        this.countdown = new CountDownTimer(time, 1000) {
            public void onTick(long fin) {
                adjustTimerComponent(fin);
            }
            public void onFinish() {
                finishTimer();
            }
        };
        this.countdown.start();
    }

    /**
     * Resets timer and sets view components to start state.
     */
    private void resetTimer() {
        this.timer = new Timer();
        this.timerIsOngoing = false;

        this.setStartComponents();
        this.setStartButtonListeners();
    }

    /**
     * Manually stops and resets the timer.
     */
    private void stopTimer() {
        this.countdown.cancel();
        this.resetTimer();
        this.stopTimerDialog();
    }

    /**
     * Finishes the focus timer and hatches a random Pokemon.
     */
    private void finishTimer() {
        this.timerIsFinished = true;
        this.timerIsOngoing = false;

        // if the confirm dialog is still on screen, dismiss it
        if (confirmstoptimerdialog != null)
            confirmstoptimerdialog.dismiss();

        this.setFinishComponents();
        this.setFinishButtonListeners();

        // generate a random pokemon based on the timer length
        Egg egg = new Egg(timer, deepFocusEnabled);
        Pokemon hatch = egg.generatePokemon();

        databaseHelper.addPokemon(new FirebaseCallbackPokemon() {
            @Override
            public void onCallbackPokemon(ArrayList<UserPokemon> list, Boolean isSuccessful, String message) {
                if (isSuccessful) {

                } else {

                }
            }
        }, true, hatch, user);

        this.createHatchEggDialog(hatch);
        this.ivegg.setImageResource(getImageId("pkmn_" + hatch.getDexNum()));
    }

    /**
     * Creates a confirm dialog for when the user wants to stop the timer.
     */
    private void createConfirmStopTimerDialog() {
        this.confirmstoptimerdialog = new CustomDialog(getView().getContext());
        this.confirmstoptimerdialog.setDialogType(CustomDialog.CONFIRM);

        this.confirmstoptimerdialog.setConfirmComponents(
                getString(R.string.focustimer_dialog_confirm_title),
                getString(R.string.focustimer_dialog_confirm_text),
                R.drawable.egg,
                getString(R.string.focustimer_dialog_confirm_title)
        );

        Button btndialogconfirm = confirmstoptimerdialog.findViewById(R.id.btn_dialog_confirm);
        btndialogconfirm.setOnClickListener(view -> {
            if (!timerIsFinished)
                this.stopTimer();
            confirmstoptimerdialog.dismiss();
        });

        this.confirmstoptimerdialog.show();
    }

    /**
     * Creates the OK dialog for when the user hatches an egg successfully.
     */
    private void createHatchEggDialog(Pokemon hatch) {
        CustomDialog hatcheggdialog = new CustomDialog(getView().getContext());
        hatcheggdialog.setDialogType(CustomDialog.OK);

        String text = getString(R.string.focustimer_dialog_hatch_text)
                + " " + hatch.getSpecies() + "!";
        hatcheggdialog.setOKComponents(
                getString(R.string.focustimer_dialog_hatch_title),
                text,
                getImageId("pkmn_" + hatch.getDexNum()));

        hatcheggdialog.show();

        this.playHatchEggSound();
        this.playPkmnCry(hatch);
    }

    /**
     * Creates the error dialog for when the user sets the time to less than 5 minutes.
     */
    private void createTimeErrorDialog() {
        CustomDialog timererrordialog = new CustomDialog(getView().getContext());
        timererrordialog.setDialogType(CustomDialog.ERROR);
        timererrordialog.setErrorComponents(
                getString(R.string.focustimer_dialog_error_title),
                getString(R.string.focustimer_dialog_error_text));

        timererrordialog.show();
    }

    /**
     * Creates the OK dialog that explains the focus timer feature.
     */
    private void createInfoDialog() {
        CustomDialog infodialog = new CustomDialog(getView().getContext());
        infodialog.setDialogType(CustomDialog.OK);
        infodialog.setOKComponents(
                getString(R.string.focustimer_dialog_info_title),
                getString(R.string.focustimer_dialog_info_text),
                R.drawable.egg);

        infodialog.show();
    }

    /**
     * Creates the OK dialog for when the timer is forcefully stopped.
     */
    public void stopTimerDialog() {
        CustomDialog stopTimerdialog = new CustomDialog(getView().getContext());
        stopTimerdialog.setDialogType(CustomDialog.OK);
        stopTimerdialog.setOKComponents(
                getString(R.string.focustimer_dialog_egg_break_title),
                getString(R.string.focustimer_dialog_egg_break_text),
                R.drawable.deadegg);

        stopTimerdialog.show();
    }

    /**
     * Plays the sound for when an egg hatches.
     */
    private void playHatchEggSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(getView().getContext(), R.raw.levelup);
        mediaPlayer.start();
    }

    /**
     * Plays a Pokemon cry.
     * @param pkmn the species of the pokemon.
     */
    private void playPkmnCry(Pokemon pkmn) {
        if (soundEnabled)
            pkmn.playPokemonCry();
    }

    /**
     * Dims the screen of the activity.
     * @param toggle if true, dim the screen
     */
    private void dimScreen(boolean toggle) {
        if (dimmedEnabled) {
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();

            if (toggle)
                lp.screenBrightness = 0.0f;
            else
                lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;

            getActivity().getWindow().setAttributes(lp);
        }
    }

    /**
     * Keeps the screen of the activity on.
     * @param toggle if true, enables flag for keeping the screen on
     */
    private void keepScreenOn(boolean toggle) {
        if (screenOnEnabled) {
            if (toggle)
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            else
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    /**
     * Enables deep focus mode.
     * @param toggle if true, registers the sensor listener
     */
    private void deepFocusMode(boolean toggle) {
        if (deepFocusEnabled) {
            if (toggle)
                this.sensorManager.registerListener(this, sensor,
                        SensorManager.SENSOR_DELAY_UI);
            else
                this.sensorManager.unregisterListener(this);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (timerIsOngoing) stopTimer();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (deepFocusEnabled) {
            float[] gravity = event.values.clone();
            float x = gravity[0];
            float y = gravity[1];
            float z = gravity[2];

            accelLast = accelCurrent;
            accelCurrent = (float) Math.sqrt(x*x + y*y + z*z);

            float delta = accelCurrent - accelLast;
            accel = accel * 0.9f + delta;

            if(accel > 5 && timerIsOngoing) stopTimer();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}