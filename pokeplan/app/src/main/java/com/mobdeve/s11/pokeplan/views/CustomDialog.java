package com.mobdeve.s11.pokeplan.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mobdeve.s11.pokeplan.R;

public class CustomDialog extends Dialog {
    public static final int OK = 0;
    public static final int ERROR = 1;
    public static final int CONFIRM = 2;
    public static final int ONE_INPUT = 3;
    public static final int TWO_INPUT = 4;
    public static final int FOUR_INPUT = 5;

    private Context context;

    public CustomDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int width = (int)(context.getResources().getDisplayMetrics().widthPixels*0.90);
        this.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void setDialogType(int type) {
        switch(type) {
            case OK: this.setContentView(R.layout.dialog_ok); break;
            case ERROR: this.setContentView(R.layout.dialog_error); break;
            case CONFIRM: this.setContentView(R.layout.dialog_confirm); break;
            case ONE_INPUT: this.setContentView(R.layout.dialog_oneinput); break;
            case TWO_INPUT: this.setContentView(R.layout.dialog_twoinputs); break;
            case FOUR_INPUT: this.setContentView(R.layout.dialog_fourinputs); break;
        }
    }

    public void setOKComponents(String title, String body, int icon) {
        TextView tvdialogtitle = this.findViewById(R.id.tv_dialog_ok_title);
        tvdialogtitle.setText(title);

        TextView tvdialogtext = this.findViewById(R.id.tv_dialog_ok_text);
        tvdialogtext.setText(body);

        ImageView ivdialogicon = this.findViewById(R.id.iv_dialog_ok_icon);
        ivdialogicon.setImageResource(icon);

        Button btndialogok = this.findViewById(R.id.btn_dialog_ok);
        btndialogok.setOnClickListener(v -> this.dismiss());
    }

    public void setErrorComponents(String title, String body) {
        TextView tvdialogtitle = this.findViewById(R.id.tv_dialog_error_title);
        tvdialogtitle.setText(title);
        TextView tvdialogtext = this.findViewById(R.id.tv_dialog_error_text);
        tvdialogtext.setText(body);

        Button btndialogerror = this.findViewById(R.id.btn_dialog_error);
        btndialogerror.setOnClickListener(v -> this.dismiss());
    }

    public void setConfirmComponents(String title, String body, int icon,
                                     String confirm) {
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);

        TextView tvdialogtitle = this.findViewById(R.id.tv_dialog_confirm_title);
        tvdialogtitle.setText(title);

        TextView tvdialogtext = this.findViewById(R.id.tv_dialog_confirm_text);
        tvdialogtext.setText(body);

        ImageView ivdialogicon = this.findViewById(R.id.iv_dialog_confirm_icon);
        ivdialogicon.setImageResource(icon);

        Button btndialogcancel = this.findViewById(R.id.btn_dialog_confirm_cancel);
        btndialogcancel.setOnClickListener(v -> this.dismiss());

        Button btndialogconfirm = this.findViewById(R.id.btn_dialog_confirm);
        btndialogconfirm.setText(confirm);
    }

    public void setOneInputComponents(String title, int icon) {
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);

        TextView tvdialogtitle = this.findViewById(R.id.iv_dialog_oneinput_title);
        tvdialogtitle.setText(title);

        ImageView ivdialogicon = this.findViewById(R.id.iv_oneinput_icon);
        ivdialogicon.setImageResource(icon);

        Button btndialogcancel = this.findViewById(R.id.btn_dialog_oneinput_confirm);
        btndialogcancel.setOnClickListener(v -> this.dismiss());
    }

    public void setTwoInputComponents(String title, String input1Title,
                                      String input2Title, String confirmButtonText) {
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);

        TextView tvdialogtitle = this.findViewById(R.id.tv_dialog_confirm_title);
        tvdialogtitle.setText(title);

        TextView tvdialoginput1title = this.findViewById(R.id.tv_dialog_twoinputs_one_title);
        tvdialoginput1title.setText(input1Title);

        TextView tvdialoginput2title = this.findViewById(R.id.tv_dialog_twoinputs_two_title);
        tvdialoginput2title.setText(input2Title);

        Button btnDialogConfirm = this.findViewById(R.id.btn_dialog_twoinputs_confirm);
        btnDialogConfirm.setText(confirmButtonText);

        Button btndialogcancel = this.findViewById(R.id.btn_dialog_twoinputs_cancel);
        btndialogcancel.setOnClickListener(v -> this.dismiss());
    }

    public void setFourInputComponents(String title,
                                       String input1Title,
                                       String input2Title,
                                       String input3Title,
                                       String input4Title,
                                       String confirmButtonText) {
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);

        TextView tvdialogtitle = this.findViewById(R.id.tv_dialog_title);
        tvdialogtitle.setText(title);

        TextView tvdialoginput1title = this.findViewById(R.id.tv_dialog_fourinput_one_title);
        tvdialoginput1title.setText(input1Title);

        TextView tvdialoginput2title = this.findViewById(R.id.tv_dialog_fourinput_two_title);
        tvdialoginput2title.setText(input2Title);

        TextView tvdialoginput3title = this.findViewById(R.id.tv_dialog_fourinput_three_title);
        tvdialoginput3title.setText(input3Title);

        TextView tvdialoginput4title = this.findViewById(R.id.tv_dialog_fourinput_four_title);
        tvdialoginput4title.setText(input4Title);

        Button btnDialogConfirm = this.findViewById(R.id.btn_dialog_fourinput_confirm);
        btnDialogConfirm.setText(confirmButtonText);

        Button btndialogcancel = this.findViewById(R.id.btn_dialog_fourinput_cancel);
        btndialogcancel.setOnClickListener(v -> this.dismiss());
    }
}
