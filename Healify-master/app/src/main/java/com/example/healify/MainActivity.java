package com.example.healify;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tooltip.OnClickListener;
import com.tooltip.Tooltip;

import static android.view.Gravity.TOP;

public class MainActivity extends AppCompatActivity {

    public TextInputLayout TextInputEmail_login;
    public TextInputLayout TextInputPassword_login;
    RelativeLayout relay1,relay2,relaymain;
    LinearLayout linearLayout;
    private ImageView logo;
    Button login_btn;
    private boolean Fire_Flag;
    TextView needhelp;
    Tooltip tooltip;
    TextView textView;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;



    String login_email, login_password;






    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            relay1.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final LoadingDialog loadingDialog = new LoadingDialog(MainActivity.this);
        firebaseAuth = FirebaseAuth.getInstance();

        TextInputEmail_login = findViewById(R.id.text_email);
        TextInputPassword_login =  findViewById(R.id.text_password);

        login_btn = findViewById(R.id.loginbutton);
        textView = findViewById(R.id.not_a_member);

        String text = "Not a member? Sign-up here!";

        SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {

                open_signup();
                finish();

            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.RED);
            }
        };

        ss.setSpan(clickableSpan1,14,21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());


        logo =(ImageView) findViewById(R.id.logo);
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.fade);
        logo.startAnimation(myanim);

        relay1 = (RelativeLayout) findViewById(R.id.layoutrelsecond);
        linearLayout = (LinearLayout) findViewById(R.id.linearlayout2);

        handler.postDelayed(runnable,2000);




        if(firebaseAuth.getCurrentUser() != null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseUser=firebaseAuth.getCurrentUser();
            databaseReference = firebaseDatabase.getReference("users").child(firebaseUser.getUid()).child("profile");
            databaseReference.setValue("Online");
                            Intent Home_intent = new Intent(getApplicationContext(), Home.class);
                            startActivity(Home_intent);
                            finish();
        }

        needhelp=findViewById(R.id.help_needed);
        needhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tooltip = new Tooltip.Builder(needhelp).setText(" Click here to E-mail us your problem ")
                        .setTextColor(Color.WHITE)
                        .setBackgroundColor(getResources().getColor(R.color.pink))
                        .setCornerRadius(8f)
                        .setGravity(TOP)
                        .setDismissOnClick(false)
                        .setCancelable(true)
                        .build();
                tooltip.show();

                tooltip.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(@NonNull Tooltip tooltip) {
                        try{
                            Intent intent = new Intent (Intent.ACTION_SEND);
                            String[] recipients={"armaandev1470@gmail.com"};
                            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                            intent.putExtra(Intent.EXTRA_SUBJECT,"Healify_Need Help");
                            intent.putExtra(Intent.EXTRA_TEXT,"Healify");
                            intent.setType("text/html");
                            intent.setPackage("com.google.android.gm");
                            startActivity(Intent.createChooser(intent, "Send mail"));
                        }catch(ActivityNotFoundException e){
                            Toast.makeText(MainActivity.this,"no activity",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Handler handler1 = new Handler();
                Runnable runnable1 = new Runnable() {
                    @Override
                    public void run() {
                        tooltip.dismiss();
                    }
                };
                handler1.postDelayed(runnable1,3000);

            }
        });



        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               confirmCheck();

               if(Fire_Flag==true){
                   loadingDialog.startLoadingDialog();
                  //String Fire_login_email = TextInputEmail_login.getEditText().getText().toString().trim();
                   //String Fire_login_password = TextInputPassword_login.getEditText().getText().toString().trim();

                   firebaseAuth.signInWithEmailAndPassword(login_email, login_password)
                           .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                               @Override
                               public void onComplete(@NonNull Task<AuthResult> task) {
                                   if (task.isSuccessful()) {
                                       loadingDialog.dissmissDialog();

                                          // Sign in success, update UI with the signed-in user's information
                                          Intent Home_intent = new Intent(getApplicationContext(), Home.class);
                                          startActivity(Home_intent);
                                          finish();
                                   }

                                   else {
                                       loadingDialog.dissmissDialog();
                                       TextInputEmail_login.setError(task.getException().getLocalizedMessage());
                                       TextInputPassword_login.setError("Wrong password");
                                       // If sign in fails, display a message to the user.
                                        Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                   }

                                   // ...
                               }
                           });


               }

            }
        });




    }

    public boolean loginEmailCheck(){

         login_email = TextInputEmail_login.getEditText().getText().toString().trim();

        if(login_email.isEmpty()){
            TextInputEmail_login.setError("Field can't be Empty");
            return false;
        }
        else{
            TextInputEmail_login.setError(null);
            return true;

        }
    }

    public boolean loginPasswordCheck(){

         login_password = TextInputPassword_login.getEditText().getText().toString().trim();

        if(login_password.isEmpty()){
            TextInputPassword_login.setError("Field can't be Empty");
            return false;
        }
        else{
            TextInputPassword_login.setError(null);
            return true;
        }
    }

    public boolean confirmCheck(){

        if(!loginEmailCheck() | !loginPasswordCheck()){
            Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
            Fire_Flag = false;
            return true;

        }
        else{

            Fire_Flag = true;
            return false;
        }

    }

    public void open_signup () {
        Intent intent = new Intent(this, signup.class);
        startActivity(intent);
    }

}
