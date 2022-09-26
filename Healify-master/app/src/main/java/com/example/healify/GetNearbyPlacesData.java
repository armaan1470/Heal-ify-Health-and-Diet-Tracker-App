package com.example.healify;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class GetNearbyPlacesData extends AsyncTask<Object, String, String>  {
    String googlePlacesData;
    GoogleMap mMap;
    String url, open, name,rating,userrating,finalRating,address,latitude,longitude;
    Context context;
    private Polyline currentPolyline;


    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googlePlacesData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject parentObject = new JSONObject(s);
            JSONArray resultarray = parentObject.getJSONArray("results");
            for (int i = 0; i < resultarray.length(); i++) {
                JSONObject jsonObject = resultarray.getJSONObject(i);
                JSONObject locationobject = jsonObject.getJSONObject("geometry").getJSONObject("location");
                latitude = locationobject.getString("lat");
                longitude = locationobject.getString("lng");

                JSONObject nameobject = resultarray.getJSONObject(i);
                name = nameobject.getString("name");
                if (nameobject.has("opening_hours")) {
                    open = nameobject.getJSONObject("opening_hours").getString("open_now");
                    if(open.equals("true")){open = "Is Open Now";}
                    else {open = "Closed Now";}
                } else {
                    open = "Opening hours Not Available";
                }
                rating = nameobject.getString("rating");
                userrating = nameobject.getString("user_ratings_total");

                finalRating = "("+userrating+")";
                address = nameobject.getString("vicinity");

                LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(name)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.markers))
                .snippet(open+ "^"+rating+"^"+address+"^"+finalRating+"^"+latitude+"^"+longitude));

              /*  mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Toast.makeText(context,name+ " " + open,Toast.LENGTH_LONG).show();
                        return true;
                    }
                });*/

            }

        }

        catch (JSONException e) {
            e.printStackTrace();
        }

        }

}
