package sms.myunibapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.myunibapp.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;

import sms.myunibapp.forgotPassword.ForgotPasswordActivity;

public class Login extends AppCompatActivity {

    static final String EMAIL_UNIBA = "@studenti.uniba.it";

    //Credenziali di accesso
    private EditText editTextUsername;
    private EditText editTextPassword;

    //Operazioni per l'accesso
    FirebaseAuth firebaseAuth;
    Button mLoginButton;

    //Variabile password dimenticata
    TextView forgotTextLink;

    //Ricordami
    CheckBox rememberMe;

    private static String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_login);

        initializeVariables(); //Inizializzazione delle variabili

        checkRemember();
        /*
        ACCESSO ALL'APPLICAZIONE
         */
        mLoginButton.setOnClickListener(v -> {

            validateUsername(); //Check Username

            validatePassword(); //Check Password

            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (!validateUsername() | !validatePassword()) {
                return;
            } else {
                authenticate(username, password);
            }
        });

        /*
        RECUPERO PASSWORD
         */
        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
            }
        });
    }

    private void checkRemember() {
        SharedPreferences pref = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        boolean ricorda = pref.getBoolean("Remember", false);
        if (ricorda) {
            rememberMe.setChecked(true);
            String mail, pass;
            mail = pref.getString("Email", "");
            pass = pref.getString("Pass", "");
            startAuthentication(mail, pass);
        }
    }

    private void startAuthentication(String m, String p) {
        //autenticazione con il sensore di impronte, se esiste l'hardware nello smartphone
        BiometricManager manager = BiometricManager.from(this);
        if (manager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) { //se c'è l'hardware dedicato ed esistono impronte registrate
            Executor ex = ContextCompat.getMainExecutor(this);
            BiometricPrompt fingerPrints = new BiometricPrompt(this, ex, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    //se l'autenticazione ha successo si procede al login su firebase
                    authenticate(m, p);
                }
            });
            fingerPrints.authenticate(new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Accesso biometrico per MyUnibApp")
                    .setSubtitle("Autenticazione con le impronte digitali")
                    .setNegativeButtonText("Annulla")
                    .setDescription("Puoi effettuare l'accesso all'applicazione con le dita, invece di inserire manualmente le tue credenziali").build());
        } else if (manager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
                || manager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE) {//se non ci sono impronte registrate nel sistema, o non esiste l'hardware
            editTextUsername.setText(m);
            editTextPassword.setText(p);
        }

    }

    private void authenticate(String mail, String pass) {
        firebaseAuth.signInWithEmailAndPassword(mail + EMAIL_UNIBA, pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
                editor.putBoolean("Remember", rememberMe.isChecked());
                editor.putString("Email", mail);
                editor.putString("Pass", pass);
                editor.apply();
                Toast.makeText(Login.this, "Login avvenuto con successo", Toast.LENGTH_SHORT).show();
                username=mail.concat(EMAIL_UNIBA).replace(".", "_");//perché a firebase da problemi il simbolo "."
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
            } else {
                Toast.makeText(Login.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public static String getUsername(){
        return username;
    }

    private void initializeVariables() {
        //Acquisizione credeziali per il login
        this.editTextUsername = findViewById(R.id.username);
        this.editTextPassword = findViewById(R.id.password);

        this.mLoginButton = findViewById(R.id.button_login);  //bottone per il login

        this.rememberMe = findViewById(R.id.remember);

        this.forgotTextLink = findViewById(R.id.lost_password);  //variabile per il reimposta password

        this.firebaseAuth = FirebaseAuth.getInstance(); //collegamento a firebase
    }

    private Boolean validateUsername() {
        boolean isCorrect = true; //variabile di controllo per stabilire se lo Username è corretto

        String username = editTextUsername.getText().toString().trim();
        TextInputLayout textInputLayout = findViewById(R.id.text_input_username);
        //Se il campo username è vuoto
        if (username.isEmpty()) {
            textInputLayout.setError("Inserisci username");
            editTextUsername.requestFocus();
            isCorrect = false;
        }

        return isCorrect;
    }

    private Boolean validatePassword() {
        boolean isCorrect = true; //variabile di controllo per stabilire se lo Password è corretta

        String password = editTextPassword.getText().toString().trim();
        TextInputLayout textInputLayout = findViewById(R.id.text_input_password);

        //Se la password è più corta di 6 caratteri
        if (password.length() < 6) {
            textInputLayout.setError("La password deve contenere almeno 6 caratteri");
            //editTextPassword.requestFocus();
            isCorrect = false;
        }
        return isCorrect;
    }

    @Override
    public void onBackPressed() {
        close();
    }

    private void close() {
        AlertDialog.Builder conferma = new AlertDialog.Builder(Login.this);
        conferma.setTitle("Conferma");
        conferma.setMessage("Sei sicuro di voler chiudere l'applicazione?");
        conferma.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        });
        conferma.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        conferma.show();
    }

}