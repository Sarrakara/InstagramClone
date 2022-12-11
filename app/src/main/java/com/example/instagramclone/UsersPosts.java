package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class UsersPosts extends AppCompatActivity {

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_posts);

        linearLayout = findViewById(R.id.linearLayout);

        Intent receivedIntentObject = getIntent();
        String receivedUsername = receivedIntentObject.getStringExtra("username");
        FancyToast.makeText(this, receivedUsername, FancyToast.LENGTH_SHORT,
                FancyToast.INFO, false).show();

        // specify the title of the action bar of this activity
        setTitle(receivedUsername + "'s posts");
        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Photo");
        // give us where the "username" is equalTo "receivedUsername"
        parseQuery.whereEqualTo("username", receivedUsername);
        // from the older to the newest photo
        parseQuery.orderByAscending("createdAt");

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading...");
        progressDialog.show();

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                // confirm that there is a valid object
                if(objects.size() > 0 && e == null){

                    // iterate over the objects that we get from the server
                    for (ParseObject post : objects){

                        // get the description from the server
                        TextView postDescription = new TextView(UsersPosts.this);
                        postDescription.setText(post.get("image_description") + "");

                        // get the image file from the server
                        ParseFile postPicture = (ParseFile) post.get("picture");
                        postPicture.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                // make sur that we get a valid data
                                if(data != null && e == null){
                                    // decode the array of data and convert it to a bitmap
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data,
                                            0, data.length);
                                    // create a new object of type imageView
                                    ImageView postImageView = new ImageView(UsersPosts.this);
                                    // specify the parameters of the imageView
                                    LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams
                                            (ViewGroup.LayoutParams.MATCH_PARENT, 1500);
                                    // specify the margins of the imageView
                                    imageViewParams.setMargins(30,5,30,5);
                                    // specifying these params to the image view
                                    postImageView.setLayoutParams(imageViewParams);
                                    postImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                    // get the image then put it inside the imageView
                                    postImageView.setImageBitmap(bitmap);


                                    //specify the parameters of the postDescription
                                    LinearLayout.LayoutParams desParams = new LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    desParams.setMargins(30,0,30,0);
                                    postDescription.setLayoutParams(desParams);
                                    postDescription.setGravity(Gravity.CENTER);
                                    postDescription.setBackgroundColor(getResources().getColor(R.color.purple_500));
                                    postDescription.setTextColor(Color.WHITE);
                                    postDescription.setTextSize(20f);
                                    postDescription.setPadding(4,10,4,10);

                                    // the order is important when we put object inside linear layout
                                    // the 1st is in the top
                                    linearLayout.addView(postImageView);
                                    linearLayout.addView(postDescription);


                                }
                            }
                        });
                    }
                    //if the list that we have from server has no object
                }else{
                    FancyToast.makeText(UsersPosts.this, receivedUsername + " does not have any posts!",
                            FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
                    finish();
                }

                progressDialog.dismiss();
            }
        });
    }

}