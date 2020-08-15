package sms.myunibapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
    private FirebaseAuth firebaseAuth;
    private Button mLoginButton;

    //Variabile password dimenticata
    private TextView forgotTextLink;

    //Ricordami
    private CheckBox rememberMe;

    private static String username;
    private String password;

    private SharedPreferences pref;

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
        boolean isFingerprintSensorEnabled = pref.getBoolean("fingerprintsEnabled", false);
        if (manager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS && isFingerprintSensorEnabled) { //se c'è l'hardware dedicato, esistono impronte registrate, e sono abilitate nelle impostazioni
            Executor ex = ContextCompat.getMainExecutor(this);
            BiometricPrompt fingerPrints = new BiometricPrompt(this, ex, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    //se l'autenticazione ha successo si procede al login su firebase
                    authenticate(m, p);
                }
            });
            Resources res = getResources();//localizzazione
            fingerPrints.authenticate(new BiometricPrompt.PromptInfo.Builder()
                    .setTitle(res.getString(R.string.fingerPrintDialogTitle))
                    .setNegativeButtonText(res.getString(R.string.fingerPrintDialogNegativeButton))
                    .setDescription(res.getString(R.string.fingerPrintDialogDescription)).build());
        } else if (manager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
                || manager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE
                || !isFingerprintSensorEnabled) {//se non ci sono impronte registrate nel sistema, non esiste l'hardware, o non viene abilitato nelle impostazioni
            editTextUsername.setText(m);
            editTextPassword.setText(p);
        }

    }

    private void authenticate(String mail, String pass) {
        String access=mail;
        if(!mail.contains("@")){
            access=mail+EMAIL_UNIBA;
        }
        firebaseAuth.signInWithEmailAndPassword(access, pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("Remember", rememberMe.isChecked());
                editor.putString("Email", mail);
                editor.putString("Pass", pass);
                editor.apply();
                Toast.makeText(Login.this, getResources().getString(R.string.login_success) + " " + mail, Toast.LENGTH_SHORT).show();
                Class target = null;
                if (mail.endsWith("@uniba.it")) {//verifico se l'utente sia uno studente o un professore
                    username = mail.replace(".", "_");
                    target = ProfessorHome.class;
                } else if (mail.endsWith("@studenti.uniba.it")) {
                    username = mail.replace(".", "_");
                    target = Home.class;
                } else {
                    username = mail.concat(EMAIL_UNIBA).replace(".", "_");//perché a firebase da problemi il simbolo "."
                    target = Home.class;
                }
                startActivity(new Intent(getApplicationContext(), target));
                finish();
            } else {
                Toast.makeText(Login.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public static String getUsername() {
        return username;
    }

    private void initializeVariables() {
        //Acquisizione credeziali per il login
        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);
        mLoginButton = findViewById(R.id.button_login);  //bottone per il login
        rememberMe = findViewById(R.id.remember);
        forgotTextLink = findViewById(R.id.lost_password);  //variabile per il reimposta password
        firebaseAuth = FirebaseAuth.getInstance(); //collegamento a firebase
        pref = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
    }

    private Boolean validateUsername() {
        boolean isCorrect = true; //variabile di controllo per stabilire se lo Username è corretto

        username = editTextUsername.getText().toString().trim();
        TextInputLayout textInputLayout = findViewById(R.id.text_input_username);
        //Se il campo username è vuoto
        if (username.isEmpty()) {
            textInputLayout.setError(getResources().getString(R.string.invalid_username));
            editTextUsername.requestFocus();
            isCorrect = false;
        }

        return isCorrect;
    }

    private Boolean validatePassword() {
        boolean isCorrect = true; //variabile di controllo per stabilire se lo Password è corretta

        password = editTextPassword.getText().toString().trim();
        TextInputLayout textInputLayout = findViewById(R.id.text_input_password);

        //Se la password è più corta di 6 caratteri
        if (password.length() < 6) {
            textInputLayout.setError(getResources().getString(R.string.invalid_password));
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
        Resources res = getResources();
        AlertDialog.Builder conferma = new AlertDialog.Builder(Login.this);
        conferma.setTitle(res.getString(R.string.exitDialogTitle));
        conferma.setMessage(res.getString(R.string.exitDialogMessage));
        conferma.setPositiveButton(res.getString(R.string.dialogConfirm), (dialog, which) -> {
            finish();
            System.exit(0);
        });
        conferma.setNegativeButton(res.getString(R.string.dialogDecline), (dialog, which) -> dialog.cancel());
        conferma.show();
    }

}