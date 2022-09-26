package com.example.healify;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.StringTokenizer;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;


    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.place_info, null);
    }

    private void rendowWindowText(Marker marker, View view) {


        if (marker.getSnippet() != null) {
            CardView dietcardview = (CardView) view.findViewById(R.id.dietitiancard);
            dietcardview.setVisibility(View.VISIBLE);
            CardView usercardview = (CardView) view.findViewById(R.id.usercard);
            usercardview.setVisibility(View.GONE);
            String title = marker.getTitle();
            TextView tvTitle = (TextView) view.findViewById(R.id.dietitian_name);

            if (!title.equals("")) {
                tvTitle.setText(title);
            }

            String snippet = marker.getSnippet();
            StringTokenizer st = new StringTokenizer(snippet, "^");
            String add = st.nextToken();
            String rate = st.nextToken();
            String addressdiet = st.nextToken();
            String userratings = st.nextToken();
            TextView addressdietitians = (TextView) view.findViewById(R.id.vic);
            addressdietitians.setText(addressdiet);
            TextView tvSnippet = (TextView) view.findViewById(R.id.address);
            System.out.println(add);
            if (add.equals("Is Open Now")) {
                tvSnippet.setTextColor(mContext.getResources().getColor(R.color.dkgreen));
            } else {
                tvSnippet.setTextColor(Color.RED);
            }
            tvSnippet.setText(add);
            float ratefloat = Float.parseFloat(rate);
            RatingBar ratesdiet = (RatingBar) view.findViewById(R.id.rates);
            ratesdiet.setRating(ratefloat);
            TextView users = (TextView) view.findViewById(R.id.userRate);
            users.setText(userratings);
        } else {
            CardView dietcardview = (CardView) view.findViewById(R.id.dietitiancard);
            CardView usercardview = (CardView) view.findViewById(R.id.usercard);
            dietcardview.setVisibility(View.GONE);
            usercardview.setVisibility(View.VISIBLE);

        }


    }

    @Override
    public View getInfoWindow(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }


}