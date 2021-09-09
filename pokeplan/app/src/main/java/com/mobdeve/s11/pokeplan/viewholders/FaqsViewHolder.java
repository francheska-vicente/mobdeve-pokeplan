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

    /**
     * Class constructor
     * @param itemView the layout of a specific item
     */
    public FaqsViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        this.tvQuestion = itemView.findViewById(R.id.tv_faqs_template_question);
        this.tvAnswer = itemView.findViewById(R.id.tv_faqs_template_answer);
    }

    /**
     * Sets the question in the TextView
     * @param question the question to be set
     */
    public void setTvQuestion (String question) {
        this.tvQuestion.setText(question);
    }

    /**
     * Sets the answer in the TextView
     * @param answer the answer to be set
     */
    public void setTvAnswer (String answer) {
        this.tvAnswer.setText(answer);
    }
}
