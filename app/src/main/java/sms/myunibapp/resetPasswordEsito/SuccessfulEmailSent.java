package sms.myunibapp.resetPasswordEsito;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.example.myunibapp.R;

import sms.myunibapp.Login;

public class SuccessfulEmailSent extends AppCompatActivity {

    /* ANIMAZIONE DEL DONE*/
    ImageView done;
    AnimatedVectorDrawableCompat animatedVectorDrawableCompat;
    AnimatedVectorDrawable animatedVectorDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.successful_email_reset);

        done = findViewById(R.id.done);

        Drawable drawable = done.getDrawable();

        if (drawable instanceof AnimatedVectorDrawableCompat) {
            animatedVectorDrawableCompat = (AnimatedVectorDrawableCompat) drawable;
            animatedVectorDrawableCompat.start();
        } else if (drawable instanceof AnimatedVectorDrawable) {
            animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
            animatedVectorDrawable.start();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SuccessfulEmailSent.this, Login.class));
                finish();
            }
        }, 10000);

    }
}