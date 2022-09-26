package com.example.healify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Question extends AppCompatActivity {

    TextView signuped_user;
    Button signout_user_btn,profile_btn;


    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        signout_user_btn = findViewById(R.id.signoutUser);
        signuped_user = findViewById(R.id.Signupuseremail);
        profile_btn = findViewById(R.id.complete_my_profile_button);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        signuped_user.setText(firebaseUser.getEmail());


        signout_user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                startActivity(new Intent(Question.this,MainActivity.class));
                finishAffinity();
            }
        });

        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Question.this,Gender.class));
                finishAffinity();

            }
        });





    }
}
