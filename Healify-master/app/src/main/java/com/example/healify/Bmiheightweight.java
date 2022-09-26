package com.example.healify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Bmiheightweight extends AppCompatActivity {

    NumberPicker program_numberpicker_height, program_numberpicker_weight, program_numberpicker_height_decimal, program_numberpicker_weight_decimal;
    Button back_btn, nextbmi_btn;
    String picker_weight, TAG = "my app", picker_height, picker_weight_decimal, picker_height_decimal, Bmi_function, string_users_bmi, formattedString_hieght, formattedString_wieght;
    Float Bmi_Height_Decimal, Bmi_Weight_Decimal, Bmi_total_height, Bmi_total_weight, changer_float, Bmi_graph, Bmi_total_height_final_squared, Bmi_total_height_final_changed;

    String changer = "0.3048";


    int bmi_height_int, bmi_weight_int;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;


    public final static String USER_BMI = "com.example.healify";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmiheightweight);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }


        program_numberpicker_height = findViewById(R.id.number_picker_height);
        program_numberpicker_weight = findViewById(R.id.number_picker_weight);

        back_btn = findViewById(R.id.back_button);
        program_numberpicker_height_decimal = findViewById(R.id.number_picker_height_decimal);
        program_numberpicker_weight_decimal = findViewById(R.id.number_picker_weight_decimal);
        nextbmi_btn = findViewById(R.id.next_bmi);


        program_numberpicker_height.setMaxValue(10);
        program_numberpicker_height.setMinValue(1);
        program_numberpicker_height.setWrapSelectorWheel(true);

        program_numberpicker_height_decimal.setMaxValue(9);
        program_numberpicker_height_decimal.setMinValue(1);
        program_numberpicker_height_decimal.setWrapSelectorWheel(true);


        program_numberpicker_weight_decimal.setMaxValue(9);
        program_numberpicker_weight_decimal.setMinValue(0);

        program_numberpicker_weight_decimal.setWrapSelectorWheel(true);


        program_numberpicker_weight.setMaxValue(200);
        program_numberpicker_weight.setMinValue(1);
        program_numberpicker_weight.setWrapSelectorWheel(true);
        program_numberpicker_weight.setValue(1);


        program_numberpicker_weight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVale) {


                bmi_weight_int = newVale;
                picker_weight = "" + newVale;
                //Bmi_Weight = Float.parseFloat(picker_weight);

            }
        });
        program_numberpicker_height.setValue(0);
        program_numberpicker_height.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal_hieght) {
                bmi_height_int = newVal_hieght;
                picker_height = "" + newVal_hieght;
                // Bmi_Height =Float.parseFloat(picker_height);

            }
        });
        program_numberpicker_height_decimal.setValue(0);
        program_numberpicker_height_decimal.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal_h_decimal, int newVal_h_decimal) {
                picker_height_decimal = "" + newVal_h_decimal;

                Bmi_Height_Decimal = Float.parseFloat(picker_height_decimal);
                Bmi_Height_Decimal = Bmi_Height_Decimal / 10;

            }
        });
        program_numberpicker_weight_decimal.setValue(0);
        program_numberpicker_weight_decimal.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal_w_decimal, int newVal_w_decimal) {

                picker_weight_decimal = "" + newVal_w_decimal;

                Bmi_Weight_Decimal = Float.parseFloat(picker_weight_decimal);
                Bmi_Weight_Decimal = Bmi_Weight_Decimal / 10;

            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        //if (Bmi_Height_Decimal != null && Bmi_Weight_Decimal != null && picker_height!=null && picker_weight !=null )  {
        nextbmi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bmimaker();
                if (Bmi_graph != null) {

                    Bmi_function = Double.toString(Bmi_graph);
/*
                    Log.w(TAG, "weight decimal>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + Bmi_total_weight);
                    Log.w(TAG, "height decimal>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + Bmi_total_height_final_squared);
*/
                    // Toast.makeText(Bmiheightweight.this, picker_weight_decimal + picker_weight_decimal, Toast.LENGTH_LONG).show();
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    reference = firebaseDatabase.getReference("users");

                    string_users_bmi = firebaseAuth.getCurrentUser().getUid();

                    reference.child(string_users_bmi).child("height").setValue(formattedString_hieght);
                    reference.child(string_users_bmi).child("weight").setValue(formattedString_wieght);


                    Intent intent = new Intent(Bmiheightweight.this, Graph.class);
                    intent.putExtra(USER_BMI, Bmi_function);
                    startActivity(intent);

                } else {
                    Toast.makeText(Bmiheightweight.this, "NULL VALUES!!!", Toast.LENGTH_SHORT).show();

                    Toast.makeText(Bmiheightweight.this, "Please RE-ROLL all the number picker to refresh the values", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void Bmimaker() throws NullPointerException {
        try {

            Bmi_total_height = bmi_height_int + Bmi_Height_Decimal;
            formattedString_hieght = String.format("%.01f", Bmi_total_height);
            Bmi_total_weight = bmi_weight_int + Bmi_Weight_Decimal;
            changer_float = Float.parseFloat(changer);
            Bmi_total_height_final_changed = Bmi_total_height * changer_float;
            Bmi_total_height_final_squared = Bmi_total_height_final_changed * Bmi_total_height_final_changed;
            formattedString_wieght = String.format("%.01f", Bmi_total_weight);
            Bmi_graph = Bmi_total_weight / Bmi_total_height_final_squared;


        } catch (NullPointerException e) {

            Toast.makeText(Bmiheightweight.this, " NULL VALUES!!! ", Toast.LENGTH_SHORT).show();
        }

    }
}
