package com.flarze.hashstash.fragment;

import android.Manifest;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flarze.hashstash.R;
import com.flarze.hashstash.data.FriendList;
import com.flarze.hashstash.data.InviteFriendAdapter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;


public class InviteFriend extends Fragment {
    RecyclerView invitefriendrecyclerView;
    InviteFriendAdapter inviteFriendAdapter;
    List<FriendList> invitefriendLists = new ArrayList<>();
    SearchView searchView;
    View view;
    Cursor phones;
    private boolean isPermission;


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

        if (requestSinglePermission()) {
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

        }

        inviteFriendAdapter.notifyDataSetChanged();

        return view;
    }

    private boolean requestSinglePermission() {

        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.READ_CONTACTS)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        //Single Permission is granted
                        //  Toast.makeText(MapsActivity.this, "Single permission is granted!", Toast.LENGTH_SHORT).show();
                        isPermission = true;
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            isPermission = false;
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

        return isPermission;

    }



}
