package com.example.innovathon21;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Splash_Screen extends AppCompatActivity {

    Thread timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);

        timer = new Thread(){
            @Override
            public void run() {
                try {
                    synchronized (this){
                        wait(5000);
                    }
                } catch (InterruptedException e){
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(Splash_Screen.this, Intro_Activity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();
    }
}