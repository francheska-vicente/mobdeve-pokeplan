package com.mobdeve.s11.pokeplan;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
                startTimer();
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
                /* TODO: add confirm dialog here */
                stopTimer();
            }
        });

        long time = TimeUnit.MILLISECONDS.convert(timer.getHours(), TimeUnit.HOURS) +
                        TimeUnit.MILLISECONDS.convert(timer.getMins(), TimeUnit.MINUTES) +
                        TimeUnit.MILLISECONDS.convert(timer.getSecs(), TimeUnit.SECONDS);

        CountDownTimer countdown = new CountDownTimer(time, 1000) {

            public void onTick(long fin) {
                int hours = (int) TimeUnit.MILLISECONDS.toHours(fin);
                int mins = (int) (TimeUnit.MILLISECONDS.toMinutes(fin) -
                                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(fin)));
                int secs = (int) (TimeUnit.MILLISECONDS.toSeconds(fin) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(fin)));

                tvhours.setText(addLeadingZero(hours));
                tvmins.setText(addLeadingZero(mins));
                tvsecs.setText(addLeadingZero(secs));
            }

            public void onFinish() {
                finishTimer();
            }
        }.start();
    }

    private void stopTimer() {

    }

    private void finishTimer() {
        Egg egg = new Egg(timer);
        egg.generatePokemon();

        btnfocustimer.setText(getString(R.string.focustimer_start_button));
        btnfocustimer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startTimer();
            }
        });

        /* TODO: display egg hatch */
    }

    private void resetTimer() {
        ibhoursup.setVisibility(View.VISIBLE);
        ibhoursdown.setVisibility(View.VISIBLE);
        ibminsup.setVisibility(View.VISIBLE);
        ibminsdown.setVisibility(View.VISIBLE);
        ibsecsup.setVisibility(View.VISIBLE);
        ibsecsdown.setVisibility(View.VISIBLE);
        ibinfo.setVisibility(View.VISIBLE);
        tvcaption.setText(getString(R.string.focustimer_start_tagline));
        btnfocustimer.setText(getString(R.string.focustimer_start_button));
        btnfocustimer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startTimer();
            }
        });
    }

    private String addLeadingZero(int num) {
        if (num < 10)   return "0" + num;
        return "" + num;
    }


}