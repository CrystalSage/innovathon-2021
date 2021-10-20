package com.example.innovathon21;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class Intro_Activity extends AppCompatActivity {

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    //TextView intro_title;
    TabLayout tabIndicator;
    Button btnNext;
    int position = 0;
    Button btnGetStarted;
    Animation btnAnim;
    TextView tvSkip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_intro_);

        // ini views
        btnNext = findViewById(R.id.btn_next);
        //intro_title=findViewById(R.id.intro_title);
        btnGetStarted = findViewById(R.id.btn_get_started);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);
        tvSkip = findViewById(R.id.tv_skip);




        // when this activity is about to be launch we need to check if its openened before or not

        if (restorePrefData()) {
            Intent mainActivity = new Intent(getApplicationContext(), MainActivity2.class);
            startActivity(mainActivity);
            finish();
        }


        // fill list screen


        //YoYo.with(Techniques.FadeInUp).duration(10000).repeat(10).playOn(intro_title);
        final List<ScreenItem> mList = new ArrayList<>();
        boolean fresh_items = mList.add(new ScreenItem("WELCOME, USER!", "Making this world a better place, by Helpin' each other :)", R.drawable.helpinob));
        mList.add(new ScreenItem("HERE TO HELP YOU", "Do not let your weaknesses get in the way of your communication ❤", R.drawable.lastslide));
        mList.add(new ScreenItem("MINIMAL INTERFACE", "To connect seamlessly;\n With everyone, without limits", R.drawable.stepsslide));
        mList.add(new ScreenItem("ALMOST THERE!", "So put on a smile, because happiness is a good conversation 😊", R.drawable.secondlast));


        // setup viewpager
        screenPager = findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        screenPager.setAdapter(introViewPagerAdapter);

        // setup tablayout with viewpager

        tabIndicator.setupWithViewPager(screenPager);

        // Get Started button click listener

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(mainActivity);
                savePrefsData();
                finish();

            }
        });



        // next button click Listner

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = screenPager.getCurrentItem();
                if (position < mList.size()) {
                    position++;
                    screenPager.setCurrentItem(position);
                }

                if (position == mList.size() - 1) { // when we rech to the last screen

                    // TODO : show the GETSTARTED Button and hide the indicator and the next button

                    loaddLastScreen();
                }
            }
        });


        // skip button click listener
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenPager.setCurrentItem(mList.size());
            }
        });



        // tablayout add change listener
        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == mList.size() - 1) {
                    loaddLastScreen();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




    }


    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend", true);
        editor.commit();
    }


    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend", false);
        return isIntroActivityOpnendBefore;
    }

    // show the GETSTARTED Button and hide the indicator and the next button
    private void loaddLastScreen() {

        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tvSkip.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        // TODO : ADD an animation the getstarted button
        // setup animation
        btnGetStarted.setAnimation(btnAnim);
    }



}