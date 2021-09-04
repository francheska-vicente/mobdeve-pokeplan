package com.mobdeve.s11.pokeplan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s11.pokeplan.viewholders.FaqsViewHolder;
import com.mobdeve.s11.pokeplan.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FaqsAdapter extends RecyclerView.Adapter<FaqsViewHolder> {

    private ArrayList<String> questionList;
    private ArrayList<String> answerList;

    public FaqsAdapter (ArrayList<String> question, ArrayList<String> answer) {
        this.questionList = question;
        this.answerList = answer;
    }

    @NonNull
    @NotNull
    @Override
    public FaqsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from (parent.getContext());
        View view = inflater.inflate (R.layout.template_faqs, parent, false);

        FaqsViewHolder faqsViewHolder = new FaqsViewHolder (view);

        return faqsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FaqsViewHolder holder, int position) {
        holder.setTvAnswer(this.answerList.get(position));
        holder.setTvQuestion(this.questionList.get(position));
    }

    @Override
    public int getItemCount() {
        return this.answerList.size();
    }
}
