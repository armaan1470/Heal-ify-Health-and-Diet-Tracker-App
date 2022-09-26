package com.example.healify;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class DietActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference, dietref;

    RecyclerView dietCycle;
    List<DietData> myDietlist;
    DietData dietdata;
    Float bmi;
    public static  String CATEGORY = "";

    final LoadingDialog loadingDialog = new LoadingDialog(DietActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Diets");

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.pink));
        }



        Intent i = getIntent();
        bmi = i.getFloatExtra("BMI",0);

        loadingDialog.startLoadingDialog();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("users").child(firebaseUser.getUid());
        reference.child("profile").setValue("Diets");


        dietCycle = findViewById(R.id.recyclerViewDietLAY);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(DietActivity.this, 1
        );
        dietCycle.setLayoutManager(gridLayoutManager);

        myDietlist = new ArrayList<>();



        if (Float.compare(bmi, 15f) <= 0) {
            CATEGORY="Gaining Diet";
            dietref = firebaseDatabase.getReference("diets").child("gain");

        } else if (Float.compare(bmi, 15f) > 0 && Float.compare(bmi, 16f) <= 0) {
            CATEGORY="Gaining Diet";
            dietref = firebaseDatabase.getReference("diets").child("gain");
        } else if (Float.compare(bmi, 16f) > 0 && Float.compare(bmi, 18.5f) <= 0) {
            CATEGORY="Gaining Diet";
            dietref = firebaseDatabase.getReference("diets").child("gain");
        } else if (Float.compare(bmi, 18.5f) > 0 && Float.compare(bmi, 25f) <= 0) {
            CATEGORY="Body Maintenance Diet";
            dietref = firebaseDatabase.getReference("diets").child("maintain");
        } else if (Float.compare(bmi, 25f) > 0 && Float.compare(bmi, 30f) <= 0) {
            dietref = firebaseDatabase.getReference("diets").child("loose");
            CATEGORY="Weight loose Diet";
        } else if (Float.compare(bmi, 30f) > 0 && Float.compare(bmi, 35f) <= 0) {
            dietref = firebaseDatabase.getReference("diets").child("loose");
            CATEGORY="Weight loose Diet";
        } else if (Float.compare(bmi, 35f) > 0 && Float.compare(bmi, 40f) <= 0) {
            dietref = firebaseDatabase.getReference("diets").child("loose");
            CATEGORY="Weight loose Diet";
        } else {
            CATEGORY="Weight loose Diet";
            dietref = firebaseDatabase.getReference("diets").child("loose");
        }

        DietAdapter dietAdapter = new DietAdapter(DietActivity.this, myDietlist);
        dietCycle.setAdapter(dietAdapter);
        dietref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    myDietlist.clear();
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {

                        dietdata = itemSnapshot.getValue(DietData.class);
                        myDietlist.add(dietdata);

                    }
                    dietAdapter.notifyDataSetChanged();
                }
                loadingDialog.dissmissDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("users").child(firebaseUser.getUid());
        reference.child("profile").setValue("Diets");
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchreport,menu);
        MenuItem searchItem = menu.findItem(R.id.searchdietsinmenu);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<DietData> mysearchlist = new ArrayList<>();

                for(DietData dietDatalist : myDietlist){
                    if(dietDatalist.getName().toLowerCase().contains(s.toLowerCase())){
                        mysearchlist.add(dietDatalist);
                    }
                }
                DietAdapter apdapterlist = new DietAdapter(DietActivity.this,mysearchlist);
                dietCycle.setAdapter(apdapterlist);
                return false;
            }
        });

        MenuItem reportbutton = menu.findItem(R.id.reportingSystem);
        reportbutton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Report report = new Report(DietActivity.this);
                report.sendReport();
                return false;
            }
        });

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
