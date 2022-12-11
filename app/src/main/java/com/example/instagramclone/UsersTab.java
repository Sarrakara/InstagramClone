package com.example.instagramclone;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


public class UsersTab extends Fragment implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {

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
        // An instance of this class will be a listener to the click of the items of the listView
        listView.setOnItemClickListener(UsersTab.this);

        listView.setOnItemLongClickListener(UsersTab.this);

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

    @Override
    // i is the position of the items inside the listView
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        /* when the user press on one of the items of the listView, we get the username
        from the parse server and  then pass it to the UsersPosts class */
        Intent intent = new Intent(getContext(), UsersPosts.class);
        /* we must specify the data type of objects(String) that we want to put inside the
        array list when we declare it above or it will show us an error */
        intent.putExtra("username", arrayList.get(i));
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        // give us the info that is specific to this username at position "i"
        parseQuery.whereEqualTo("username", arrayList.get(i));

        parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null && e == null){
                    /*
                    if(user.get("ProfileProfession") != null) {
                        FancyToast.makeText(getContext(), user.get("ProfileProfession") + "",
                                FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                    }else{
                        FancyToast.makeText(getContext()," No info", FancyToast.LENGTH_SHORT,
                                FancyToast.ERROR, false).show();
                    }*/

                    final PrettyDialog prettyDialog = new PrettyDialog(getContext());
                    prettyDialog.setTitle(user.getUsername() + "'s info")
                            .setMessage(user.get("ProfileBio") + "\n" +
                                        user.get("ProfileProfession" + "\n") +
                                        user.get("ProfileHobbies")+ "\n" +
                                        user.get("ProfileFavSport"))
                            .setIcon(R.drawable.person)
                            .setIconTint(R.color.purple_500)
                            .addButton(
                                    "OK", // button text
                                    R.color.white, // button textColor
                                    R.color.purple_500,  // button background color
                                    new PrettyDialogCallback() { // button onclickListener
                                        @Override
                                        public void onClick() {
                                            // do what you gotta do
                                            prettyDialog.dismiss();

                                        }
                                    }

                            ).show();
                }

            }
        });
        // if we want that the functionality "onItemLongClick" works, we have to return true
        /**if  you return true, that means that the event is consumed. It is handled.
         *  No other click events will be notified.And if the value is false that means event
         *  is not consumed, any other click event will be called(such as normal click).
         */
        return true;
    }
}