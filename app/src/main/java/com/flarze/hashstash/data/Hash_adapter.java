package com.flarze.hashstash.data;

import android.content.Context;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flarze.hashstash.R;
import com.flarze.hashstash.activity.MapsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Hash_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mcontext;
    List<Hash_List> hashList;
    View view;
    String  vote;

    public Hash_adapter(Context context, List<Hash_List> hashLists) {
        this.mcontext = context;
        this.hashList = hashLists;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hash_single_row_design, parent, false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {



        // Glide.with(mcontext).load(mcontext.getString(R.string.server_base_url_images) + hashList.get(position).getProfileImage()).into(((UserViewHolder) holder).circleImageView);
        ((UserViewHolder) holder).circleImageView.setImageResource(hashList.get(position).getImages());
        ((UserViewHolder) holder).hashcomment.setText(hashList.get(position).getHashcomment());
        ((UserViewHolder) holder).date.setText(hashList.get(position).getDate());
        ((UserViewHolder) holder).time.setText(hashList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return hashList == null ? 0 : hashList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView circleImageView;
        TextView hashcomment, date, time;
        ToggleButton button_favorite;
        String userId,hashId;


        public UserViewHolder(View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.profile_image_hash_row);
            hashcomment = itemView.findViewById(R.id.txt_hash_comment);
            date = itemView.findViewById(R.id.txt_date);
            time = itemView.findViewById(R.id.txt_time);
            userId=((MapsActivity)mcontext).userId;
            button_favorite = itemView.findViewById(R.id.button_favorite);
            button_favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Hash_List hash_lists = hashList.get(getLayoutPosition());
                    hashId = hash_lists.getHashId();
                    if (isChecked) {

                        String HttpUrl = "https://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/" + userId + "/hash-or-stash/" + hashId + "/hashes/votes";
                        RequestQueue requestQueue;
                        requestQueue = Volley.newRequestQueue(mcontext);
                        StringRequest stringRequest = new StringRequest(Request.Method.PUT, HttpUrl,
                                new com.android.volley.Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                    }
                                },
                                new com.android.volley.Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        Toast.makeText(mcontext, "" + volleyError, Toast.LENGTH_SHORT).show();
                                    }
                                });

                        // Creating RequestQueue.
                        requestQueue = Volley.newRequestQueue(mcontext);
                        // Adding the StringRequest object into requestQueue.
                        requestQueue.add(stringRequest);
                    }
                    else {
                        String HttpUrl = "https://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/" + userId + "/hash-or-stash/" + hashId + "/hashes/votes";
                        RequestQueue requestQueue;
                        requestQueue = Volley.newRequestQueue(mcontext);
                        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, HttpUrl,
                                new com.android.volley.Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                    }
                                },
                                new com.android.volley.Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        Toast.makeText(mcontext, "" + volleyError, Toast.LENGTH_SHORT).show();
                                    }
                                });

                        // Creating RequestQueue.
                        requestQueue = Volley.newRequestQueue(mcontext);
                        // Adding the StringRequest object into requestQueue.
                        requestQueue.add(stringRequest);
                    }
                }
            });


        }


    }
}
