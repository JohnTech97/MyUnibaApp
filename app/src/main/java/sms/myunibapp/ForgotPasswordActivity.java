package sms.myunibapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;

import com.example.myunibapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import sms.myunibapp.resetPasswordEsito.NotSuccessfulEmailSent;
import sms.myunibapp.resetPasswordEsito.SuccessfulEmailSent;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailForPassword;
    Button mRecoveryButton;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailForPassword = findViewById(R.id.email_for_password_reset);
        mRecoveryButton = findViewById(R.id.reimposta_password);

        firebaseAuth = FirebaseAuth.getInstance(); //collegamento a firebase

        /* REIMPOSTA PASSWORD*/
        mRecoveryButton.setOnClickListener(v -> {

            String email = emailForPassword.getText().toString().trim();

            //Se il campo username è vuoto
            if (email.isEmpty()) {
                emailForPassword.setError("Inserisci username");
                emailForPassword.requestFocus();
                return;
            }

            firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    startActivity(new Intent(getApplicationContext(), SuccessfulEmailSent.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    startActivity(new Intent(getApplicationContext(), NotSuccessfulEmailSent.class));
                }
            });
        });
    }
}