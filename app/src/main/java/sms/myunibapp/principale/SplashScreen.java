package sms.myunibapp.principale;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.myunibapp.R;

import java.util.Locale;

import sms.myunibapp.accessApp.LoginActivity;

public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_DELAY = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        /*
        IMPOSTAZIONE LINGUA PER L'APPLICAZIONE
         */
        SharedPreferences settings = getSharedPreferences("Settings", Activity.MODE_PRIVATE); //Acquisizione dei settaggi di sistema

        String system = getResources().getStringArray(R.array.language_abbreviations)[0];//conterrà una stringa che rappresenta una parola di default per la lingua di sistema
        String lingua = settings.getString("Language", system); //in caso in cui non venga trovato nulla, cioè tipicamente al primo accesso
        Locale locale = null;

        if (lingua.equals(system)) {
            locale = Resources.getSystem().getConfiguration().locale;//lingua impostata dal sistema, non la lingua scelta nell'app
        } else {
            locale = new Locale(lingua);
        }

        Configuration c = new Configuration();
        c.locale = locale;
        Resources res = getBaseContext().getResources();
        res.updateConfiguration(c, res.getDisplayMetrics());

        /*
            PER NASCONDERE LA BARRA DI NAVIGAZIONE
         */
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE
                // Hide the nav bar and status bar
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        new Handler().postDelayed(() -> {

            if (!settings.contains("Email")){
                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
            } else if (settings.getBoolean("fingerprintsEnabled",true) && settings.getBoolean("Remember", false)) {
                startActivity(new Intent(SplashScreen.this, sms.myunibapp.accessApp.Fingerprints.class));
            }else{
                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
            }
            finish();

        }, SPLASH_DELAY);

    }

}