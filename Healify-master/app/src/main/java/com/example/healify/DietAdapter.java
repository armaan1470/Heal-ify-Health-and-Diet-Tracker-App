package com.example.healify;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DietAdapter extends RecyclerView.Adapter<DietViewHolder>  {

    private Context Dietcontext;
    private List<DietData> myDietList;
    private List<DietData> MyDietListFull;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference, databaseReference;
    FirebaseUser firebaseUser;
    String DietCount;
    Integer dietcounter;


    public DietAdapter(Context dietcontext, List<DietData> myDietList) {
        Dietcontext = dietcontext;
        this.myDietList = myDietList;
        this.MyDietListFull = new ArrayList<>(myDietList);

    }


    @NonNull
    @Override
    public DietViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View dietview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_row_diet, viewGroup, false);

        return new DietViewHolder(dietview);
    }

    @Override
    public void onBindViewHolder(@NonNull DietViewHolder dietViewHolder, int i) {

        Glide.with(Dietcontext).load(myDietList.get(i).getImage()).into(dietViewHolder.dietimage);
        dietViewHolder.dietname.setText(myDietList.get(i).getName());
        dietViewHolder.dietdesc.setText(myDietList.get(i).getDesc());
        dietViewHolder.dietrate.setRating(myDietList.get(i).getRatings());


        dietViewHolder.dietcardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                firebaseAuth = FirebaseAuth.getInstance();
                firebaseDatabase = FirebaseDatabase.getInstance();
                firebaseUser = firebaseAuth.getCurrentUser();

                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("users").child(firebaseUser.getUid()).child("profile");
                databaseReference.setValue("DietClicked");

                reference = firebaseDatabase.getReference("users").child(firebaseUser.getUid());
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        DietCount = dataSnapshot.child("dietcount").getValue().toString();
                        dietcounter = Integer.parseInt(DietCount);
                        dietcounter=dietcounter+1;
                        reference.child("dietcount").setValue(dietcounter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                Intent intent = new Intent(Dietcontext,DietDetail.class);
                intent.putExtra("image",myDietList.get(dietViewHolder.getAdapterPosition()).getImage());
                intent.putExtra("title",myDietList.get(dietViewHolder.getAdapterPosition()).getName());
                intent.putExtra("meal1",myDietList.get(dietViewHolder.getAdapterPosition()).getMeal1());
                intent.putExtra("meal2",myDietList.get(dietViewHolder.getAdapterPosition()).getMeal2());
                intent.putExtra("meal3",myDietList.get(dietViewHolder.getAdapterPosition()).getMeal3());
                intent.putExtra("meal4",myDietList.get(dietViewHolder.getAdapterPosition()).getMeal4());
                intent.putExtra("fats",myDietList.get(dietViewHolder.getAdapterPosition()).getFats());
                intent.putExtra("protiens",myDietList.get(dietViewHolder.getAdapterPosition()).getProtiens());
                intent.putExtra("carbs",myDietList.get(dietViewHolder.getAdapterPosition()).getCarbs());
                Dietcontext.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return myDietList.size();
    }



}
class   DietViewHolder extends RecyclerView.ViewHolder{
    ImageView dietimage;
    TextView dietname,dietdesc;
    RatingBar dietrate;
    CardView dietcardview;

    public DietViewHolder(View itemView) {
        super(itemView);
        dietimage = itemView.findViewById(R.id.dietImage);
        dietname = itemView.findViewById(R.id.dietNameText);
        dietdesc = itemView.findViewById(R.id.dietDescText);
        dietrate = itemView.findViewById(R.id.dietRating);
        dietcardview=itemView.findViewById(R.id.myCardView);


    }
    }
