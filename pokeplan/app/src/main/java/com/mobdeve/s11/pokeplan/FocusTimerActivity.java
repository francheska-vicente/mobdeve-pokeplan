package com.mobdeve.s11.pokeplan;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

public class FocusTimerActivity extends AppCompatActivity {
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

    private Dialog infodialog;
    private Dialog timererrordialog;
    private Dialog confirmstoptimerdialog;
    private Dialog hatcheggdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focustimer);

        initView();
        initTimer();
    }

    private void initView() {
        tvhours = findViewById(R.id.tv_focustimer_hours);
        tvmins = findViewById(R.id.tv_focustimer_mins);
        tvsecs = findViewById(R.id.tv_focustimer_secs);
        tvcaption = findViewById(R.id.tv_focustimer_tagline);

        ibinfo = findViewById(R.id.ib_focustimer_info);
        ibhoursup = findViewById(R.id.ib_timer_hours_up);
        ibhoursdown = findViewById(R.id.ib_timer_hours_down);
        ibminsup = findViewById(R.id.ib_timer_mins_up);
        ibminsdown = findViewById(R.id.ib_timer_mins_down);
        ibsecsup = findViewById(R.id.ib_timer_secs_up);
        ibsecsdown = findViewById(R.id.ib_timer_secs_down);

        btnfocustimer = findViewById(R.id.btn_focustimer_main);
    }

    private void initTimer() {
        timer = new Timer();

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

        btnfocustimer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (timeToMilliseconds(timer) >= (1000 * 60 * 5))
                    startTimer();
                else
                    createTimeErrorDialog(view);
            }
        });

        ibinfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // add info dialog here
            }
        });
    }

    private void startTimer() {
        // hide timer buttons
        ibhoursup.setVisibility(View.GONE);
        ibhoursdown.setVisibility(View.GONE);
        ibminsup.setVisibility(View.GONE);
        ibminsdown.setVisibility(View.GONE);
        ibsecsup.setVisibility(View.GONE);
        ibsecsdown.setVisibility(View.GONE);
        ibinfo.setVisibility(View.GONE);

        // change caption
        tvcaption.setText(getString(R.string.focustimer_ongoing_tagline));

        // change button from start timer to stop timer
        btnfocustimer.setText(getString(R.string.focustimer_stop_button));
        btnfocustimer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                createConfirmStopTimerDialog(view);
            }
        });

        // start countdown timer
        long time = timeToMilliseconds(timer);
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
        // confirmstoptimerdialog.dismiss();

        Egg egg = new Egg(timer);
        egg.generatePokemon();

        btnfocustimer.setText(R.string.focustimer_finish_button);
        btnfocustimer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                resetTimer();
            }
        });

        createHatchEggDialog(this.findViewById(android.R.id.content));
    }

    private void resetTimer() {
        timer = new Timer();
        ibhoursup.setVisibility(View.VISIBLE);
        ibhoursdown.setVisibility(View.VISIBLE);
        ibminsup.setVisibility(View.VISIBLE);
        ibminsdown.setVisibility(View.VISIBLE);
        ibsecsup.setVisibility(View.VISIBLE);
        ibsecsdown.setVisibility(View.VISIBLE);
        ibinfo.setVisibility(View.VISIBLE);

        tvcaption.setText(R.string.focustimer_start_tagline);
        btnfocustimer.setText(R.string.focustimer_start_button);
        btnfocustimer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startTimer();
            }
        });

        tvhours.setText("00");
        tvmins.setText("10");
        tvsecs.setText("00");
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
                stopTimer();
                confirmstoptimerdialog.dismiss();
            }
        });
        confirmstoptimerdialog.show();
    }

    private void createHatchEggDialog(View view) {
        hatcheggdialog = new Dialog(view.getContext());
        hatcheggdialog.setContentView(R.layout.dialog_ok);
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.40);

        hatcheggdialog.getWindow().setLayout(width, height);
        hatcheggdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tvdialogtitle = (TextView) hatcheggdialog.findViewById(R.id.tv_dialog_ok_title);
        tvdialogtitle.setText(R.string.focustimer_dialog_confirm_title);
        TextView tvdialogtext = (TextView) hatcheggdialog.findViewById(R.id.tv_dialog_ok_text);
        tvdialogtext.setText(R.string.focustimer_dialog_confirm_text);
        ImageView ivdialogicon = (ImageView) hatcheggdialog.findViewById(R.id.iv_dialog_ok_icon);
        ivdialogicon.setImageResource(R.drawable.sunny_side_up);

        Button btndialogok = (Button) hatcheggdialog.findViewById(R.id.btn_dialog_ok);
        btndialogok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private long timeToMilliseconds (Timer timer) {
        return TimeUnit.MILLISECONDS.convert(timer.getHours(), TimeUnit.HOURS) +
                TimeUnit.MILLISECONDS.convert(timer.getMins(), TimeUnit.MINUTES) +
                TimeUnit.MILLISECONDS.convert(timer.getSecs(), TimeUnit.SECONDS);
    }

    private String addLeadingZero(int num) {
        if (num < 10)   return "0" + num;
        return "" + num;
    }


}