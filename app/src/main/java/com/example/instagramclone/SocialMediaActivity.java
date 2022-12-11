package com.example.instagramclone;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;

public class SocialMediaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private TabAdapter tabAdapter;

    private String[] titles = {"Profile", "Users", "Share Picture"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        setTitle("Social Media App");


        toolbar = findViewById(R.id.myToolbar);
        // set this toolbar as the action bar of our social media activity
        setSupportActionBar(toolbar);

        viewPager2 = findViewById(R.id.viewPager2);

        tabLayout = findViewById(R.id.tabLayout);

        tabAdapter = new TabAdapter(this);
        // to switch between tabs
        viewPager2.setAdapter(tabAdapter);
        // show the titles of our fragments
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> tab.setText(titles[position]))
                .attach();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /**
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu, menu); */
        // instead of the 2 lines above
        getMenuInflater().inflate(R.menu.my_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.postImageItem){
            /* allow to the user to capture an image from the device and upload it to the
            parse server or share it with others */

            // we have to request permission first
            if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.
                    READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ){

                captureImageRequestPermissionLauncher.launch
                        (Manifest.permission.READ_EXTERNAL_STORAGE);

            }else{
                captureImage();
            }

        }else if(item.getItemId() == R.id.logoutUserItem){

            ParseUser.getCurrentUser().logOut();
            finish();
            // to comeback to login activity when we logout
            Intent intent = new Intent(SocialMediaActivity.this, LoginActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    private void captureImage() {

        Intent captureImageIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        captureImageActivityResultLauncher.launch(captureImageIntent);

    }

    private ActivityResultLauncher<Intent> captureImageActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {

                    Intent data = result.getData();
                             if(data != null && result.getResultCode() == RESULT_OK ){

                                 try {
                                     // we use this way because we are inside an activity
                                     Uri capturedImage = data.getData();
                                     Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                             getContentResolver(), capturedImage);
                                     ByteArrayOutputStream byteArrayOutputStream =
                                             new ByteArrayOutputStream();

                                     bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                     byte[] bytes = byteArrayOutputStream.toByteArray();

                                     // upload the image to the server
                                     ParseFile parseFile = new ParseFile("img.png", bytes);
                                     ParseObject parseObject = new ParseObject("Photo");
                                     parseObject.put("picture", parseFile);
                                     parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                                     final ProgressDialog progressDialog = new ProgressDialog(this);
                                     progressDialog.setMessage("Loading...");
                                     progressDialog.show();
                                     parseObject.saveInBackground(new SaveCallback() {
                                         @Override
                                         public void done(ParseException e) {
                                             if(e == null) {
                                                 FancyToast.makeText(SocialMediaActivity.this,
                                                         "Picture uploaded", FancyToast.LENGTH_LONG,
                                                         FancyToast.SUCCESS, false).show();
                                             }else{
                                                 FancyToast.makeText(SocialMediaActivity.this,
                                                         "Unknown error" + e.getMessage(), FancyToast.LENGTH_LONG,
                                                         FancyToast.ERROR, false).show();
                                             }
                                             progressDialog.dismiss();
                                         }
                                     });

                                 }catch (Exception e){

                                     e.printStackTrace();
                                 }
                             }
                    });

    private ActivityResultLauncher<String> captureImageRequestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    result -> {

                        if(result) {
                            captureImage();
                        }

                    });



}
