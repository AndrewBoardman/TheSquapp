package com.example.thesquapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

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
}
