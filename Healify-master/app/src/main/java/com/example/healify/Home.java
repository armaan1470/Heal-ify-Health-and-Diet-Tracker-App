package com.example.healify;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.healify.view.home.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.harjot.crollerTest.Croller;

import com.sdsmdg.harjot.crollerTest.OnCrollerChangeListener;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;

import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class Home extends AppCompatActivity {

    CardView profileCard, dietCard, recipeCard, dietitianCard;
    TextView text_useremail, crollerlable, greeting, goaltext;
    Button mygoal;
    ImageView profilepic;
    String value, indicator, value_bmi;
    float bmi;
    int updatedprogress, initialprogress;
    SliderView sliderView;
    List<SliderItem> mSliderItems;
    String tipNum;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, reference, tipreference, goalref;

    @Override
    protected void onStart() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users").child(firebaseUser.getUid()).child("profile");
        databaseReference.setValue("Homepage");
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home1);

        tipNum = String.valueOf(getRandomNumberInRange(1, 3));
        //  Toast.makeText(Home.this, tipNum, Toast.LENGTH_LONG).show();
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.pink));
        }
        greeting = findViewById(R.id.welcome);
        profilepic = findViewById(R.id.profileimage);
        profileCard = findViewById(R.id.profile_card);
        text_useremail = findViewById(R.id.welcome_user);
        dietCard = findViewById(R.id.diets_card);
        recipeCard = findViewById(R.id.recipe_card);
        dietitianCard = findViewById(R.id.Dietitian_card);
        crollerlable = findViewById(R.id.crollerbmi);
        Croller croller = (Croller) findViewById(R.id.croller);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        text_useremail.setText(firebaseUser.getDisplayName());
        sliderView = findViewById(R.id.imageSlider);
        mygoal = findViewById(R.id.goalusers);
        goaltext = findViewById(R.id.GoalText);

        showLoading();

        mSliderItems = new ArrayList<>();
        SliderAdapterExample sliderAdapterExample = new SliderAdapterExample(this, mSliderItems);
        sliderView.setSliderAdapter(sliderAdapterExample);


        firebaseDatabase = FirebaseDatabase.getInstance();
        tipreference = firebaseDatabase.getReference("tips");

        tipreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mSliderItems.clear();
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        SliderItem sliderItem = itemSnapshot.getValue(SliderItem.class);
                        mSliderItems.add(sliderItem);
                        //   Toast.makeText(Home.this, itemSnapshot.getValue().toString(), Toast.LENGTH_LONG).show();
                    }
                    sliderAdapterExample.notifyDataSetChanged();
                    sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                    sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                    sliderView.setScrollTimeInSec(3);
                    sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
                    sliderView.setIndicatorSelectedColor(Color.WHITE);
                    sliderView.setIndicatorUnselectedColor(Color.GRAY);
                    sliderView.setScrollTimeInSec(7); //set scroll delay in seconds :
                    sliderView.startAutoCycle();
                }
                hideLoading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users").child(firebaseUser.getUid()).child("profile");
        databaseReference.setValue("Homepage");

        retrieveGoal();

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay >= 0 && timeOfDay < 12) {
            String greet = "Good Morning";
            greeting.setText(greet);

        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            String greet = "Good Afternoon";
            greeting.setText(greet);

        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            String greet = "Good Evening";
            greeting.setText(greet);

        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            String greet = "Good Night";
            greeting.setText(greet);

        }


        reference = firebaseDatabase.getReference("users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                value = dataSnapshot.child("bmi").getValue().toString();
                value_bmi = value;
                bmi = Float.parseFloat(value);

                initialprogress = Integer.parseInt(String.valueOf(Math.round(bmi)));
                if (initialprogress > 50) {
                    initialprogress = 50;
                }
                croller.setProgress(initialprogress);
                if (Float.compare(bmi, 15f) <= 0) {

                    indicator = "Underweight";

                } else if (Float.compare(bmi, 15f) > 0 && Float.compare(bmi, 16f) <= 0) {

                    indicator = "Underweight";

                } else if (Float.compare(bmi, 16f) > 0 && Float.compare(bmi, 18.5f) <= 0) {

                    indicator = "Slightly Underweight";

                } else if (Float.compare(bmi, 18.5f) > 0 && Float.compare(bmi, 25f) <= 0) {

                    indicator = "Healthy Weight";

                } else if (Float.compare(bmi, 25f) > 0 && Float.compare(bmi, 30f) <= 0) {
                    indicator = "Slightly Overweight";

                } else if (Float.compare(bmi, 30f) > 0 && Float.compare(bmi, 35f) <= 0) {
                    indicator = "Overweight Class-I";

                } else if (Float.compare(bmi, 35f) > 0 && Float.compare(bmi, 40f) <= 0) {
                    indicator = "Overweight Class-II";

                } else {
                    indicator = "Overweight Class-III";

                }

                croller.setLabel(indicator);
                crollerlable.setText(value);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        croller.setOnCrollerChangeListener(new OnCrollerChangeListener() {
            @Override
            public void onProgressChanged(Croller croller, int progress) {
                updatedprogress = progress;
            }

            @Override
            public void onStartTrackingTouch(Croller croller) {
             /*   mygoal.setVisibility(View.VISIBLE);
                mygoal.isEnabled();
                mygoal.setText("SET MY GOAL TO " + updatedprogress + " kg/m²");*/

            }

            @Override
            public void onStopTrackingTouch(Croller croller) {


                if (updatedprogress != initialprogress) {
                    mygoal.setVisibility(View.VISIBLE);
                    mygoal.isEnabled();
                    mygoal.setText("SET MY GOAL TO " + updatedprogress + " kg/m²");
                } else {
                    mygoal.setVisibility(View.INVISIBLE);
                }
            }
        });
        profileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Profile.class));

            }
        });

        recipeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, HomeActivity.class);
                startActivity(i);
                databaseReference = firebaseDatabase.getReference("users");
                databaseReference.child(firebaseUser.getUid()).child("profile").setValue("Recipes");

            }
        });

        dietCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, DietActivity.class);
                i.putExtra("BMI", bmi);
                startActivity(i);
                databaseReference = firebaseDatabase.getReference("users");
                databaseReference.child(firebaseUser.getUid()).child("profile").setValue("Diets");


            }
        });
        dietitianCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, MapsActivity.class));

                databaseReference = firebaseDatabase.getReference("users");
                databaseReference.child(firebaseUser.getUid()).child("profile").setValue("Dietitians");
            }
        });
        mygoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goal();
            }
        });
    }

    public void goal() {
        if (updatedprogress > 25) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(Home.this);
            builder1.setMessage("You are about to set your goal to over-weight status according to Dieto");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            goalref = firebaseDatabase.getReference("goals").child(firebaseUser.getUid());
                            goalref.setValue(String.valueOf(updatedprogress)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        UpdateDoneFeedback updateDoneFeedback = new UpdateDoneFeedback(Home.this);
                                        updateDoneFeedback.update_animate("Your goal is updated");
                                    }
                                }
                            });
                            dialog.cancel();

                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        } else if (updatedprogress < 19) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(Home.this);
            builder1.setMessage("You are about to set your goal to under-weight status according to Dieto");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            goalref = firebaseDatabase.getReference("goals").child(firebaseUser.getUid());
                            goalref.setValue(String.valueOf(updatedprogress)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        UpdateDoneFeedback updateDoneFeedback = new UpdateDoneFeedback(Home.this);
                                        updateDoneFeedback.update_animate("Your goal is updated");
                                    }
                                }
                            });
                            dialog.cancel();

                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            UpdateDoneFeedback updateDoneFeedback = new UpdateDoneFeedback(Home.this);
                            updateDoneFeedback.update_animate("Your goal is updated");
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        } else {
            goalref = firebaseDatabase.getReference("goals").child(firebaseUser.getUid());
            goalref.setValue(String.valueOf(updatedprogress));
            UpdateDoneFeedback updateDoneFeedback = new UpdateDoneFeedback(Home.this);
            updateDoneFeedback.update_animate("Your goal is updated");
        }


    }

    private void retrieveGoal() {
        goalref = firebaseDatabase.getReference("goals").child(firebaseUser.getUid());
        goalref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String value1 = dataSnapshot.getValue().toString();
                    if (!value1.isEmpty()) {
                        goaltext = findViewById(R.id.GoalText);
                        goaltext.setText("YOUR GOAL IS " + value1 + " kg/m²");
                    } else {
                        goaltext.setText("YOUR GOAL IS NOT SET");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showLoading() {
        findViewById(R.id.shimmertip).setVisibility(View.VISIBLE);
        findViewById(R.id.shimmerbmi).setVisibility(View.VISIBLE);

    }

    public void hideLoading() {
        findViewById(R.id.shimmertip).setVisibility(View.GONE);
        findViewById(R.id.shimmerbmi).setVisibility(View.GONE);
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }


}

