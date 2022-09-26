package com.example.healify;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.progresviews.ProgressLine;
import com.bumptech.glide.Glide;

import java.util.StringTokenizer;

import static com.example.healify.DietActivity.CATEGORY;

public class DietDetail extends AppCompatActivity {

    ImageView imageDiet;
    TextView breakfasttext, lunchtext, snacktext, dinnertext, category,cal;
    @Deprecated
    Toolbar toolbar1;
    CollapsingToolbarLayout collapsingToolbarLayout;
    AppBarLayout appBarLayout;
    ProgressBar dietbar;
    String dietpic, fats, proteins, carbs;
    ProgressLine progressFats, progressProtein, progressCarbs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_detail);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.transparent));
        }

        dietbar = findViewById(R.id.progressBarDiet);
        dietbar.setVisibility(View.VISIBLE);

        imageDiet = findViewById(R.id.mealThumbDiet);
        breakfasttext = findViewById(R.id.breakfast);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        toolbar1 = findViewById(R.id.toolbarDiet);
        snacktext = findViewById(R.id.snack);
        lunchtext = findViewById(R.id.lunch);
        dinnertext = findViewById(R.id.dinner);
        category = findViewById(R.id.categoryDiet);
        progressFats = findViewById(R.id.fatline);
        progressProtein = findViewById(R.id.proteinslline);
        progressCarbs = findViewById(R.id.carbsline);
        cal = findViewById(R.id.measurecal);


        progressFats.setmPercentage(100);
        progressProtein.setmPercentage(100);
        progressCarbs.setmPercentage(100);


        Bundle mbundle = getIntent().getExtras();

        if (mbundle != null) {
            collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.pink));
            collapsingToolbarLayout.setTitle(mbundle.getString("title"));
            breakfasttext.setText(mbundle.getString("meal1"));
            lunchtext.setText(mbundle.getString("meal2"));
            snacktext.setText(mbundle.getString("meal3"));
            dinnertext.setText(mbundle.getString("meal4"));
            fats = mbundle.getString("fats");
            carbs = mbundle.getString("carbs");
            proteins = mbundle.getString("protiens");
            String image = mbundle.getString("image");
            Glide.with(this).load(image).into(imageDiet);
            dietbar.setVisibility(View.INVISIBLE);
            category.setText(CATEGORY);
            getGraphNumber();
        }

        // Toast.makeText(this,dietDescriptionText.getText(),Toast.LENGTH_SHORT).show();
        setupActionBar();


    }


    private void setupActionBar() {
        setSupportActionBar(toolbar1);
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.white));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.pink));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /*
        void setupColorActionBarIcon(Drawable favoriteItemColor) {
            appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
                if ((collapsingToolbarLayout.getHeight() + verticalOffset) < (2 * ViewCompat.getMinimumHeight(collapsingToolbarLayout))) {
                    if (toolbar1.getNavigationIcon() != null)
                        toolbar1.getNavigationIcon().setColorFilter(getResources().getColor(R.color.pink), PorterDuff.Mode.SRC_ATOP);
                    favoriteItemColor.mutate().setColorFilter(getResources().getColor(R.color.pink),
                            PorterDuff.Mode.SRC_ATOP);

                } else {
                    if (toolbar1.getNavigationIcon() != null)
                        toolbar1.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                    favoriteItemColor.mutate().setColorFilter(getResources().getColor(R.color.white),
                            PorterDuff.Mode.SRC_ATOP);
                }
            });
        }
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getGraphNumber() {
        cal = findViewById(R.id.measurecal);
        progressFats = findViewById(R.id.fatline);
        progressProtein = findViewById(R.id.proteinslline);
        progressCarbs = findViewById(R.id.carbsline);

        StringTokenizer fat_splitter = new StringTokenizer(fats, "g");
        fats = fat_splitter.nextToken().trim();
        StringTokenizer carbs_splitter = new StringTokenizer(carbs, "g");
        carbs = carbs_splitter.nextToken().trim();
        StringTokenizer proteins_splitter = new StringTokenizer(proteins, "g");
        proteins = proteins_splitter.nextToken().trim();


        float fats_num = Float.parseFloat(fats);
        float carbs_num = Float.parseFloat(carbs);
        float proteins_num = Float.parseFloat(proteins);

        float calories = Math.round(proteins_num * 4) + (fats_num * 9) + (carbs_num * 4);


        fats_num = Math.round((fats_num * 9 / calories) * 100);
        carbs_num = Math.round((carbs_num * 4 / calories) * 100);
        proteins_num = Math.round((proteins_num * 4 / calories) * 100);

        fats = String.valueOf(fats_num);
        carbs = String.valueOf(carbs_num);
        proteins = String.valueOf(proteins_num);

        StringTokenizer fat_splitter_dot = new StringTokenizer(fats, ".");
        fats = fat_splitter_dot.nextToken().trim();
        StringTokenizer carbs_splitter_dot = new StringTokenizer(carbs, ".");
        carbs = carbs_splitter_dot.nextToken().trim();
        StringTokenizer proteins_splitter_dot = new StringTokenizer(proteins, ".");
        proteins = proteins_splitter_dot.nextToken().trim();

        cal.setText(String.valueOf(calories)+" Cal");



        int fat_percentage = Integer.parseInt(fats);
        int carbs_percentage = Integer.parseInt(carbs);
        int proteins_percentage = Integer.parseInt(proteins);
        progressFats.setmPercentage(fat_percentage);
        progressProtein.setmPercentage(proteins_percentage);
        progressCarbs.setmPercentage(carbs_percentage);




    }

}
