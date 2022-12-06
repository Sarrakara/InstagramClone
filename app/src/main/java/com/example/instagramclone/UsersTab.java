package com.example.instagramclone;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class UsersTab extends Fragment {

    private ListView listView;
    private ArrayList <String> arrayList;
    private ArrayAdapter arrayAdapter;


    public UsersTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_users_tab, container, false);

        listView = view.findViewById(R.id.listView);
        // initialize the array list
        arrayList = new ArrayList();
        // initialize the array adapter
        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, arrayList);
        TextView txtLoadingUsers = view.findViewById(R.id.txtLoadingUsers);

        ParseQuery <ParseUser> parseQuery = ParseUser.getQuery();
        // get all the user's username except the current user
        // we don't want to query the username of the current user (not get it from the server)
        parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        // we use find in background because we want to retrieve a list
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null){
                    if(objects.size() > 0){
                        for(ParseUser object : objects ){

                            arrayList.add(object.getUsername());
                        }

                       listView.setAdapter(arrayAdapter);
                       txtLoadingUsers.animate().alpha(0).setDuration(500);
                       listView.setVisibility(View.VISIBLE);
                       // because we already set it to gone in the xml file

                    }
                }

            }
        });
        return view;
    }
}