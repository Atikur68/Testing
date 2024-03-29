package com.flarze.hashstash.fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.flarze.hashstash.R;
import com.flarze.hashstash.activity.MapsActivity;
import com.flarze.hashstash.data.Hash_List;
import com.flarze.hashstash.data.Hash_adapter;
import com.flarze.hashstash.data.instagram_login.AppPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HashFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private Hash_adapter adapter;
    private List<Hash_List> hashLists = new ArrayList<>();
    private List<String> votedHashList = new ArrayList<>();
    View view;
    private String locationLatitude = "", locationLongitude = "", hashOrStash = "", userId = "", storeNames = "", hashStashId = "", hashStashlocation = "";
    private TextView storeName;
    private AppPreferences appPreferences = null;
    public MapsActivity mapsActivity;
    public SimpleDateFormat simpledateformat, simpleTimeformate;
    private Paint p = new Paint();
    private SwipeRefreshLayout swipeRefreshLayout;

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
        locationLatitude = bundle.getString("latitude");
        locationLongitude = bundle.getString("longitude");

        userId = bundle.getString("userId");
        hashOrStash = bundle.getString("hashOrStash");
        votedHashList = bundle.getStringArrayList("votedHashes");
        storeName = view.findViewById(R.id.storeName);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);


        if (hashOrStash.contains("hash")) {
            getActivity().setTitle("HashList");
        } else {
            getActivity().setTitle("StashList");
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.hash_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // cancel the Visual indication of a refresh
                swipeRefreshLayout.setRefreshing(false);
                getHashStashByLocation();
            }
        });


        getHashStashByLocation();

        enableSwipe();

        return view;
    }


    private void enableSwipe() {

        if (hashOrStash.contains("hash")) {

            ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    int position = viewHolder.getAdapterPosition();

                    if (direction == ItemTouchHelper.LEFT) {
                        int test =adapter.removeItem(viewHolder.getAdapterPosition(), hashLists.get(viewHolder.getAdapterPosition()).getHashId(),hashLists.get(viewHolder.getAdapterPosition()).getUserIdHashStash());
                        if(test==0){
                            getHashStashByLocation();
                        }

                    }
                }

                @Override
                public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                    Bitmap icon;
                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                        View itemView = viewHolder.itemView;
                        float height = (float) itemView.getBottom() - (float) itemView.getTop();
                        float width = height / 3;


                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.share_iconn);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);

                    }
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            };
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        } else {
            ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    int position = viewHolder.getAdapterPosition();

                    if (direction == ItemTouchHelper.LEFT) {
                        adapter.removeItem(viewHolder.getAdapterPosition(), hashLists.get(viewHolder.getAdapterPosition()).getHashId(),hashLists.get(viewHolder.getAdapterPosition()).getUserIdHashStash());

                    } else {
                        adapter.StashToHash(viewHolder.getAdapterPosition(), hashLists.get(viewHolder.getAdapterPosition()).getHashId(), hashStashlocation, hashLists.get(viewHolder.getAdapterPosition()).getHashStashlatitude(), hashLists.get(viewHolder.getAdapterPosition()).getHashStashlongitude(), hashLists.get(viewHolder.getAdapterPosition()).getHashcomment(),hashLists.get(viewHolder.getAdapterPosition()).getUserIdHashStash());

                    }
                }

                @Override
                public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                    Bitmap icon;
                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                        View itemView = viewHolder.itemView;
                        float height = (float) itemView.getBottom() - (float) itemView.getTop();
                        float width = height / 3;

                        if (dX > 0) {
                            p.setColor(Color.parseColor("#FFFFFF"));
                            RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                            c.drawRect(background, p);
                            icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_hash);
                            RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                            c.drawBitmap(icon, null, icon_dest, p);
                        } else {


                            p.setColor(Color.parseColor("#D32F2F"));
                            RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                            c.drawRect(background, p);
                            icon = BitmapFactory.decodeResource(getResources(), R.drawable.share_iconn);
                            RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                            c.drawBitmap(icon, null, icon_dest, p);
                        }
                    }
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            };
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }
    }

    public void getHashStashByLocation() {
        String HttpUrl = "";
        if (hashOrStash.contains("hash")) {
            HttpUrl = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/" + userId+ "/hash-or-stash/hash/locations/" + locationLatitude + "/" + locationLongitude;
        } else {
            HttpUrl = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/" + userId + "/hash-or-stash/stashes/locations/" + locationLatitude + "/" + locationLongitude;
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
                                int status = 0;

                                JSONObject hashStash = response.getJSONObject(i);
                                hashStashId = hashStash.getString("id");
                                String hashStashComments = hashStash.getString("comments");
                                String hashStashcmtTime = hashStash.getString("cmtTime");
                                hashStashlocation = hashStash.getString("location");
                                String hashStashlocationId = hashStash.getString("locationId");
                                String hashStashlatitude = hashStash.getString("latitude");
                                String hashStashlongitude = hashStash.getString("longitude");
                                String hashStashduration = hashStash.getString("duration");
                                String hashStashImage = hashStash.getString("hashImage");
                                String hashStashUserImage = hashStash.getString("image");
                                String hahsStashUserId = hashStash.getString("userId");


                                storeNames = hashStashlocation;

                                long num = Long.valueOf(hashStashcmtTime);
                                simpledateformat = new SimpleDateFormat("dd-MM-yyyy");
                                simpleTimeformate = new SimpleDateFormat("HH:mm:ss");

                                String time = simpleTimeformate.format(new Date(num * 1000L));
                                String date = simpledateformat.format(new Date(num * 1000L));

                                for (int v = 0; v < votedHashList.size(); v++) {
                                    if (hashStashId.equals(votedHashList.get(v))) {
                                        status = 1;
                                        break;
                                    } else
                                        status = 0;
                                }

                                if (status == 1)
                                    hashLists.add(new Hash_List(hashStashUserImage, hashStashId, hashStashComments, date, time, 1, hashStashImage, hahsStashUserId, hashStashlatitude, hashStashlongitude));
                                else
                                    hashLists.add(new Hash_List(hashStashUserImage, hashStashId, hashStashComments, date, time, 0, hashStashImage, hahsStashUserId, hashStashlatitude, hashStashlongitude));


                            }
                            storeName.setText(storeNames);
                            adapter = new Hash_adapter(getContext(), hashLists, userId, hashOrStash);
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


    @Override
    public void onRefresh() {
        getHashStashByLocation();
    }
}
