package com.example.flarzehashstash.fragment;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.flarzehashstash.R;
import com.example.flarzehashstash.data.FriendList;
import com.example.flarzehashstash.data.InviteFriendAdapter;

import java.util.ArrayList;
import java.util.List;


public class InviteFriend extends Fragment {
    RecyclerView invitefriendrecyclerView;
    InviteFriendAdapter inviteFriendAdapter;
    List<FriendList> invitefriendLists = new ArrayList<>();
    SearchView searchView;
    View view;
    Cursor phones;


    public InviteFriend() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_invite_friend, container, false);
        getActivity().setTitle("Invite Friends");

        invitefriendrecyclerView = view.findViewById(R.id.invitefriendrecycle);
        invitefriendrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        invitefriendrecyclerView.setHasFixedSize(true);

        searchView = view.findViewById(R.id.searchnumber);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                inviteFriendAdapter.getFilter().filter(newText);
                return false;
            }
        });

        phones = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        if (phones != null) {
            Log.e("count", "" + phones.getCount());
            if (phones.getCount() == 0) {

            }

            while (phones.moveToNext()) {
                Bitmap bit_thumb = null;
                String id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


                invitefriendLists.add(new FriendList(name, phoneNumber));



            }
            inviteFriendAdapter=new InviteFriendAdapter(getContext(),invitefriendLists);
            invitefriendrecyclerView.setAdapter(inviteFriendAdapter);



        } else {
            Log.e("Cursor close 1", "----------------");
        }
        inviteFriendAdapter.notifyDataSetChanged();

        return view;
    }



}
