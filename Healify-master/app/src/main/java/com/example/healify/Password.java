package com.example.healify;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.healify.Profile.email_input;

public class Password {
    private Activity activity;
    private AlertDialog dialog;
    Button confirmpassword, cancelbutton, okay;
    TextInputLayout passwordText;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, reference;
    TextView sureUser, updateduser;
    ProgressBar password_loading;
    RelativeLayout main, update;
    ImageView updateemailgif;


    public Password(Activity myActivity) {
        activity = myActivity;

    }

    boolean authenticateuser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.activity_password, null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
        main = dialog.findViewById(R.id.mainlayout);
        confirmpassword = dialog.findViewById(R.id.Confirm);
        update = dialog.findViewById(R.id.updatedLayout);
        cancelbutton = dialog.findViewById(R.id.Cancel);
        sureUser = dialog.findViewById(R.id.sure_user);
        password_loading = dialog.findViewById(R.id.passwordloading);
        main.setVisibility(View.VISIBLE);
        update.setVisibility(View.INVISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users").child(firebaseUser.getUid()).child("profile");
        databaseReference.setValue("updateEmail");

        sureUser.setText("Are you really " + firebaseUser.getDisplayName() + " ?");
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        confirmpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPasswordnow();
                builder.setCancelable(false);

            }
        });
        return true;
    }

    private void getPasswordnow() {

        updateduser = dialog.findViewById(R.id.emailupdated);
        okay = dialog.findViewById(R.id.closed);
        main = dialog.findViewById(R.id.mainlayout);
        update = dialog.findViewById(R.id.updatedLayout);
        updateemailgif = dialog.findViewById(R.id.updategif);

        password_loading.setIndeterminate(true);
        passwordText = dialog.findViewById(R.id.ConfirmPassword);
        String password = passwordText.getEditText().getText().toString().trim();
        if (password.isEmpty()) {
            password_loading.setIndeterminate(false);
            Toast.makeText(activity, "Cant be empty", Toast.LENGTH_SHORT).show();
        } else {
            reference = firebaseDatabase.getReference("users").child(firebaseUser.getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String firepass = dataSnapshot.child("password").getValue().toString();
                    if (password.equals(firepass)) {
                        firebaseAuth = FirebaseAuth.getInstance();
                        firebaseUser = firebaseAuth.getCurrentUser();
                        String USer = firebaseUser.getEmail();

                        AuthCredential credential = EmailAuthProvider
                                .getCredential(USer, password);
                        firebaseUser.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        firebaseUser.updateEmail(email_input).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    password_loading.setIndeterminate(false);
                                                    reference.child("email").setValue(email_input).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                UpdateDoneFeedback updateDoneFeedback = new UpdateDoneFeedback(activity);
                                                                updateDoneFeedback.update_animate("Email changes to " + email_input);
                                                                dialog.dismiss();
                                                            }

                                                        }
                                                    });
                                                } else {
                                                    Toast.makeText(activity, "Error" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                                password_loading.setIndeterminate(false);
                                            }
                                        });
                                    }
                                });
                    } else {
                        password_loading.setIndeterminate(false);
                        Toast.makeText(activity, "Wrong password", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }


    }

    public void updateUsernamePass(String s) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.activity_password, null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();

        updateduser = dialog.findViewById(R.id.emailupdated);
        okay = dialog.findViewById(R.id.closed);
        main = dialog.findViewById(R.id.mainlayout);
        update = dialog.findViewById(R.id.updatedLayout);
        updateemailgif = dialog.findViewById(R.id.updategif);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        main.setVisibility(View.INVISIBLE);
        update.setVisibility(View.VISIBLE);
        Glide.with(activity).load(R.drawable.croppedtick).into(updateemailgif);
        updateduser.setText("Username updated to " + s);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}
