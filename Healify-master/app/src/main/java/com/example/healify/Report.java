package com.example.healify;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Report {
    private Activity activity;
    private AlertDialog dialog;
    Button reportit,back;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,reference;
    ProgressBar reportLoading;
    EditText report_user;
    TextInputLayout myreport;
    String report_user_string,reportname;


    public Report(Activity myActivity) {
        activity = myActivity;
    }

   public void sendReport(){

       AlertDialog.Builder builder = new AlertDialog.Builder(activity);
       LayoutInflater inflater = activity.getLayoutInflater();
       builder.setView(inflater.inflate(R.layout.activity_report, null));
       builder.setCancelable(true);
       dialog = builder.create();
       dialog.show();
       back =dialog.findViewById(R.id.goBack);
       reportit = dialog.findViewById(R.id.reportUs);
       myreport = dialog.findViewById(R.id.MyReport);
       report_user = dialog.findViewById(R.id.problem);
       reportLoading= dialog.findViewById(R.id.report_loading);

       firebaseAuth = FirebaseAuth.getInstance();
       firebaseDatabase = FirebaseDatabase.getInstance();
       firebaseUser = firebaseAuth.getCurrentUser();

       firebaseDatabase = FirebaseDatabase.getInstance();
       databaseReference = firebaseDatabase.getReference("users").child(firebaseUser.getUid()).child("profile");
       databaseReference.setValue("Reporting");

       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog.dismiss();
           }
       });

       reportit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              reportLoading.setIndeterminate(true);
               reportname = myreport.getEditText().getText().toString().toLowerCase();
               report_user_string = report_user.getText().toString();

               if (!report_user_string.isEmpty() && !reportname.isEmpty()){

                   databaseReference = firebaseDatabase.getReference("reports").child(firebaseUser.getUid()).child(reportname);
                    databaseReference.setValue(report_user_string).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                          UpdateDoneFeedback updateDoneFeedback = new UpdateDoneFeedback(activity);
                          updateDoneFeedback.update_animate("Report regarding "+reportname+" is being issued!"+"\n Thank you for reporting");
                            dialog.dismiss();
                        }
                    });
               }else{
                   reportLoading.setIndeterminate(false);
                   if(reportname.isEmpty()){
                       Toast.makeText(activity,"Invalid Name of diet",Toast.LENGTH_SHORT).show();
                   }else{
                       Toast.makeText(activity,"Please enter your report",Toast.LENGTH_SHORT).show();
                   }
               }


           }
       });



    }

}
