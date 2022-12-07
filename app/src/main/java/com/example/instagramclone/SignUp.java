package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    // UI components
    private EditText edtSignupEmail, edtSignupUsername, edtSignupPassword;
    private TextView txtLoginActivity;
    private Button btnSignup;
    private ConstraintLayout rootLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // set the title of the action bar
        // setTitle("Sign Up");

        edtSignupEmail = findViewById(R.id.edtSignupEmail);
        edtSignupUsername = findViewById(R.id.edtSignupUsername);
        edtSignupPassword = findViewById(R.id.edtSignupPassword);

        edtSignupPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                // if the key that is pressed is the enter key
                if(keyCode == keyEvent.KEYCODE_ENTER &&
                        // if there is an action down on this key
                keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    // btn sign up is a view
                    onClick(btnSignup);
                }
                return false;
            }
        });
        btnSignup = findViewById(R.id.btnSignup);
        txtLoginActivity = findViewById(R.id.txtLoginActivity);


        btnSignup.setOnClickListener(this);
        txtLoginActivity.setOnClickListener(this);

        // Here I'm simply making sure that the parse user is valid and logged in.
        // If the parse user is valid,
        if(ParseUser.getCurrentUser() != null){
           // transitionToSocialMediaActivity();
            ParseUser.getCurrentUser().logOut();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSignup:

                if(edtSignupEmail.getText().toString().equals("") ||
                        edtSignupUsername.getText().toString().equals("") ||
                        edtSignupPassword.getText().toString().equals("")){

                    FancyToast.makeText(this, "Email, Username, password is " +
                            "required!", FancyToast.LENGTH_LONG, FancyToast.INFO,
                            false).show();
                }else {
                    final ParseUser appUser = new ParseUser();
                    appUser.setEmail(edtSignupEmail.getText().toString());
                    appUser.setUsername(edtSignupUsername.getText().toString());
                    appUser.setPassword(edtSignupPassword.getText().toString());
                    // show a progress dialog when the process of signing is happening
                    ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Signing up " + edtSignupUsername.getText().toString());
                    progressDialog.show();

                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {

                                FancyToast.makeText(SignUp.this, appUser.getUsername()
                                                + "is Signed up", FancyToast.LENGTH_LONG, FancyToast.SUCCESS,
                                        false).show();
                                edtSignupEmail.setText("");
                                edtSignupUsername.setText("");
                                edtSignupPassword.setText("");
                                transitionToSocialMediaActivity();

                            } else {
                                FancyToast.makeText(SignUp.this, "there was an error " +
                                                e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR,
                                        false).show();

                            }
                            //when signing up process is finished then progress dialog is dismissed
                            progressDialog.dismiss();
                        }
                    });

                }
                break;
            case R.id.txtLoginActivity:
                finish();
                break;
        }
    }

    public void rootLayoutIsTappedInSignup(View view){

        // here we have used try catch to prevent the app to crash when the user taps on the
        // screen where there is no keyboard
        // the way that was used in login activity doesn't need try / catch block
        try{
            InputMethodManager inputMethodManager = (InputMethodManager)
                    getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void transitionToSocialMediaActivity(){
        Intent intent = new Intent(SignUp.this, SocialMediaActivity.class);
        startActivity(intent);
        /* finish the signup activity after the transition to socialMediaActivity (there will be
        no sign up activity) because se don't want the user to go back to the login activity
         when he presses on the back button*/
        finish();
    }
}