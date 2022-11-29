package com.example.instagramclone;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;


public class ProfileTab extends Fragment {

    private EditText edtProfileName, edtProfileBio, edtProfileProfession, edtProfileHobbies,
            edtProfileFavSport;
    private Button btnUpdateInfo;

    public ProfileTab() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ProfileTab newInstance(String param1, String param2) {
        ProfileTab fragment = new ProfileTab();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);
        edtProfileName = view.findViewById(R.id.edtPrifileName);
        edtProfileBio = view.findViewById(R.id.edtProfileBio);
        edtProfileProfession = view.findViewById(R.id.edtProfileProfession);
        edtProfileHobbies = view.findViewById(R.id.edtProfileHobbies);
        edtProfileFavSport = view.findViewById(R.id.edtProfileFavSport);

        btnUpdateInfo = view.findViewById(R.id.btnUpdateInfo);
        final ParseUser parseUser = ParseUser.getCurrentUser();


        /*
        edtProfileName.setText(parseUser.get("ProfileName") == null  ?  "" :
                   parseUser.get("ProfileName").toString());*/

        if (parseUser.get("ProfileName") == null){
            edtProfileName.setText("");
        } else {
            // we used toString method because we are sur that ProfileBio is not null
            edtProfileName.setText(parseUser.get("ProfileName").toString());
        }

         if (parseUser.get("ProfileBio") == null){
             edtProfileBio.setText("");
         } else{
            // we used toString method because we are sur that ProfileBio is not null
             edtProfileBio.setText(parseUser.get("ProfileBio").toString());
         }

         if (parseUser.get("ProfileProfession") == null){
             edtProfileProfession.setText("");
         } else {
             edtProfileProfession.setText(parseUser.get("ProfileProfession").toString());
         }

         if (parseUser.get("ProfileHobbies") == null){
             edtProfileHobbies.setText("");
         }else{
             edtProfileHobbies.setText(parseUser.get("ProfileHobbies").toString());
         }

         if(parseUser.get("ProfileFavSport") == null){
             edtProfileFavSport.setText("");
         }else{
             edtProfileFavSport.setText(parseUser.get("ProfileFavSport").toString());
         }


        btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                parseUser.put("ProfileName", edtProfileName.getText().toString());
                parseUser.put("ProfileBio", edtProfileBio.getText().toString());
                parseUser.put("ProfileProfession", edtProfileProfession.getText().toString());
                parseUser.put("ProfileHobbies", edtProfileHobbies.getText().toString());
                parseUser.put("ProfileFavSport", edtProfileFavSport.getText().toString());

                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            FancyToast.makeText(getContext(), "Info Updated",
                                    FancyToast.LENGTH_LONG, FancyToast.SUCCESS,
                                    false).show();


                        }else{
                            FancyToast.makeText(getContext(), e.getMessage(), FancyToast.LENGTH_LONG,
                                    FancyToast.ERROR, false).show();
                        }
                    }
                });
            }
        });

        return view;
    }
}