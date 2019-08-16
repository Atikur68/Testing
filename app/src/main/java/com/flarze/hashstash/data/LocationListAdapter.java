package com.flarze.hashstash.data;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flarze.hashstash.R;
import com.flarze.hashstash.activity.MapsActivity;
import com.flarze.hashstash.activity.SignUpActivity;
import com.flarze.hashstash.data.instagram_login.AppPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<LocationList> locationLists = new ArrayList<>();
    private Context context;

    public LocationListAdapter(List<LocationList> locationLists, Context context) {
        this.locationLists = locationLists;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.location_list_single_row, viewGroup, false);
        return new LocationListAdapter.TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        ((LocationListAdapter.TeamViewHolder) holder).locationImage.setImageResource(locationLists.get(i).getLocationImage());
        ((LocationListAdapter.TeamViewHolder) holder).locationName.setText(locationLists.get(i).getLocatonName());
    }

    @Override
    public int getItemCount() {
        return locationLists == null ? 0 : locationLists.size();
    }

    public class TeamViewHolder extends RecyclerView.ViewHolder {

        private ImageView locationImage;
        private TextView locationName;
        String comment, time, date, location, locationid, latitude, longitude, hashorstash, userId;


        public TeamViewHolder(View itemView) {
            super(itemView);

            locationName = itemView.findViewById(R.id.locationName);
            locationImage = itemView.findViewById(R.id.locationImage);

            locationName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LocationList locationList = locationLists.get(getLayoutPosition());
//                    hashorstash=((MapsActivity)context).hashOrStash;
//                    time=((MapsActivity)context).Time;
//                    date=((MapsActivity)context).Date;
//                    location=locationList.getLocatonName();
//                    locationid=locationList.getLocationId();
//                    longitude=locationList.getLocatonLon();
//                    latitude=locationList.getLocatonLat();
//                    userId=((MapsActivity)context).userId;

                    hashorstash = "stash";
                    time = "1565805367";
                    // date=((MapsActivity)context).Date;
                    location = "Shiker";
                    locationid = "4321";
                    longitude = "22.8661445";
                    latitude = "90.2663245";
                    userId = "33";

                    if (hashorstash.contains("hash")) {
                        comment = ((MapsActivity) context).edt_hash_comment_str;
                    } else {
                        // comment=((MapsActivity)context).edt_shash_comment_str;
                        comment = "Stash Checking";
                    }

                    Map<String, String> param = new HashMap<String, String>();
                    // Adding All values to Params.

                    param.put("comments", comment);
                    param.put("cmtTime", time);
                    param.put("location", location);
                    param.put("locationId", locationid);
                    param.put("latitude", longitude);
                    param.put("longitude", longitude);
                    param.put("duration", "120");
                    param.put("hashOrStash", hashorstash);

                    String HttpUrl = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/" + userId + "/hashorstash";
                    //  139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/33/hash-or-stash


                  //  StringRequest jsonObjReq = new StringRequest(Request.Method.POST, HttpUrl, new com.android.volley.Response.Listener<String>

                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, HttpUrl,new JSONObject(param), new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            //   Log.d("response:", response.toString());

                            Toast.makeText(context, "Successfull" + response, Toast.LENGTH_SHORT).show();
                        }


                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//                            Map<String, String> param = new HashMap<String, String>();
//                            // Adding All values to Params.
//
//                            param.put("comments", comment);
//                            param.put("cmtTime", time);
//                            param.put("location", location);
//                            param.put("locationId", locationid);
//                            param.put("latitude", longitude);
//                            param.put("longitude", longitude);
//                            param.put("duration", "120");
//                            param.put("hashOrStash", hashorstash);
//
//                            return param;
//                        }


                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "application/json; charset=utf-8");
                            headers.put("Transfer-Encoding", "chunked");
                            return headers;
                        }

                    };


                    RequestQueue requestQueue;
                    jsonObjReq.setShouldCache(false);
                    //  Creating RequestQueue.
                    requestQueue = Volley.newRequestQueue(context);
                    // Adding the StringRequest object into requestQueue.
                    requestQueue.add(jsonObjReq);


                    ((MapsActivity) context).dialogDismiss();
                }
            });

        }
    }
}
