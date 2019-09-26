package com.flarze.hashstash.data;

import android.content.Context;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flarze.hashstash.R;
import com.flarze.hashstash.activity.MapsActivity;
import com.flarze.hashstash.activity.UserProfileActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Hash_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mcontext;
    List<Hash_List> hashList;
    View view;
    String vote, userId;

    public Hash_adapter(Context context, List<Hash_List> hashLists, String userId) {
        this.mcontext = context;
        this.hashList = hashLists;
        this.userId = userId;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hash_single_row_design, parent, false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (hashList.get(position).getVoteStatus() == 1)
            ((UserViewHolder) holder).button_favorite.setChecked(true);
        else
            ((UserViewHolder) holder).button_favorite.setChecked(false);

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
        CircularImageView circleImageView;
        TextView hashcomment, date, time;
        ToggleButton button_favorite;
        String userid, hashId;


        public UserViewHolder(View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.profile_image_hash_row);
            hashcomment = itemView.findViewById(R.id.txt_hash_comment);
            date = itemView.findViewById(R.id.txt_date);
            time = itemView.findViewById(R.id.txt_time);
            userid = userId;
            button_favorite = itemView.findViewById(R.id.button_favorite);
            button_favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Hash_List hash_lists = hashList.get(getLayoutPosition());
                    hashId = hash_lists.getHashId();
                    if (isChecked) {

                        String HttpUrl = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/" + userId + "/hash-or-stash/" + hashId + "/hashes/votes";
                        RequestQueue requestQueue;
                        requestQueue = Volley.newRequestQueue(mcontext);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl,
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

//                        Gson gson = new GsonBuilder()
//                                .setLenient()
//                                .create();
//
//                        Retrofit retrofit = new Retrofit.Builder()
//                                .baseUrl(HttpUrl)
//                                .addConverterFactory(GsonConverterFactory.create(gson)) //Here we are using the GsonConverterFactory to directly convert json data to object
//                                .build();
//                        Api api = retrofit.create(Api.class);
//
//                        // finally, execute the request
//                        Call<String> call = api.VoteForHash();
//                        call.enqueue(new Callback<String>() {
//                            @Override
//                            public void onResponse(Call<String> call,
//                                                   retrofit2.Response<String> response) {
//
//                                // successMessage[0] = response.body();
//                                Toast.makeText(mcontext, ""+response, Toast.LENGTH_SHORT).show();
//                                //  SharedPref.write("user", mUser);
//                                Log.v("Upload", "success");
//                            }
//                            @Override
//                            public void onFailure(Call<String> call, Throwable t) {
//                                Log.e("Upload error:", t.getMessage());
//                            }
//
//                        });
                    } else {
                        String HttpUrl = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/" + userId + "/hash-or-stash/" + hashId + "/hashes/votes";
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
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
