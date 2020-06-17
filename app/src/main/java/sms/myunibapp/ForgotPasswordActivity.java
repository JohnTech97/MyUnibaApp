package sms.myunibapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myunibapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

   private EditText emailForPassword;
   Button mRecoveryButton;
   FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailForPassword = findViewById(R.id.email_for_password);
        mRecoveryButton = findViewById(R.id.reimposta_password);

        mRecoveryButton.setOnClickListener(v -> {

            String email = emailForPassword.getText().toString();

            //Se il campo username è vuoto
            if (email.isEmpty()) {
                emailForPassword.setError("Inserisci username");
                emailForPassword.requestFocus();
                return;
            }

            firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                            finish();
                        }
                    },3000);
                }
            });
        });





    }
}