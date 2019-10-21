package com.flarze.hashstash.data;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flarze.hashstash.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Hash_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mcontext;
    List<Hash_List> hashList;
    View view;
    String vote, userId, hashOrStash, times, locationid, location, latitude, longitude;
    Calendar calander;

    public Hash_adapter(Context context, List<Hash_List> hashLists, String userId, String hashOrStash) {
        this.mcontext = context;
        this.hashList = hashLists;
        this.userId = userId;
        this.hashOrStash = hashOrStash;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hash_single_row_design, parent, false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (hashList.get(position).getVoteStatus() == 1) {
            ((UserViewHolder) holder).button_favorite.setChecked(true);
            //do not shake the hour glass
        }
        else {
            ((UserViewHolder) holder).button_favorite.setChecked(false);
            //shake the hour glass to indicate time is running out
            ((UserViewHolder) holder).sandClock.startAnimation(AnimationUtils.loadAnimation(mcontext, R.anim.shake));
        }

        // Glide.with(mcontext).load(mcontext.getString(R.string.server_base_url_images) + hashList.get(position).getProfileImage()).into(((UserViewHolder) holder).circleImageView);
        // ((UserViewHolder) holder).circleImageView.setImageResource(hashList.get(position).getImages());
        ((UserViewHolder) holder).hashcomment.setText(hashList.get(position).getHashcomment());
        ((UserViewHolder) holder).date.setText(hashList.get(position).getDate());
        ((UserViewHolder) holder).time.setText(hashList.get(position).getTime());

        String imageValues = hashList.get(position).getProfileImage();
        String imageValue = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/" + imageValues;
        Picasso.with(mcontext).load(imageValue).into(((UserViewHolder) holder).circleImageView);
    }

    @Override
    public int getItemCount() {
        return hashList == null ? 0 : hashList.size();
    }

    public int removeItem(int position, String stashId, String userHashStashId) {

        int tester = 0;

        if (userHashStashId.equals(userId)) {
            tester = 1;
            hashList.remove(position);
            notifyItemRemoved(position);
            DeleteStash(stashId, userId, userHashStashId);

        } else {
            tester = 0;
            Toast.makeText(mcontext, "You can delete only your own hash", Toast.LENGTH_LONG).show();
        }
        return tester;
    }

    public void DeleteStash(String stashId, String userId, String userHashStashId) {

        String HttpUrl = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/" + userId + "/hash-or-stash/" + stashId + "/stashes";
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
                        //  Toast.makeText(mcontext, "" + volleyError, Toast.LENGTH_SHORT).show();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(mcontext);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);
    }

    public void restoreItem(Hash_List item, int position) {
        hashList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public void StashToHash(int position, String stashId, String locations, String latitudes, String longitudes, String comment, String userHashStashId) {
        try {


            calander = Calendar.getInstance();
            times = String.valueOf(calander.getTimeInMillis() / 1000);
            locationid = "1234";

            RequestQueue requestQueue = Volley.newRequestQueue(mcontext);
            String HttpUrl = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/hash-or-stash/" + stashId;
            JSONObject param = new JSONObject();

            param.put("comments", comment);
            param.put("cmtTime", times);
            param.put("location", locations);
            param.put("locationId", locationid);
            param.put("latitude", latitudes);
            param.put("longitude", longitudes);
            param.put("duration", "120");
            param.put("hashOrStash", "HASH");

            final String requestBody = param.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.PUT, HttpUrl, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    final JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(response);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mcontext, error.getMessage(), Toast.LENGTH_LONG).show();
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
                protected com.android.volley.Response<String> parseNetworkResponse(NetworkResponse response) {
                    try {
                        String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        return com.android.volley.Response.success(jsonString,
                                HttpHeaderParser.parseCacheHeaders(response));
                    } catch (UnsupportedEncodingException e) {
                        return com.android.volley.Response.error(new ParseError(e));
                    }
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //  removeItem(position,stashId);

        hashList.remove(position);
        notifyItemRemoved(position);
    }

    private com.android.volley.Response.Listener<JSONObject> createRequestSuccessListener() {
        com.android.volley.Response.Listener listener = new com.android.volley.Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Toast.makeText(mcontext, response.toString(), Toast.LENGTH_LONG).show();
            }
        };
        return listener;
    }

    private com.android.volley.Response.ErrorListener createRequestErrorListener() {
        com.android.volley.Response.ErrorListener err = new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mcontext, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
        return err;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        CircularImageView circleImageView;
        TextView hashcomment, date, time;
        ToggleButton button_favorite;
        String userid, hashId;
        ImageView sandClock;
        public RelativeLayout viewForeground, viewBackground;


        public UserViewHolder(View itemView) {
            super(itemView);

            viewForeground = itemView.findViewById(R.id.forefround);
            circleImageView = itemView.findViewById(R.id.profile_image_hash_row);
            hashcomment = itemView.findViewById(R.id.txt_hash_comment);
            date = itemView.findViewById(R.id.txt_date);
            time = itemView.findViewById(R.id.txt_time);
            userid = userId;
            button_favorite = itemView.findViewById(R.id.button_favorite);
            sandClock = itemView.findViewById(R.id.sandClock);


            if (hashOrStash.contains("hash")) {
                button_favorite.setVisibility(VISIBLE);
                sandClock.setVisibility(VISIBLE);
            } else {
                button_favorite.setVisibility(GONE);
                sandClock.setVisibility(GONE);
            }


            hashcomment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(mcontext);
                    dialog.setContentView(R.layout.dialoglayout);
                    dialog.setTitle("Hello");

                    ImageView im = dialog.findViewById(R.id.imageViewHash);
                    // im.setImageResource(hashList.get(getLayoutPosition()).getHashImage());
                    if (mcontext != null && im != null) {
                        String imageValues = hashList.get(getLayoutPosition()).getHashStashImage();
                        String imageValue = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/" + imageValues;
                        Picasso.with(mcontext).load(imageValue).into(im);
                    }
                    ImageView btn = dialog.findViewById(R.id.share_shot);
                    final LinearLayout layout = dialog.findViewById(R.id.dialogLayoutHashImage);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            takeScreenshot(ScreenshotType.CUSTOM, v, layout);
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });

            button_favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (hashList.get(getLayoutPosition()).getUserIdHashStash().equals(userId))
//                        button_favorite.setEnabled(false);
//                    else {
                    Hash_List hash_lists = hashList.get(getLayoutPosition());
                    hashId = hash_lists.getHashId();
                    if (isChecked) {
                        button_favorite.setVisibility(VISIBLE);
                        if (hashList.get(getLayoutPosition()).getUserIdHashStash().equals(userId)) {
                            button_favorite.setVisibility(GONE);
                        }
                        // else {
                        // button_favorite.setEnabled(true);
                        String HttpUrl = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/" + userId + "/hash-or-stash/" + hashId + "/hashes/votes";
                        RequestQueue requestQueue;
                        requestQueue = Volley.newRequestQueue(mcontext);
                        StringRequest stringRequest = new StringRequest(Request.Method.PUT, HttpUrl,
                                new com.android.volley.Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        // Toast.makeText(mcontext, "Successful", Toast.LENGTH_SHORT).show();
                                    }
                                },
                                new com.android.volley.Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        //  Toast.makeText(mcontext, "" + volleyError, Toast.LENGTH_SHORT).show();
                                    }
                                });

                        // Creating RequestQueue.
                        requestQueue = Volley.newRequestQueue(mcontext);
                        // Adding the StringRequest object into requestQueue.
                        requestQueue.add(stringRequest);
                        // }


                    } else {
                        button_favorite.setVisibility(VISIBLE);
                        if (hashList.get(getLayoutPosition()).getUserIdHashStash().equals(userId)) {
                            button_favorite.setVisibility(GONE);
                        }
                        // button_favorite.setEnabled(true);
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
                                        //  Toast.makeText(mcontext, "" + volleyError, Toast.LENGTH_SHORT).show();
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

        private void takeScreenshot(ScreenshotType screenshotType, View view, LinearLayout layout) {
            Bitmap b = null;
            View rootContent = layout;
            switch (screenshotType) {
                case CUSTOM:
                    //fullPageScreenshot.setVisibility(View.INVISIBLE);//set the visibility to INVISIBLE of first button
                    //hiddenText.setVisibility(View.VISIBLE);//set the visibility to VISIBLE of hidden text

                    b = ScreenshotUtils.getScreenShot(rootContent);


                    //fullPageScreenshot.setVisibility(View.VISIBLE);//set the visibility to VISIBLE of first button again
                    //hiddenText.setVisibility(View.INVISIBLE);//set the visibility to INVISIBLE of hidden text
                    break;
            }

            //If bitmap is not null
            if (b != null) {

                File saveFile = ScreenshotUtils.getMainDirectoryName(mcontext);//get the path to save screenshot
                File file = ScreenshotUtils.store(b, "screenshot" + screenshotType + ".jpg", saveFile);//save the screenshot to selected path
                shareScreenshot(file, view);//finally share screenshot
            } else {
                Toast.makeText(mcontext, R.string.screenshot_take_failed, Toast.LENGTH_SHORT).show();
            }


        }

        /*  Show screenshot Bitmap */


        /*  Share Screenshot  */
        private void shareScreenshot(File file, View view) {
            Uri uri = Uri.fromFile(file);//Convert file path into Uri for sharing
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setType("image/*");
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
            intent.putExtra(android.content.Intent.EXTRA_TEXT, view.getResources().getString(R.string.sharing_text));
            intent.putExtra(Intent.EXTRA_STREAM, uri);//pass uri here

            view.getContext().startActivity(Intent.createChooser(intent, view.getResources().getString(R.string.share_title)));
        }


    }
}
