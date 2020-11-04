package com.uc.myfirebaseapss;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    protected void onStart() {
        super.onStart();
        mUser= mAuth.getCurrentUser();
        if(mUser!= null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent onBoard = new Intent(Splash.this, MainFragment.class);
                    onBoard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(onBoard);
                    finish();
                }
            }, 2500);
        }else if(mUser == null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent onBoard = new Intent(Splash.this, MainActivity.class);
                    onBoard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(onBoard);
                    finish();
                }
            }, 2500);
        }
    }
}