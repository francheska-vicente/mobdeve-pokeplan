package com.mobdeve.s11.pokeplan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FocusTimerStartActivity extends AppCompatActivity {
    private ImageButton ibhoursup;
    private ImageButton ibhoursdown;
    private ImageButton ibminsup;
    private ImageButton ibminsdown;
    private ImageButton ibsecsup;
    private ImageButton ibsecsdown;
    private TextView tvhours;
    private TextView tvmins;
    private TextView tvsecs;

    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focustimer_start);

        initTimer();
    }

    private void initTimer() {
        timer = new Timer();

        tvhours = findViewById(R.id.tv_focustimer_s_hours);
        tvmins = findViewById(R.id.tv_focustimer_s_mins);
        tvsecs = findViewById(R.id.tv_focustimer_s_secs);

        ibhoursup = findViewById(R.id.ib_timer_hours_up);
        ibhoursup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (timer.getHours() < 99) {
                    timer.setHours(timer.getHours()+1);
                    tvhours.setText(addLeadingZero(timer.getHours()));
                }
            }
        });

        ibhoursdown = findViewById(R.id.ib_timer_hours_down);
        ibhoursdown.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (timer.getHours() > 0) {
                    timer.setHours(timer.getHours()-1);
                    tvhours.setText(addLeadingZero(timer.getHours()));
                }
            }
        });

        ibminsup = findViewById(R.id.ib_timer_mins_up);
        ibminsup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (timer.getMins() < 59) {
                    timer.setMins(timer.getMins()+1);
                    tvmins.setText(addLeadingZero(timer.getMins()));
                }
            }
        });

        ibminsdown = findViewById(R.id.ib_timer_mins_down);
        ibminsdown.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (timer.getMins() > 0) {
                    timer.setMins(timer.getMins()-1);
                    tvmins.setText(addLeadingZero(timer.getMins()));
                }
            }
        });

        ibsecsup = findViewById(R.id.ib_timer_secs_up);
        ibsecsup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (timer.getSecs() < 59) {
                    timer.setSecs(timer.getSecs()+1);
                    tvsecs.setText(addLeadingZero(timer.getSecs()));
                }
            }
        });

        ibsecsdown = findViewById(R.id.ib_timer_secs_down);
        ibsecsdown.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (timer.getSecs() > 0) {
                    timer.setSecs(timer.getSecs()-1);
                    tvsecs.setText(addLeadingZero(timer.getSecs()));
                }
            }
        });
    }

    private String addLeadingZero(int num) {
        if (num < 10)   return "0" + num;
        return "" + num;
    }
}