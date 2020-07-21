package sms.myunibapp.forgotPassword;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.example.myunibapp.R;

public class NotSuccessfulEmailSent extends AppCompatActivity {

    /* ANIMAZIONE DEL DONE*/
    ImageView notDone;
    AnimatedVectorDrawableCompat animatedVectorDrawableCompat;
    AnimatedVectorDrawable animatedVectorDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.not_successful_email_sent);

        notDone = findViewById(R.id.not_done);
        Drawable drawable = notDone.getDrawable();

        if (drawable instanceof AnimatedVectorDrawableCompat) {
            animatedVectorDrawableCompat = (AnimatedVectorDrawableCompat) drawable;
            animatedVectorDrawableCompat.start();
        } else if (drawable instanceof AnimatedVectorDrawable) {
            animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
            animatedVectorDrawable.start();
        }

        TextView lblClickable = (TextView) findViewById(R.id.text_link);
        String htmlText = "Se hai problemi con l'accesso, puoi cliccare su <A HREF='https://www.uniba.it/studenti/servizi-informatici/posta-elettronica-e-servizi-associati'>AIUTAMI</A> per essere reinderizzato alla pagina di recupero delle credenziali di Esse3 ";
        lblClickable.setText(Html.fromHtml(htmlText));
        lblClickable.setMovementMethod(LinkMovementMethod.getInstance());

    }
}

