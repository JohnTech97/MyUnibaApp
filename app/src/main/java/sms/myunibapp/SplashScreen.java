package sms.myunibapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.myunibapp.R;

import java.util.Locale;

public class SplashScreen extends AppCompatActivity {

    private ImageView imageView; //logo dell'app
    private final int SPLASH_DELAY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        /*
        IMPOSTAZIONE LINGUA PER L'APPLICAZIONE
         */
        SharedPreferences pref = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String system=getResources().getStringArray(R.array.language_items)[0];//conterrà una stringa che rappresenta una parola di default per la lingua di sistema
        String lingua=pref.getString("Language", system); //in caso in cui non venga trovato nulla, cioè tipicamente al primo accesso
        Locale locale=null;
        if(lingua.equals(system)){
            locale= Resources.getSystem().getConfiguration().locale;//lingua impostata dal sistema, non la lingua scelta nell'app
        }else{
            locale=new Locale(lingua);
        }
        Configuration c=new Configuration();
        c.locale=locale;
        Resources res= getBaseContext().getResources();
        res.updateConfiguration(c,res.getDisplayMetrics());

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