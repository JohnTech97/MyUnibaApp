package sms.myunibapp.accessApp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myunibapp.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import sms.myunibapp.SessionManager;
import sms.myunibapp.principale.HomeActivity;
import sms.myunibapp.principale.ProfessorHome;

import sms.myunibapp.forgotPassword.ForgotPasswordActivity;

public class LoginActivity extends AppCompatActivity {

    public static final String EMAIL_UNIBA = "@studenti.uniba.it";

    //Credenziali di accesso
    private EditText editTextUsername;
    private EditText editTextPassword;

    //Operazioni per l'accesso
    private FirebaseAuth firebaseAuth;
    SessionManager session;
    private Button mLoginButton;

    //Variabile password dimenticata
    private TextView forgotTextLink;

    //Ricordami
    private CheckBox rememberMe;

    private static String username;
    private String password;

    private FrameLayout progress;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_login);

        initializeVariables(); //Inizializzazione delle variabili
        progress.setVisibility(View.GONE);

        // Creazione del nuovo manager di sessione
        session = new SessionManager(getApplicationContext());

        checkRemember();

        /* ACCESSO ALL'APPLICAZIONE */
        mLoginButton.setOnClickListener(v -> {

            if (!validateUsername() | !validatePassword()) {
                return;

            } else if (checkRemember()) {
                progress.setVisibility(View.VISIBLE);
                TextInputLayout textInputLayout = findViewById(R.id.text_input_username);
                textInputLayout.setVisibility(View.INVISIBLE);
                String mail, pass;
                mail = pref.getString("Email", "");
                pass = pref.getString("Pass", "");
                authenticate(mail, pass);

            } else {
                progress.setVisibility(View.VISIBLE);
                authenticate(username, password);
            }
        });

        /*
        RECUPERO PASSWORD
         */
        forgotTextLink.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class)));

    }

    private boolean checkRemember() {
        boolean ricorda = pref.getBoolean("Remember", false);
        if (ricorda) {
            rememberMe.setChecked(true);
            editTextUsername.setText(pref.getString("Email", ""));
            editTextPassword.setText(pref.getString("Pass", ""));
            ricorda = true;
        }
        return ricorda;
    }


    private void authenticate(String mail, String pass) {
        String access = mail;

        if (!mail.contains("@")) {
            access = mail + EMAIL_UNIBA;
        }

        firebaseAuth.signInWithEmailAndPassword(access, pass).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                progress.setVisibility(View.GONE);

                Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_success) + " " + mail, Toast.LENGTH_SHORT).show();
                Class target = null;
                if (mail.endsWith("@uniba.it")) {//verifico se l'utente sia uno studente o un professore
                    username = mail.replace(".", "_");
                    target = ProfessorHome.class;
                } else if (mail.endsWith("@studenti.uniba.it")) {
                    username = mail.replace(".", "_");
                    target = HomeActivity.class;
                } else {
                    username = mail.concat(EMAIL_UNIBA).replace(".", "_");//perché a firebase da problemi il simbolo "."
                    target = HomeActivity.class;
                }
                session.createLoginSession(rememberMe.isChecked(), mail, pass, username);
                startActivity(new Intent(getApplicationContext(), target));
                finish();
            } else {
                progress.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        LoginActivity.username = username;
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
        progress = findViewById(R.id.progress_view);
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
        AlertDialog.Builder conferma = new AlertDialog.Builder(LoginActivity.this);
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