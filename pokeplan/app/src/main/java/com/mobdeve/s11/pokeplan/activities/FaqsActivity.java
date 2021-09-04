package com.mobdeve.s11.pokeplan.activities;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s11.pokeplan.R;
import com.mobdeve.s11.pokeplan.adapters.FaqsAdapter;
import com.mobdeve.s11.pokeplan.data.FaqsDataHelper;

import java.util.ArrayList;

public class FaqsActivity extends AppCompatActivity {
    private ImageButton ibBack;

    private RecyclerView rvFaqsList;
    private RecyclerView.LayoutManager faqsListManager;
    private FaqsAdapter faqsAdapter;
    private ArrayList <String> questionList;
    private ArrayList <String> answerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);


        ibBack = findViewById(R.id.ib_faqs_back);
        this.setButtonListeners();

        this.initRecyclerView();
    }

    private void initRecyclerView () {
        this.rvFaqsList = findViewById(R.id.rv_faqs);

        this.faqsListManager  = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        this.rvFaqsList.setLayoutManager(this.faqsListManager);

        FaqsDataHelper faqsDataHelper = new FaqsDataHelper();

        this.questionList = faqsDataHelper.getQuestions();
        this.answerList = faqsDataHelper.getAnswers();

        this.faqsAdapter = new FaqsAdapter(this.questionList, this.answerList);
        this.rvFaqsList.setAdapter(this.faqsAdapter);
    }

    private void setButtonListeners() {
        ibBack.setOnClickListener(view -> onBackPressed());
    }
}