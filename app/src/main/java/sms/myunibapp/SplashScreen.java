package sms.myunibapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.myunibapp.R;

public class SplashScreen extends AppCompatActivity {

    private ImageView imageView; //logo dell'app
    private final int SPLASH_DELAY = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        /*
            PER NASCONDERE LA BARRA DI NAVIGAZIONE
         */
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE
                // Hide the nav bar and status bar
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        /*
            PER ANIMARE IL LOGO
         */
        animateLogo();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, Login.class));
                finish();
            }
        },SPLASH_DELAY);
    }

    private void animateLogo() {
        imageView = findViewById(R.id.logoUniba);
        Animation fadingInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadingInAnimation.setDuration(SPLASH_DELAY);
        imageView.startAnimation(fadingInAnimation);
    }
}