package com.example.healify;

import android.content.ActivityNotFoundException;
import android.content.Intent;

import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tooltip.OnClickListener;
import com.tooltip.Tooltip;

import static android.view.Gravity.TOP;

public class signup extends AppCompatActivity {

    public TextInputLayout TextInputEmail;
    public TextInputLayout TextInputPassword;
    public TextInputLayout TextNumber;
    Button signup_btn;
    private FirebaseAuth firebaseAuth;
    TextView help;
    RelativeLayout relativeLayout;
    Tooltip tooltip;


    Double initial_Height = 0.0, initial_Weight = 0.0, initial_BMI = 0.0;
    String gender = "", profile_flag = "";
    Integer dietcount=0;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;


    private boolean Flag;

    String passwordinput, number, emailinput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        final LoadingDialog loadingDialog = new LoadingDialog(signup.this);


        TextInputEmail = findViewById(R.id.welcome_email_input_layout);
        TextInputPassword = findViewById(R.id.welcome_password_input_layout);
        TextNumber = findViewById(R.id.welcome_number_input_layout);
        signup_btn = findViewById(R.id.signupbutton);
        help = findViewById(R.id.needHelp);
        relativeLayout = findViewById(R.id.layoutrelmain);



help.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        tooltip = new Tooltip.Builder(help).setText(" Click here to E-mail us your problem ")
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
                   Toast.makeText(signup.this,"no activity",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                tooltip.dismiss();
            }
        };
        handler.postDelayed(runnable,3000);

    }
});




        firebaseAuth = FirebaseAuth.getInstance();


        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirm();
                if (Flag == true) {
                    loadingDialog.startLoadingDialog();
                    // Fire_email = TextInputEmail.getEditText().getText().toString().trim();
                    //Fire_password = TextInputPassword.getEditText().getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(emailinput, passwordinput)
                            .addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        //Success then to next pages of questions.
                                        Intent question_intent = new Intent(getApplicationContext(), Gender.class);
                                        startActivity(question_intent);
                                        finish();
                                        loadingDialog.dissmissDialog();
                                        Toast.makeText(signup.this, "Successfully Registered!!", Toast.LENGTH_SHORT).show();
                                        profile_flag = "false";

                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(number).build();

                                        FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates);

                                        String currentuser = firebaseAuth.getCurrentUser().getUid();

                                        firebaseDatabase = FirebaseDatabase.getInstance();
                                        reference = firebaseDatabase.getReference("users");

                                        Userhelper userhelper = new Userhelper(emailinput, number, passwordinput, initial_Height, initial_Weight, initial_BMI, gender, profile_flag,dietcount);

                                        reference.child(currentuser).setValue(userhelper);
                                    } else {
                                        profile_flag = "false";
                                        //TextInputEmail.setError(task.getException().getLocalizedMessage());
                                        loadingDialog.dissmissDialog();

                                        Toast.makeText(signup.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                                    }

                                    // ...
                                }
                            });



                } else {

                }
            }
        });

    }

    public boolean ValidateNumber() {
        number = TextNumber.getEditText().getText().toString().trim();

        if (number.isEmpty()) {
            TextNumber.setError("Field can't be empty");
            YoYo.with(Techniques.Pulse).duration(300).playOn(TextNumber);
            return false;
        }
        if(number.length()<3){
            TextNumber.setError("Please enter at least 3 characters");
            YoYo.with(Techniques.Pulse).duration(300).playOn(TextNumber);
            return false;
        }
        if (!Utils.USERNAME_PATTERN.matcher(number).matches()) {
            TextNumber.setError("Please enter valid Username with first letter capital and no numbers");
            YoYo.with(Techniques.Pulse).duration(300).playOn(TextNumber);
            return false;
        }
        else {
            TextNumber.setError(null);
            return true;
        }
    }


    public boolean ValidateEmail() {

        emailinput = TextInputEmail.getEditText().getText().toString().trim();

        if (emailinput.isEmpty()) {
            TextInputEmail.setError("Field can't be empty!");
            YoYo.with(Techniques.Pulse).duration(300).playOn(TextInputEmail);
            return false;
        }
        if (!Utils.MY_EMAIL.matcher(emailinput).matches()) {
            TextInputEmail.setError("Invalid email address");
            YoYo.with(Techniques.Pulse).duration(300).playOn(TextInputEmail);
            return false;
        } else {
            TextInputEmail.setError(null);
            return true;
        }
    }

    public boolean ValidatePassword() {
        passwordinput = TextInputPassword.getEditText().getText().toString().trim();
        if (passwordinput.isEmpty()) {
            TextInputPassword.setError("Field can't be empty!");
            YoYo.with(Techniques.Pulse).duration(300).playOn(TextInputPassword);
            return false;
        }
        if (passwordinput.length() <= 7) {
            TextInputPassword.setError("Password must have at-least 8 character");
            YoYo.with(Techniques.Pulse).duration(300).playOn(TextInputPassword);
            return false;
        }

        if (!Utils.PASSWORD_LOWER_PATTERN.matcher(passwordinput).matches()) {
            TextInputPassword.setError("Password is weak. Lowercase character required!");
            YoYo.with(Techniques.Pulse).duration(300).playOn(TextInputPassword);
            return false;
        }
        if (!Utils.PASSWORD_UPPERCASE_PATTERN.matcher(passwordinput).matches()) {
            TextInputPassword.setError("Password is weak. Uppercase character required!");
            YoYo.with(Techniques.Pulse).duration(300).playOn(TextInputPassword);
            return false;
        }
        if (!Utils.PASSWORD_NUMBER_PATTERN.matcher(passwordinput).matches()) {
            TextInputPassword.setError("Password is weak. Minimum 1 number is required!");
            YoYo.with(Techniques.Pulse).duration(300).playOn(TextInputPassword);
            return false;
        }
        if (!Utils.PASSWORD_SPECIAL_CHARACTER_PATTERN.matcher(passwordinput).matches()) {
            TextInputPassword.setError("Password is weak. Minimum 1 special character required!");
            YoYo.with(Techniques.Pulse).duration(300).playOn(TextInputPassword);
            return false;
        } else {
            TextInputPassword.setError(null);
            return true;
        }
    }


    public boolean confirm() {
        if (!ValidateEmail() | !ValidatePassword() | !ValidateNumber()) {
            //Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            Flag = false;
            return true;

        } else {

            //  String input = "Email: " + TextInputEmail.getEditText().getText().toString();
            //  input+="\n";
            //  input += "Password: " + TextInputPassword.getEditText().getText().toString();
            //  input+="\n";
            //  input += "Number: " + TextNumber.getEditText().getText().toString();

            // Toast.makeText(this, input, Toast.LENGTH_SHORT).show();

            Flag = true;

            return false;
        }


    }
}