package com.example.healify;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class Profile extends AppCompatActivity {

    TextView userdisplay, genderprofile, Dcount;
    CardView Diets, Workouts;
    TextInputLayout email, username, height_profile, weight_profile;
    Button updateinfo, logoutuser;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference, databaseReference;
    FirebaseUser firebaseUser;
    String genderValue, heightValue, weightValue, Username_string, heighttext, weighttext, bmi_string;
    ImageView gender_img_male, gender_img_female;
    Float heightfloat, weightfloat, max_height, max_weight, min_height, min_weight, bmi;
    public static String email_input = "";
    Float bmi_check = 0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.pink));
        }

        userdisplay = findViewById(R.id.profile_welcome_user);
        genderprofile = findViewById(R.id.gender_profile);
        username = findViewById(R.id.username_update);
        email = findViewById(R.id.mail_update);
        genderprofile = findViewById(R.id.gender_profile);
        logoutuser = findViewById(R.id.logout_user);
        updateinfo = findViewById(R.id.update_info);
        gender_img_male = findViewById(R.id.gender_image_male);
        gender_img_female = findViewById(R.id.gender_image_female);
        height_profile = findViewById(R.id.profile_height);
        weight_profile = findViewById(R.id.profile_weight);
        Dcount = findViewById(R.id.dietscounter);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users").child(firebaseUser.getUid()).child("profile");
        databaseReference.setValue("EditProfile");

        userdisplay.setText(firebaseAuth.getCurrentUser().getDisplayName());
        username.getEditText().setText(firebaseAuth.getCurrentUser().getDisplayName());
        email.getEditText().setText(firebaseAuth.getCurrentUser().getEmail());

        getinfo();

        logoutuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference = firebaseDatabase.getReference("users");
                databaseReference.child(firebaseUser.getUid()).child("profile").setValue("Logged-Out");
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Profile.this, MainActivity.class));
                finishAffinity();

            }
        });

        updateinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Username_string = username.getEditText().getText().toString().trim();
                if (!Username_string.isEmpty() && Utils.USERNAME_PATTERN.matcher(Username_string).matches()) {
                    if (!Username_string.equals(firebaseUser.getDisplayName())) {
                        username.setError(null);
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(Username_string).build();
                        FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates);
                        reference = firebaseDatabase.getReference("users").child(firebaseUser.getUid()).child("username");
                        reference.setValue(Username_string).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                UpdateDoneFeedback updateDoneFeedbackuser = new UpdateDoneFeedback(Profile.this);
                                updateDoneFeedbackuser.update_animate("Username updated to " + Username_string);
                            }
                        });
                    } else {


                    }
                } else {
                    if (Username_string.isEmpty()) {
                        username.setError("Field can't be empty");
                    }
                    if (!Utils.USERNAME_PATTERN.matcher(Username_string).matches()) {
                        username.setError("Please enter valid Username with first letter capital and no numbers");
                    }
                    /* Toast.makeText(Profile.this,"Username is empty or bad format",Toast.LENGTH_SHORT).show();*/
                }

                //Email change
                email_input = email.getEditText().getText().toString().trim();
                if (!email_input.isEmpty()) {
                    if (Utils.MY_EMAIL.matcher(email_input).matches()) {
                        if (!email_input.equals(firebaseUser.getEmail())) {
                            email.setError(null);
                            Password password = new Password(Profile.this);
                            password.authenticateuser();

                        } else {
                        }
                    } else {
                        email.setError("Invalid E-mail address");
                    }
                } else {
                    email.setError("E-mail address can't be empty");
                    /*Toast.makeText(Profile.this,"Bad Email format or empty",Toast.LENGTH_SHORT).show();*/
                }
                heighttext = height_profile.getEditText().getText().toString().trim();
                weighttext = weight_profile.getEditText().getText().toString().trim();


                //Heightweight
                if (heighttext.isEmpty() || weighttext.isEmpty()) {
                    if (heighttext.isEmpty()) {
                        height_profile.setError("Field can't be empty ");
                    }

                    if (weighttext.isEmpty()) {
                        weight_profile.setError("Field can't be empty ");
                    }

                    Toast.makeText(Profile.this, "Height or Weight can't be empty", Toast.LENGTH_SHORT).show();
                } else {
                    height_profile.setError(null);
                    weight_profile.setError(null);
                    heightfloat = Float.parseFloat(heighttext);
                    weightfloat = Float.parseFloat(weighttext);
                    min_height = 0.0f;
                    max_height = 10.9f;
                    min_weight = 0.0f;
                    max_weight = 200.9f;

                    if (heightfloat <= min_height || heightfloat > max_height || weightfloat <= min_weight || weightfloat > max_weight) {
                        if (heightfloat <= min_height) {
                            height_profile.setError("You are too short!");
                        }
                        if (heightfloat > max_height) {
                            height_profile.setError("You are too big!");
                        }
                        if (weightfloat <= min_weight) {
                            weight_profile.setError("Do you eat air?");
                        }
                        if (weightfloat > max_weight) {
                            weight_profile.setError("You are too heavy!");
                        }
                        Toast.makeText(Profile.this, "INVALID HEIGHT or Weight = " + heighttext + "Ft " + weighttext + "Kg", Toast.LENGTH_SHORT).show();
                    } else {
                        height_profile.setError(null);
                        weight_profile.setError(null);
                        firebaseDatabase = FirebaseDatabase.getInstance();
                        databaseReference = firebaseDatabase.getReference("users").child(firebaseUser.getUid());
                        databaseReference.child("height").setValue(heightfloat.toString());
                        databaseReference.child("weight").setValue(weightfloat.toString());

                        updateBMI();
                        if (!bmi_string.equals(String.valueOf(bmi_check))) {
                            databaseReference.child("bmi").setValue(bmi_string).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    UpdateDoneFeedback updateDoneFeedback = new UpdateDoneFeedback(Profile.this);
                                    updateDoneFeedback.update_animate("Bmi Updated");
                                }
                            });
                        }
                    }
                }

            }
        });
    }


    private void getinfo() {

        reference = firebaseDatabase.getReference().child("users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                genderValue = dataSnapshot.child("gender").getValue().toString();
                genderprofile.setText(genderValue.toUpperCase());
                heightValue = dataSnapshot.child("height").getValue().toString();
                height_profile.getEditText().setText(heightValue);
                weightValue = dataSnapshot.child("weight").getValue().toString();
                weight_profile.getEditText().setText(weightValue);
                userdisplay.setText(dataSnapshot.child("username").getValue().toString());
                Dcount.setText(dataSnapshot.child("dietcount").getValue().toString());
                bmi_check = Float.parseFloat(dataSnapshot.child("bmi").getValue().toString());

                if (genderValue.equals("Male")) {
                    gender_img_male.setVisibility(View.VISIBLE);
                } else {
                    gender_img_female.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void updateBMI() {
        heightfloat = heightfloat * 0.3048f;
        heightfloat = heightfloat * heightfloat;
        bmi = weightfloat / heightfloat;
        bmi_string = String.valueOf(bmi);
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        bmi_string = df.format(bmi);

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
