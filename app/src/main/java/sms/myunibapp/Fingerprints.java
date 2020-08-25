package sms.myunibapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myunibapp.R;
import com.google.firebase.auth.FirebaseAuth;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;

import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.Executor;

import sms.myunibapp.forgotPassword.ForgotPasswordActivity;

public class Fingerprints extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(); //collegamento a firebase;
    ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprints);

        TextView msg_txt = findViewById(R.id.text_fingerprint);
        Button buttonCredentials = findViewById(R.id.button_insertCredentials);
        progressBar = findViewById(R.id.progress_login);

        //Creo il BiometricManager e controllo che l'utente possa usare il sensore oppure no
        BiometricManager biometricManager = BiometricManager.from(this);

        //se c'è l'hardware dedicato, esistono impronte registrate, e sono abilitate nelle impostazioni
        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {

            //Ora dobbiamo controllare se siamo autorizzati ad usare il sensore biometrico, creiamo il DialogBoxBiometric
            Executor executor = ContextCompat.getMainExecutor(this);
            BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    buttonCredentials.setVisibility(View.GONE); //nascondo il bottone per accedere all'immissione delle credenziali
                    progressBar.setVisibility(View.VISIBLE);  //faccio vedere la progress bar

                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                }
            });

            Resources res = getResources();//localizzazione
            biometricPrompt.authenticate(new BiometricPrompt.PromptInfo.Builder()
                    .setTitle(res.getString(R.string.fingerPrintDialogTitle))
                    .setNegativeButtonText(res.getString(R.string.fingerPrintDialogNegativeButton))
                    .setDescription(res.getString(R.string.fingerPrintDialogDescription)).build());

            //se non c'è l'hardware dedicato, oppure non esistono impronte registrate o non sono abilitate nelle impostazioni
        } else if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
                || biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE) {
            //inserimento manuale delle credenziali
                buttonCredentials.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Login.class)));
        }

    }
}