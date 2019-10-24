package com.flarze.hashstash.data;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flarze.hashstash.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

public class InviteFriendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    Context mcontext;
    List<FriendList> friendLists = new ArrayList<>();
    List<FriendList> newfriendLists = new ArrayList<>();
    View view;
    private InviteFriendAdapterOnClickListener listener;
    public InviteFriendAdapter(Context context, List<FriendList> friendLists) {
        this.mcontext=context;
        this.friendLists=friendLists;
        this.newfriendLists=friendLists;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invite_friend_row_design, parent, false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((UserViewHolder) holder).inviteFriendNumber.setText(friendLists.get(position).getFriendnumber());
        ((UserViewHolder) holder).invitefriendName.setText(friendLists.get(position).getInvitefriendName());
    }

    @Override
    public int getItemCount() {
        return friendLists.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    friendLists = newfriendLists;
                } else {

                    List<FriendList> filteredList = new ArrayList<>();

                    for (FriendList filteredlist : newfriendLists) {

                        if (filteredlist.getInvitefriendName().toLowerCase().contains(charString)) {

                            filteredList.add(filteredlist);
                        }
                    }

                    friendLists = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = friendLists;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                friendLists = (List<FriendList>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        TextView invitefriendName,inviteFriendNumber;
        CircularImageView mapLocationImage;
        LinearLayout container;
        TextView addbutton;

        public UserViewHolder(View itemView) {
            super(itemView);
            //container=itemView.findViewById(R.id.containerloc);
            inviteFriendNumber = itemView.findViewById(R.id.invitefriendNumber);
            invitefriendName = itemView.findViewById(R.id.invitefriendName);
            addbutton=itemView.findViewById(R.id.addButton);


            addbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(Intent.ACTION_VIEW);
                    intent.setType("vnd.android-dir/mms-sms");
                    intent.putExtra("address",friendLists.get(getLayoutPosition()).getFriendnumber());
                    intent.putExtra("sms_body","Hi Friend, Download this apps "+" https://www.google.com");
                    mcontext.startActivity(intent);


                }
            });

        }
    }



    public interface InviteFriendAdapterOnClickListener {
        public void onRowClicked(int position);
    }
}
