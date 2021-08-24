package com.mobdeve.s11.pokeplan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class FaqsActivity extends AppCompatActivity {

    private RecyclerView rvFaqsList;
    private RecyclerView.LayoutManager faqsListManager;
    private FaqsAdapter faqsAdapter;
    private ArrayList <String> questionList;
    private ArrayList <String> answerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);

        this.initRecyclerView ();
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
}