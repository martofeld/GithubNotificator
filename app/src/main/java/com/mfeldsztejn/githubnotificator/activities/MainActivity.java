package com.mfeldsztejn.githubnotificator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mfeldsztejn.githubnotificator.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openPullRequestActivity(View view) {

    }

    public void openMeActivity(View view) {
        startActivity(new Intent(this, MeActivity.class));
    }
}
