package com.mobdeve.s11.pokeplan.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s11.pokeplan.R;

import org.jetbrains.annotations.NotNull;

public class FaqsViewHolder extends RecyclerView.ViewHolder {

    private TextView tvQuestion;
    private TextView tvAnswer;

    public FaqsViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        this.tvQuestion = itemView.findViewById(R.id.tv_faqs_template_question);
        this.tvAnswer = itemView.findViewById(R.id.tv_faqs_template_answer);
    }

    public void setTvQuestion (String question) {
        this.tvQuestion.setText(question);
    }

    public void setTvAnswer (String answer) {
        this.tvAnswer.setText(answer);
    }
}
