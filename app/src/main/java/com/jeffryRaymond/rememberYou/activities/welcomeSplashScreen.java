package com.jeffryRaymond.rememberYou.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.jeffryRaymond.rememberYou.R;


/**
 * This activity deals with displaying of the logo.
 */

public class welcomeSplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_splash_screen);
        ImageView rememberYouLogoImageView = findViewById(R.id.rememberYouLogo);
        Animation animation = AnimationUtils.loadAnimation(welcomeSplashScreen.this, R.anim.my_transition);
        rememberYouLogoImageView.startAnimation(animation);
        final Intent intent = new Intent(welcomeSplashScreen.this, LoginScreenActivity.class);
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();
    }
}
