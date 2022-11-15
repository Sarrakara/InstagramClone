package com.example.instagramclone;

import android.app.Application;
import com.parse.Parse;



public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("oxtoFIqbABKtLCdqCj7q4stt2gdwswnlWWNad0zc")
                // if defined
                .clientKey("4mNjreWCRMNwYohp07VQMEFIpXViZRHIhjZrVVsZ")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
