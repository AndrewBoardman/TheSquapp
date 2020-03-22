package com.example.thesquapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;

public class AuthCheck extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_check);

        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {

                    @Override
                    public void onResult(UserStateDetails userStateDetails) {
                        authCheck();
                    }
                    @Override
                    public void onError(Exception e) {
                        Log.e("INIT", "Initialization error.", e);
                    }
                }
        );


    }

    private void authCheck(){

        final Intent intent = new Intent(this, AuthenticatorActivity.class);
        final Intent intent1 = new Intent(this, StartScreen.class);

        if(!AWSMobileClient.getInstance().isSignedIn()){
            startActivity(intent);
        } else {
            startActivity(intent1);
       }
    }
}
