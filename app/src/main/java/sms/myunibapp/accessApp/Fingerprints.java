package sms.myunibapp.accessApp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myunibapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;

import sms.myunibapp.FirebaseDb;
import sms.myunibapp.SessionManager;
import sms.myunibapp.principale.HomeActivity;
import sms.myunibapp.principale.ProfessorHome;

public class Fingerprints extends AppCompatActivity {

    public static final String EMAIL_UNIBA = "@studenti.uniba.it";

    private final int DELAY = 2000;

    private SessionManager session;
    private Button buttonCredentials;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(); //collegamento a firebase;
    private static String userName;
    //Operazioni per l'accesso

    ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprints);

        TextView msg_txt = findViewById(R.id.text_fingerprint);
        buttonCredentials = findViewById(R.id.button_insertCredentials);
        progressBar = findViewById(R.id.progress_login);

        // Creazione del nuovo manager di sessione
        session = new SessionManager(getApplicationContext());

        //Creo il BiometricManager e controllo che l'utente possa usare il sensore oppure no
        BiometricManager biometricManager = BiometricManager.from(this);
        progressBar.setVisibility(View.GONE);

        //se c'è l'hardware dedicato, esistono impronte registrate, e sono abilitate nelle impostazioni
        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            msg_txt.setText(R.string.text_fingerprint);

            //Ora dobbiamo controllare se siamo autorizzati ad usare il sensore biometrico, creiamo il DialogBoxBiometric
            Executor executor = ContextCompat.getMainExecutor(this);
            BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    SharedPreferences settings = getSharedPreferences("Settings", Activity.MODE_PRIVATE); //Acquisizione dei settaggi di sistema

                    String username = settings.getString("Email", "");
                    String password = settings.getString("Pass", "");

                    animationButton();
                    authentication(username,password);
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    msg_txt.setText("Riconoscimento FALLITO! Riprova di nuovo.");
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
            startActivity(new Intent(sms.myunibapp.accessApp.Fingerprints.this, LoginActivity.class));
        }
        buttonCredentials.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), LoginActivity.class)));
    }

    private void authentication(String mail, String pass) {
        String access = mail;

        if (!mail.contains("@")) {
            access = mail + EMAIL_UNIBA;
        }

        firebaseAuth.signInWithEmailAndPassword(access, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(sms.myunibapp.accessApp.Fingerprints.this, getResources().getString(R.string.login_success) + " " + session.getSessionEmail(), Toast.LENGTH_SHORT).show();

                    Class target = null;
                    if (mail.endsWith("@uniba.it")) {//verifico se l'utente sia uno studente o un professore
                        userName = mail.replace(".", "_");
                        target = ProfessorHome.class;
                    } else if (mail.endsWith("@studenti.uniba.it")) {
                        userName = mail.replace(".", "_");
                        target = HomeActivity.class;
                    } else {
                        userName = mail.concat(EMAIL_UNIBA).replace(".", "_");//perché a firebase da problemi il simbolo "."
                        target = HomeActivity.class;
                    }
                    session.createLoginSession(mail, pass, userName);
                    LoginActivity.setUsername(userName);
                    startActivity(new Intent(getApplicationContext(), target));
                    finish();
                } else {
                    Toast.makeText(sms.myunibapp.accessApp.Fingerprints.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void animationButton() {

        buttonCredentials = findViewById(R.id.button_insertCredentials);
        Animation fadingInAnimation = AnimationUtils.loadAnimation(this, R.anim.fragment_close_exit);
        fadingInAnimation.setDuration(2000);
        buttonCredentials.startAnimation(fadingInAnimation);
        buttonCredentials.setVisibility(View.INVISIBLE);

        new Handler().postDelayed(() -> {
            progressBar.setVisibility(View.VISIBLE);
        }, DELAY);
    }
}