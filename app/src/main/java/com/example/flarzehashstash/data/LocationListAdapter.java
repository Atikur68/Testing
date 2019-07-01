package com.example.flarzehashstash.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.flarzehashstash.R;
import com.example.flarzehashstash.activity.MapsActivity;

import java.util.ArrayList;
import java.util.List;

public class LocationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<LocationList> locationLists= new ArrayList<>();
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


        public TeamViewHolder(View itemView) {
            super(itemView);

            locationName=itemView.findViewById(R.id.locationName);
            locationImage=itemView.findViewById(R.id.locationImage);

            locationName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MapsActivity)context).dialogDismiss();
                }
            });

        }
    }
}
