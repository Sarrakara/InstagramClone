package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    // UI components
    EditText edtLoginEmail, edtLoginPassword;
    Button btnLogin;
    TextView txtSignupActivity;
    ConstraintLayout rootLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        edtLoginPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                // if the key that is pressed is the enter key
                if(keyCode == keyEvent.KEYCODE_ENTER &&
                        // if there is an action down on this key
                        keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    // btn sign up is a view
                    onClick(btnLogin);
                }
                return false;
            }
        });
        btnLogin = findViewById(R.id.btnLogin);
        txtSignupActivity = findViewById(R.id.txtSignupActivity);

        btnLogin.setOnClickListener(LoginActivity.this);
        txtSignupActivity.setOnClickListener(LoginActivity.this);

        if(ParseUser.getCurrentUser() != null){
            ParseUser.getCurrentUser().logOut();

        }

        rootLayout = findViewById(R.id.rootLayout);
        rootLayout.setOnClickListener(LoginActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnLogin:
                if(edtLoginEmail.getText().toString().equals("") ||
                   edtLoginPassword.getText().toString().equals("")){

                    FancyToast.makeText(this, "Email, Password is required!",
                            FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
                }else {

                    ParseUser.logInInBackground(edtLoginEmail.getText().toString(),
                            edtLoginPassword.getText().toString(), new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {
                                    if (user != null && e == null) {
                                        FancyToast.makeText(LoginActivity.this, user.getUsername()
                                                        + " is logged in", FancyToast.LENGTH_LONG, FancyToast.SUCCESS,
                                                false).show();

                                        edtLoginEmail.setText("");
                                        edtLoginPassword.setText("");
                                        transitionToSocialMediaActivity();

                                    } else {

                                    }
                                }
                            });
                }

            break;
            case R.id.txtSignupActivity:
                Intent intent = new Intent(LoginActivity.this, SignUp.class);
                startActivity(intent);
                break;

            case R.id.rootLayout:
                hideKeyboard(rootLayout);
                break;
        }
    }
    public void hideKeyboard(View view){

        InputMethodManager inputMethodManager = (InputMethodManager)
                getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);

    }

    private void transitionToSocialMediaActivity(){
        Intent intent = new Intent(LoginActivity.this, SocialMediaActivity.class);
        startActivity(intent);
    }
}