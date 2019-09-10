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
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flarze.hashstash.R;
import com.flarze.hashstash.activity.MapsActivity;
import com.flarze.hashstash.activity.SignUpActivity;
import com.flarze.hashstash.data.instagram_login.AppPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
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
                    hashorstash=((MapsActivity)context).hashOrStash;


                    if (hashorstash.contains("hash")) {
                        comment = ((MapsActivity) context).edt_hash_comment_str;
                    } else {
                         comment=((MapsActivity)context).edt_shash_comment_str;
                    }

                    try {
//                        hashorstash = "0";
//                      time = "1568096021";
//                       location = "Shiker";
//                        locationid = "4321";
//                        latitude = "37.6955209";
//                        longitude = "-122.1045643";
//                        userId = "1";


                       // hashorstash=((MapsActivity)context).hashOrStash;
                       // time=((MapsActivity)context).Time;
                        time=((MapsActivity)context).getTime();
                        location=locationList.getLocatonName();
                        locationid=locationList.getLocationId();
                        longitude=locationList.getLocatonLon();
                        latitude=locationList.getLocatonLat();
                        userId=((MapsActivity)context).userId;

                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        String URL = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/" + userId + "/hashorstash";
                        //String URL = "http://192.168.0.10:8084/hashorstash/users/" + userId + "/hashorstash";
                        JSONObject param = new JSONObject();

                        param.put("comments", comment);
                        param.put("cmtTime", time);
                        param.put("location", location);
                        param.put("locationId", locationid);
                        param.put("latitude", longitude);
                        param.put("longitude", longitude);
                        param.put("duration", "120");
                        param.put("hashOrStash", "HASH");

                        final String requestBody = param.toString();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(context,response, Toast.LENGTH_LONG).show();
                                ((MapsActivity)context).dialogDismiss();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context,error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";
                            }

                            @Override
                            public byte[] getBody() throws AuthFailureError {
                                try {
                                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                                } catch (UnsupportedEncodingException uee) {
                                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                                    return null;
                                }
                            }

                            @Override
                            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                                try {
                                    String jsonString = new String(response.data,
                                            HttpHeaderParser.parseCharset(response.headers));
                                    return Response.success(jsonString,
                                            HttpHeaderParser.parseCacheHeaders(response));
                                } catch (UnsupportedEncodingException e) {
                                    return Response.error(new ParseError(e));
                                }
                            }
                        };

                        requestQueue.add(stringRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });



        }

        private Response.Listener<JSONObject> createRequestSuccessListener() {
            Response.Listener listener = new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                }
            };
            return listener;
        }
        private Response.ErrorListener createRequestErrorListener() {
            Response.ErrorListener err = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            };
            return err;
        }
    }


}
