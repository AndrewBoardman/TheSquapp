package com.example.thesquapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.SignOutOptions;

public class StartScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
    }

    public void seePlayersBtn(View view){
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void seeChallengesBtn(View view){
        final Intent intent = new Intent(this, SeeChallengesActivity.class);
        startActivity(intent);
    }

    public void signOutBtn(View view){

        final Intent intent = new Intent(this, AuthenticatorActivity.class);

        AWSMobileClient.getInstance().signOut(SignOutOptions.builder().signOutGlobally(true).build(), new Callback<Void>() {
            @Override
            public void onResult(final Void result) {
                startActivity(intent);
            }
            @Override
            public void onError(Exception e) {
            }
        });
    }

    public void selectProfilePic(View view){
        final Intent intent = new Intent(this, ChoosePhotoActivity.class);
        startActivity(intent);
    }
}
