package com.example.flarzehashstash.data;

import android.content.Context;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.flarzehashstash.R;

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


        public UserViewHolder(View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.profile_image_hash_row);
            hashcomment = itemView.findViewById(R.id.txt_hash_comment);
            date = itemView.findViewById(R.id.txt_date);
            time = itemView.findViewById(R.id.txt_time);


        }


    }
}
