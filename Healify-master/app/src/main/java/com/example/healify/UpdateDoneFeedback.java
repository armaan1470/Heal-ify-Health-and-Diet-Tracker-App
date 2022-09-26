package com.example.healify;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class UpdateDoneFeedback {
    private Activity activity;
    private AlertDialog views;
    TextView update;
    CardView close;


    public UpdateDoneFeedback(Activity myActivity) {
        activity = myActivity;
    }

    public void update_animate(String Update) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.feedback_dialogue, null));
        builder.setCancelable(true);
        views = builder.create();
        views.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        views.show();
        update = views.findViewById(R.id.update_text);
        close = views.findViewById(R.id.close_btn);
        update.setText(Update);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                views.dismiss();
            }
        });
    }
}