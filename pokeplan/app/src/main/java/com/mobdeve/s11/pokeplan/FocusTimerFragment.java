package com.mobdeve.s11.pokeplan;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.TimeUnit;


public class FocusTimerFragment extends Fragment {
    private final int INITIAL = 0;
    private final int START = 1;
    private final int ONGOING = 2;
    private final int FINISH = 3;

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
    private boolean timerIsDone;

    private Dialog infodialog;
    private Dialog timererrordialog;
    private Dialog confirmstoptimerdialog;
    private Dialog hatcheggdialog;

    public FocusTimerFragment() {
    }

    public static FocusTimerFragment newInstance() {
        FocusTimerFragment fragment = new FocusTimerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_focustimer, container, false);

        initView(view);
        timer = new Timer();
        return view;
    }

    private void initView(View view) {
        tvhours = view.findViewById(R.id.tv_focustimer_hours);
        tvmins = view.findViewById(R.id.tv_focustimer_mins);
        tvsecs = view.findViewById(R.id.tv_focustimer_secs);
        tvcaption = view.findViewById(R.id.tv_focustimer_tagline);

        ibinfo = view.findViewById(R.id.ib_focustimer_info);
        ibhoursup = view.findViewById(R.id.ib_timer_hours_up);
        ibhoursdown = view.findViewById(R.id.ib_timer_hours_down);
        ibminsup = view.findViewById(R.id.ib_timer_mins_up);
        ibminsdown = view.findViewById(R.id.ib_timer_mins_down);
        ibsecsup = view.findViewById(R.id.ib_timer_secs_up);
        ibsecsdown = view.findViewById(R.id.ib_timer_secs_down);

        ivegg = view.findViewById(R.id.iv_egg);

        btnfocustimer = view.findViewById(R.id.btn_focustimer_main);
        setViewComponents(INITIAL);
    }

    private void startTimer() {
        setViewComponents(ONGOING);

        // start countdown timer
        long time = timeToMilliseconds(timer) + 500;
        timerIsDone = false;
        countdown = new CountDownTimer(time, 1000) {
            public void onTick(long fin) {
                adjustTimer(fin);
            }
            public void onFinish() {
                finishTimer();
            }
        };
        countdown.start();
    }

    private void stopTimer() {
        countdown.cancel();
        resetTimer();
    }

    private void finishTimer() {
        timerIsDone = true;
        setViewComponents(FINISH);

        Egg egg = new Egg(timer);
        Pokemon hatch = egg.generatePokemon();
        createHatchEggDialog(getView(), hatch);
    }

    private void resetTimer() {
        timer = new Timer();
        setViewComponents(START);
    }

    private void adjustTimer(long fin) {
        int hours = (int) TimeUnit.MILLISECONDS.toHours(fin);
        int mins = (int) (TimeUnit.MILLISECONDS.toMinutes(fin) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(fin)));
        int secs = (int) (TimeUnit.MILLISECONDS.toSeconds(fin) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(fin)));

        tvhours.setText(addLeadingZero(hours));
        tvmins.setText(addLeadingZero(mins));
        tvsecs.setText(addLeadingZero(secs));
    }

    private void createConfirmStopTimerDialog(View view) {
        confirmstoptimerdialog = new Dialog(view.getContext());
        confirmstoptimerdialog.setContentView(R.layout.dialog_confirm);
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.40);

        confirmstoptimerdialog.getWindow().setLayout(width, height);
        confirmstoptimerdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tvdialogtitle = (TextView) confirmstoptimerdialog.findViewById(R.id.tv_dialog_title);
        tvdialogtitle.setText(R.string.focustimer_dialog_confirm_title);
        TextView tvdialogtext = (TextView) confirmstoptimerdialog.findViewById(R.id.tv_dialog_text);
        tvdialogtext.setText(R.string.focustimer_dialog_confirm_text);
        ImageView ivdialogicon = (ImageView) confirmstoptimerdialog.findViewById(R.id.iv_dialog_icon);
        ivdialogicon.setImageResource(R.drawable.sunny_side_up);

        Button btndialogcancel = (Button) confirmstoptimerdialog.findViewById(R.id.btn_dialog_cancel);
        tvdialogtitle.setText(R.string.focustimer_dialog_confirm_title);
        btndialogcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmstoptimerdialog.dismiss();
            }
        });

        Button btndialogconfirm = (Button) confirmstoptimerdialog.findViewById(R.id.btn_dialog_confirm);
        btndialogconfirm.setText(R.string.focustimer_dialog_confirm_title);
        btndialogconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timerIsDone) {
                    stopTimer();
                }
                confirmstoptimerdialog.dismiss();
            }
        });
        confirmstoptimerdialog.show();
    }

    private void createHatchEggDialog(View view, Pokemon hatch) {
        hatcheggdialog = new Dialog(view.getContext());
        hatcheggdialog.setContentView(R.layout.dialog_ok);
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.40);

        hatcheggdialog.getWindow().setLayout(width, height);
        hatcheggdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tvdialogtitle = (TextView) hatcheggdialog.findViewById(R.id.tv_dialog_ok_title);
        tvdialogtitle.setText(R.string.focustimer_dialog_hatch_title);
        TextView tvdialogtext = (TextView) hatcheggdialog.findViewById(R.id.tv_dialog_ok_text);
        String text = getString(R.string.focustimer_dialog_hatch_text) + " " + hatch.getSpecies() + "!";
        tvdialogtext.setText(text);
        ImageView ivdialogicon = (ImageView) hatcheggdialog.findViewById(R.id.iv_dialog_ok_icon);
        ivdialogicon.setImageResource(getImageId(getContext(), "pkmn_" + hatch.getDexNum()));

        Button btndialogok = (Button) hatcheggdialog.findViewById(R.id.btn_dialog_ok);
        btndialogok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivegg.setImageResource(getImageId(getContext(), "pkmn_" + hatch.getDexNum()));
                hatcheggdialog.dismiss();
            }
        });
        hatcheggdialog.show();
    }

    private void createTimeErrorDialog(View view) {
        timererrordialog = new Dialog(view.getContext());
        timererrordialog.setContentView(R.layout.dialog_error);
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.40);

        timererrordialog.getWindow().setLayout(width, height);
        timererrordialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tvdialogtitle = (TextView) timererrordialog.findViewById(R.id.tv_dialog_error_title);
        tvdialogtitle.setText(R.string.focustimer_dialog_error_title);
        TextView tvdialogtext = (TextView) timererrordialog.findViewById(R.id.tv_dialog_error_text);
        tvdialogtext.setText(R.string.focustimer_dialog_error_text);

        Button btndialogerror = (Button) timererrordialog.findViewById(R.id.btn_dialog_error);
        btndialogerror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timererrordialog.dismiss();
            }
        });
        timererrordialog.show();
    }

    private void createInfoDialog(View view) {
        infodialog = new Dialog(view.getContext());
        infodialog.setContentView(R.layout.dialog_ok);
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.40);

        infodialog.getWindow().setLayout(width, height);
        infodialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tvdialogtitle = (TextView) infodialog.findViewById(R.id.tv_dialog_ok_title);
        tvdialogtitle.setText(R.string.focustimer_dialog_info_title);
        TextView tvdialogtext = (TextView) infodialog.findViewById(R.id.tv_dialog_ok_text);
        tvdialogtext.setText(R.string.focustimer_dialog_info_text);
        ImageView ivdialogicon = (ImageView) infodialog.findViewById(R.id.iv_dialog_ok_icon);
        ivdialogicon.setImageResource(R.drawable.egg);

        Button btndialoginfo = (Button) infodialog.findViewById(R.id.btn_dialog_ok);
        btndialoginfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infodialog.dismiss();
            }
        });
        infodialog.show();
    }

    private void setViewComponents(int state) {
        switch (state) {
            case INITIAL:
                ibhoursup.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (timer.getHours() < 99) {
                            timer.setHours(timer.getHours()+1);
                            tvhours.setText(addLeadingZero(timer.getHours()));
                        }
                    }
                });
                ibhoursdown.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (timer.getHours() > 0) {
                            timer.setHours(timer.getHours()-1);
                            tvhours.setText(addLeadingZero(timer.getHours()));
                        }
                    }
                });
                ibminsup.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (timer.getMins() < 59) {
                            timer.setMins(timer.getMins()+1);
                            tvmins.setText(addLeadingZero(timer.getMins()));
                        }
                    }
                });
                ibminsdown.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (timer.getMins() > 0) {
                            timer.setMins(timer.getMins()-1);
                            tvmins.setText(addLeadingZero(timer.getMins()));
                        }
                    }
                });
                ibsecsup.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (timer.getSecs() < 59) {
                            timer.setSecs(timer.getSecs()+1);
                            tvsecs.setText(addLeadingZero(timer.getSecs()));
                        }
                    }
                });
                ibsecsdown.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (timer.getSecs() > 0) {
                            timer.setSecs(timer.getSecs()-1);
                            tvsecs.setText(addLeadingZero(timer.getSecs()));
                        }
                    }
                });
                ibinfo.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        createInfoDialog(view);
                    }
                });
                btnfocustimer.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (timeToMilliseconds(timer) >= (1000 * 60 * 5))
                            startTimer();
                        else createTimeErrorDialog(view);
                    }
                });
                break;
            case START:
                setTimerButtonsVisibility(true);
                tvcaption.setText(R.string.focustimer_start_tagline);
                btnfocustimer.setText(R.string.focustimer_start_button);
                tvhours.setText("00");
                tvmins.setText("10");
                tvsecs.setText("00");
                ivegg.setImageResource(R.drawable.egg);
                btnfocustimer.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (timeToMilliseconds(timer) >= (1000 * 60 * 5))
                            startTimer();
                        else createTimeErrorDialog(view);
                    }
                });
                setBottomBarEnabled(true);
                dimScreen(false);
                break;
            case ONGOING:
                setTimerButtonsVisibility(false);
                tvcaption.setText(getString(R.string.focustimer_ongoing_tagline));
                btnfocustimer.setText(getString(R.string.focustimer_stop_button));
                btnfocustimer.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        createConfirmStopTimerDialog(view);
                    }
                });
                dimScreen(true);
                setBottomBarEnabled(false);
                break;
            case FINISH:
                tvcaption.setText(R.string.focustimer_finish_tagline);
                btnfocustimer.setText(R.string.focustimer_finish_button);
                btnfocustimer.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        resetTimer();
                    }
                });
                dimScreen(false);
                setBottomBarEnabled(true);
                break;
        }
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
        BottomNavigationView navView = (BottomNavigationView)
                getActivity().findViewById(R.id.bottom_nav_view);
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

        getActivity().getWindow().setAttributes(lp);
    }

    private long timeToMilliseconds (Timer timer) {
        return TimeUnit.MILLISECONDS.convert(timer.getHours(), TimeUnit.HOURS) +
                TimeUnit.MILLISECONDS.convert(timer.getMins(), TimeUnit.MINUTES) +
                TimeUnit.MILLISECONDS.convert(timer.getSecs(), TimeUnit.SECONDS);
    }

    private String addLeadingZero(int num) {
        if (num < 10)   return "0" + num;
        return "" + num;
    }

    public static int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }
}