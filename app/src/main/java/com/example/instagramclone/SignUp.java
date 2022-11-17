package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private EditText edtName, edtPunchSpeed, edtPunchPower, edtKickSpeed, edtKickPower;
    private Button btnSave;
    private TextView txtGetData;
    private Button btnGetAllData;
    private String allKickBoxers;
    private Button btnSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        edtName = findViewById(R.id.edtName);
        edtPunchSpeed = findViewById(R.id.edtPunchSpeed);
        edtPunchPower = findViewById(R.id.edtPunchPower);
        edtKickSpeed = findViewById(R.id.edtKickSpeed);
        edtKickPower = findViewById(R.id.edtKickPower);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(SignUp.this);

        txtGetData = findViewById(R.id.txtGetData);
        btnGetAllData = findViewById(R.id.btnGetAllData);

        btnSwitch = findViewById(R.id.btnSwitch);


        txtGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("KickBoxer");
                parseQuery.getInBackground("4HTqXZyYvZ", new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if(object != null && e == null){
                            txtGetData.setText(object.get("name")+ " - " + "punchPower: " +
                                    object.get("punch_power"));
                            Toast.makeText(SignUp.this, "success 1", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        btnGetAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allKickBoxers = "";
                ParseQuery<ParseObject> queryAll = ParseQuery.getQuery("KickBoxer");
                // get all the objects where the punch_power is grater than 4000
                queryAll.whereGreaterThan("kick_power",4000);
                // we use findInBackground to get all object
                queryAll.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e == null ){
                            if(objects.size() > 0){

                                for(ParseObject kickBoxer : objects){
                                    allKickBoxers = allKickBoxers + kickBoxer.get("name") + "\n";
                                }

                                FancyToast.makeText(SignUp.this, allKickBoxers, FancyToast.LENGTH_LONG,
                                        FancyToast.SUCCESS, true).show();
                            }else {
                                FancyToast.makeText(SignUp.this, "no kickBoxer", FancyToast.LENGTH_LONG,
                                        FancyToast.ERROR,true).show();
                            }
                        }
                    }
                });
            }
        });

        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this,SignUpLoginActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onClick(View view) {

        try {
            ParseObject kickBoxer = new ParseObject("KickBoxer");
            kickBoxer.put("name", edtName.getText().toString());
            kickBoxer.put("punch_speed", Integer.parseInt(edtPunchSpeed.getText().toString()));
            kickBoxer.put("punch_power", Integer.parseInt(edtPunchPower.getText().toString()));
            kickBoxer.put("kick_Speed", Integer.parseInt(edtKickSpeed.getText().toString()));
            kickBoxer.put("kick_power", Integer.parseInt(edtKickPower.getText().toString()));
            kickBoxer.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        FancyToast.makeText(SignUp.this, kickBoxer.get("name") +
                                        " is saved successfully to the sever", FancyToast.LENGTH_LONG,
                                FancyToast.SUCCESS, true).show();
                        edtName.setText("");
                        edtKickPower.setText("");
                        edtKickSpeed.setText("");
                        edtPunchPower.setText("");
                        edtPunchSpeed.setText("");

                    } else {
                        FancyToast.makeText(SignUp.this, e.getMessage(), FancyToast.LENGTH_LONG,
                                FancyToast.ERROR, true).show();

                    }
                }
            });
        }catch (Exception e ){
            FancyToast.makeText(SignUp.this, e.getMessage(), FancyToast.LENGTH_LONG,
                    FancyToast.ERROR, true).show();
        }
    }
}