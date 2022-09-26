package com.example.healify;

import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.DecimalFormat;

import static com.example.healify.Bmiheightweight.USER_BMI;

public class Graph extends AppCompatActivity {
    Double GET_BMI_Double;
    Float GET_BMI_FLOAT;
    String final_bmi, string_users_bmi_graph, indicator, bmi_string;


    TextView graph_text, bmi_status_text, bmi_number;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    Button dashboard_btn;
    Integer GET_BMI_INT;
    ImageView gif;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }

        Intent intent = getIntent();
        String GET_Bmi_graph = intent.getStringExtra(USER_BMI);

        GET_BMI_Double = Double.parseDouble(GET_Bmi_graph);

        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        final_bmi = df.format(GET_BMI_Double);

        GET_BMI_FLOAT = Float.parseFloat(final_bmi);
        GET_BMI_INT = Math.round(GET_BMI_FLOAT);


        // bmi_number  =findViewById(R.id.bmi_num);
        graph_text = findViewById(R.id.Graph_text);
        bmi_status_text = findViewById(R.id.bmi_status);
        dashboard_btn = findViewById(R.id.dashboardbutton);
        gif = findViewById(R.id.giffy);

        if (Float.compare(GET_BMI_FLOAT, 15f) <= 0) {
            bmi_status_text.setText("Your BMI Status : Very severely underweight!!");
            indicator = "Underweight";
            Glide.with(this).load(R.drawable.lowerweight).into(gif);

        } else if (Float.compare(GET_BMI_FLOAT, 15f) > 0 && Float.compare(GET_BMI_FLOAT, 16f) <= 0) {
            bmi_status_text.setText("Your BMI Status : Severely underweight!!");
            indicator = "Underweight";
            Glide.with(this).load(R.drawable.lowerweight).into(gif);
        } else if (Float.compare(GET_BMI_FLOAT, 16f) > 0 && Float.compare(GET_BMI_FLOAT, 18.5f) <= 0) {
            bmi_status_text.setText("Your BMI Status : Underweight");
            indicator = "Underweight";
            Glide.with(this).load(R.drawable.lowerweight).into(gif);
        } else if (Float.compare(GET_BMI_FLOAT, 18.5f) > 0 && Float.compare(GET_BMI_FLOAT, 25f) <= 0) {
            bmi_status_text.setText("Your BMI Status : Healthy Weight");
            indicator = "Healthy Weight";
            Glide.with(this).load(R.drawable.healthy).into(gif);
        } else if (Float.compare(GET_BMI_FLOAT, 25f) > 0 && Float.compare(GET_BMI_FLOAT, 30f) <= 0) {
            indicator = "Overweight";
            bmi_status_text.setText("Your BMI Status : Slightly Overweight");
            Glide.with(this).load(R.drawable.overweight).into(gif);
        } else if (Float.compare(GET_BMI_FLOAT, 30f) > 0 && Float.compare(GET_BMI_FLOAT, 35f) <= 0) {
            indicator = "Overweight Class-I";
            bmi_status_text.setText("Your BMI Status : Obese Class-I");
            Glide.with(this).load(R.drawable.overweight).into(gif);
        } else if (Float.compare(GET_BMI_FLOAT, 35f) > 0 && Float.compare(GET_BMI_FLOAT, 40f) <= 0) {
            indicator = "Overweight Class-II";
            bmi_status_text.setText("Your BMI Status : Obese Class-II");
            Glide.with(this).load(R.drawable.overweight).into(gif);
        } else {
            indicator = "Overweight Class-III";
            bmi_status_text.setText("Obese Class-III");
            Glide.with(this).load(R.drawable.overweight).into(gif);
        }


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("users");


        string_users_bmi_graph = firebaseAuth.getCurrentUser().getUid();
        reference.child(string_users_bmi_graph).child("bmi").setValue(final_bmi);


        reference = firebaseDatabase.getReference("users").child(string_users_bmi_graph);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.child("profile").getValue().toString();

                // Toast.makeText(Graph.this,value,Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // Croller croller = (Croller) findViewById(R.id.croller);
        // croller.setIndicatorWidth(15);
        // croller.setMin(0);
        // croller.setMax(50);
        // croller.setProgress(GET_BMI_INT);
        // croller.setLabel(indicator+" " + GET_BMI_FLOAT + " kg/m²");
        // YoYo.with(Techniques.FadeIn).duration(1000).playOn(croller);
        graph_text.setText("BMI is: " + GET_BMI_FLOAT + " kg/m²");
        // bmi_number.setText(""+GET_BMI_FLOAT);
        //  YoYo.with(Techniques.FadeIn).duration(1000).playOn(bmi_number);


        dashboard_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dashboard = new Intent(Graph.this, Home.class);
                startActivity(dashboard);
                finishAffinity();

            }
        });


    }

}
