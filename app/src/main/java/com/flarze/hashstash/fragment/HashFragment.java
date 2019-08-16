package com.flarze.hashstash.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.flarze.hashstash.R;
import com.flarze.hashstash.activity.MapsActivity;
import com.flarze.hashstash.data.HashStashList;
import com.flarze.hashstash.data.Hash_List;
import com.flarze.hashstash.data.Hash_adapter;
import com.flarze.hashstash.data.instagram_login.AppPreferences;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HashFragment extends Fragment {

    private RecyclerView recyclerView;
    private Hash_adapter adapter;
    private List<Hash_List> hashLists = new ArrayList<>();
    View view;
    private String locationId = "1234", hashOrStash = "", userId = "";
    private TextView storeName;
    private AppPreferences appPreferences = null;
    public MapsActivity mapsActivity;

    public HashFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        mapsActivity = (MapsActivity) context;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().invalidateOptionsMenu();
        setHasOptionsMenu(true);
      //  mapsActivity = (MapsActivity) getActivity();
       // hashOrStash = mapsActivity.hashOrStash;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_hash, container, false);
        Bundle bundle = this.getArguments();

//        appPreferences = new AppPreferences(getContext());
//
        locationId=bundle.getString("locationId");

        userId=bundle.getString("userId");
        hashOrStash=bundle.getString("hashOrStash");

        getActivity().setTitle("HashList");

        storeName =  view.findViewById(R.id.storeName);
        recyclerView = (RecyclerView) view.findViewById(R.id.hash_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getHashStashByLocation();


        return view;
    }

    private void getHashStashByLocation() {
        String HttpUrl = "";
        if (hashOrStash.contains("HASH")) {
            HttpUrl = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/hash-or-stash/hashes/locations/" + locationId;
        } else {
            HttpUrl = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/" + userId + "/hash-or-stash/stashes/locations/" + locationId;
        }


        //Toast.makeText(this, HttpUrl, Toast.LENGTH_SHORT).show();

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, HttpUrl, (JSONArray) null, new Response.Listener<JSONArray>() {


                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            hashLists.clear();

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject hashStash = response.getJSONObject(i);
                                String hashStashId = hashStash.getString("id");
                                String hashStashComments = hashStash.getString("comments");
                                String hashStashcmtTime = hashStash.getString("cmtTime");
                                String hashStashlocation = hashStash.getString("location");
                                String hashStashlocationId = hashStash.getString("locationId");
                                String hashStashlatitude = hashStash.getString("latitude");
                                String hashStashlongitude = hashStash.getString("longitude");
                                String hashStashduration = hashStash.getString("duration");

                                storeName.setText(hashStashlocation);


                                hashLists.add(new Hash_List(R.drawable.demoman,hashStashId, hashStashComments, "5, Feb 2019", "08:29:12"));

                            }
                            adapter = new Hash_adapter(getContext(), hashLists);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            // pDialog.hide();
                            e.printStackTrace();
                        }


                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                // Toast.makeText(MapsActivity.this, "" + volleyError, Toast.LENGTH_SHORT).show();
                            }
                        });

        // Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(getContext());
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(jsArrayRequest);
    }

}
