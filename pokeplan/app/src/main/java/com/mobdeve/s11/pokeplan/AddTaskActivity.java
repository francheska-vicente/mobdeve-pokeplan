package com.mobdeve.s11.pokeplan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        this.initComponents();
    }

    private void initComponents () {
        this.initType ();
        this.initDeadline ();
        this.initTimeDue ();
    }

    private void initType () {
        Spinner spinner = (Spinner) findViewById(R.id.sp_add_task_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.add_task_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.template_spinner);
        spinner.setAdapter(adapter);
    }

    private void initDeadline () {
        EditText edittext= (EditText) findViewById(R.id.et_add_task_due_date);
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month,
                                  int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                String myFormat = "dd.MM.yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ROOT);

                edittext.setText(sdf.format(calendar.getTime()));
            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddTaskActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void initTimeDue () {

    }
}