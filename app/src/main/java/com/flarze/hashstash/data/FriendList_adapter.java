package com.flarze.hashstash.data;

import android.content.Context;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flarze.hashstash.R;
import com.flarze.hashstash.data.instagram_login.AppPreferences;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FriendList_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mcontext;
    List<Hash_List> friendList;
    View view;

    public FriendList_adapter(Context context, List<Hash_List> hashLists) {
        this.mcontext = context;
        this.friendList = hashLists;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendlist_single_row_design, parent, false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String imageValues = friendList.get(position).getFriendlist();
        String imageValue = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/" + imageValues;
        Picasso.with(mcontext).load(imageValue).into(((UserViewHolder) holder).friends_image);
        ((UserViewHolder) holder).friends_name.setText(friendList.get(position).getFriendsName());

    }

    @Override
    public int getItemCount() {
        return friendList == null ? 0 : friendList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView friends_image;
        TextView friends_name;

        public UserViewHolder(View itemView) {
            super(itemView);
            friends_image = itemView.findViewById(R.id.friends_image);
            friends_name = itemView.findViewById(R.id.friends_name);
        }


    }
}
