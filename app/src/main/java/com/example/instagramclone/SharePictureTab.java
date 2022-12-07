package com.example.instagramclone;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;


public class SharePictureTab extends Fragment implements View.OnClickListener {

    private ImageView imgShare;
    private EditText edtDescription;
    Button btnShareImage;
    /** use it with multiple permissions
    private String[] permissions = { Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE }; */
    /* we declare it here to use it to share image in addition to
       imageActivityResultLauncher of getChosenImage method */
    Bitmap receivedImageBitmap;

    public SharePictureTab() {
        // Required empty public constructor
    }

    public static SharePictureTab newInstance(String param1, String param2) {
        SharePictureTab fragment = new SharePictureTab();

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
        View view =  inflater.inflate(R.layout.fragment_share_picture_tab, container, false);
        imgShare = view.findViewById(R.id.imgShare);
        edtDescription = view.findViewById(R.id.edtDescription);
        btnShareImage = view.findViewById(R.id.btnShareImage);

        imgShare.setOnClickListener(SharePictureTab.this);
        btnShareImage.setOnClickListener(SharePictureTab.this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgShare:
                // ask for runtime permission
                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {

                    getChosenImage();
                   // return;

                }else if (shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Permission needed")
                        .setMessage("This permission is needed to access the images")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
               // return;

                   /** for multiple permissions
                    if (!hasPermissions(permissions)){
                         multiplePermissionLauncher.launch(permissions); */
                }
                else {
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
                break;

            case R.id.btnShareImage:
                   // make sur that the user has chosen an image
                    if(receivedImageBitmap != null){
                        // must add a description or the user can't share the image
                        if(edtDescription.getText().toString().equals("")){
                            FancyToast.makeText(getContext(), "Error: you must specify a description",
                                    FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        }else{

                            /* convert the image to an array of byte to can upload it easily to
                              the server */
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            receivedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                            byte[] bytes = byteArrayOutputStream.toByteArray();
                            ParseFile parseFile = new ParseFile("img.png", bytes);
                            // create new class named Photo inside the parse server
                            ParseObject photoObject = new ParseObject("Photo");
                            // put the file (image) inside the parse object named Photo
                            photoObject.put("picture", parseFile);
                            photoObject.put("image_description", edtDescription.getText().toString());
                            // pass the username of the current user that has uploaded the image to the server
                            photoObject.put("username", ParseUser.getCurrentUser().getUsername());
                            final ProgressDialog progressDialog = new ProgressDialog(getContext());
                            progressDialog.setMessage("loading...");
                            progressDialog.show();
                            photoObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e == null){
                                        FancyToast.makeText(getContext(), "Done...", FancyToast.LENGTH_LONG,
                                                FancyToast.SUCCESS, false).show();
                                    }else{
                                        FancyToast.makeText(getContext(), "Unknown error: " + e.getMessage(),
                                                FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                                    }
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    }else{
                        FancyToast.makeText(getContext(), "Error: you must select an image",
                                FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }
                break;
        }
    }

    private void getChosenImage() {
       /* FancyToast.makeText(getContext(), "now we can access the images", FancyToast.LENGTH_LONG,
                FancyToast.SUCCESS, false).show(); */

        Intent imagePickerIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagesActivityResultLauncher.launch(imagePickerIntent);
    }

    ActivityResultLauncher<Intent> imagesActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.
                    StartActivityForResult(), result -> {

                        Intent data = result.getData();

                        if(data != null &&
                                result.getResultCode() == Activity.RESULT_OK);

                            try{
                                Uri selectedImage = data.getData();
                                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                                Cursor cursor = getActivity().getContentResolver().query(
                                        selectedImage, filePathColumn, null,
                                        null, null );
                                // access the first object inside the cursor
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                // get the string value of the columnIndex
                                // get the path of our image
                                String picturePath = cursor.getString(columnIndex);
                                // we close the cursor to not waste the resources of the device
                                cursor.close();
                                // decode the image and convert it to a bitmap
                                receivedImageBitmap = BitmapFactory.decodeFile(picturePath);
                                imgShare.setImageBitmap(receivedImageBitmap);

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                    });


    private ActivityResultLauncher<String> requestPermissionLauncher =
         registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                 result -> {
                     if (result){
                         // Permission is granted. Continue the action or workflow in your app.
                         getChosenImage();

                     }else{
                         // Explain to the user that the feature is unavailable because the
                         // feature requires a permission that the user has denied. At the
                         // same time, respect the user's decision. Don't link to system
                         // settings in an effort to convince the user to change their
                         // decision.
                     }
                 });
        }
   /** for multiple permissions
    private ActivityResultLauncher <String[]> multiplePermissionLauncher =
           registerForActivityResult(
                   new ActivityResultContracts.RequestMultiplePermissions(),
                   result -> {
                       boolean areAllGranted = true;
                       for(boolean b : result.values()){
                           areAllGranted = areAllGranted && b;
                       }

                       if(areAllGranted){
                           getChosenImage();
                       }
                   }); */

    /** for multiple permissions
    private boolean hasPermissions(String[] permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(getContext(), permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    Log.d("PERMISSIONS", "Permission is not granted: " + permission);
                    return false;
                }
                Log.d("PERMISSIONS", "Permission already granted: " + permission);
            }
            return true;
        }
        return false;
    }  */
