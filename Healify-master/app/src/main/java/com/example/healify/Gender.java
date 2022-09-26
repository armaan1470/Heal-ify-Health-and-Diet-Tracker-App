package com.example.healify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Gender extends AppCompatActivity {
    Button next_gender;
    ImageButton male_btn,female_btn;
    String gender_str,string_users;
    boolean gender_flag;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);

        gender_flag =false;

        next_gender = findViewById(R.id.next_gender_button);
        male_btn = findViewById(R.id.male_button);
        female_btn = findViewById(R.id.female_button);


        male_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender_str = "Male";
                Toast.makeText(Gender.this,gender_str,Toast.LENGTH_SHORT).show();
                gender_flag = true;
                female_btn.setAlpha((float) 0.50);
                male_btn.setAlpha((float)1.00);
            }
        });

        female_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender_str = "Female";
                Toast.makeText(Gender.this,gender_str,Toast.LENGTH_SHORT).show();
                gender_flag = true;
                female_btn.setAlpha((float) 1.00);
                male_btn.setAlpha((float)0.5);

            }
        });




        next_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(gender_flag == true) {
                   firebaseAuth = FirebaseAuth.getInstance();
                   firebaseDatabase = FirebaseDatabase.getInstance();
                   reference = firebaseDatabase.getReference("users");

                   string_users = firebaseAuth.getCurrentUser().getUid();

                   reference.child(string_users).child("gender").setValue(gender_str);
                   Intent gender_intent = new Intent(getApplicationContext(), Bmiheightweight.class);
                   startActivity(gender_intent);

               }
               else{
                   Toast.makeText(Gender.this,"Please select your gender!",Toast.LENGTH_SHORT).show();
               }

                }
        });


    }
}
