package sms.myunibapp.accessApp;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.myunibapp.R;
import com.google.android.material.textfield.TextInputLayout;

import sms.myunibapp.forgotPassword.ForgotPasswordActivity;
import sms.myunibapp.principale.DrawerActivity;

public class LoginActivity extends DrawerActivity {

    public static final String EMAIL_UNIBA = "@studenti.uniba.it";

    //Credenziali di accesso
    private EditText editTextUsername;
    private EditText editTextPassword;

    private Button mLoginButton;

    //Variabile password dimenticata
    private TextView forgotTextLink;

    //Ricordami
    private CheckBox rememberMe;

    private static String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_login);

        initializeVariables(); //Inizializzazione delle variabili
        progressBar.setVisibility(View.GONE);

        boolean isChecked = checkRemember();


        /* ACCESSO ALL'APPLICAZIONE */
        mLoginButton.setOnClickListener(v -> {

            setCheckRemeberMe();

            if (!validateUsername() | !validatePassword()) {
                return;

            } else if (isChecked) {
                progressBar.setVisibility(View.VISIBLE);
                String mail, pass;
                mail = sessionManager.getSessionUsername();
                pass = sessionManager.getSessionPassword();
                authenticate(mail, pass);

            } else {
                progressBar.setVisibility(View.VISIBLE);
                authenticate(username, password);
            }
        });

        /*
        RECUPERO PASSWORD
         */
        forgotTextLink.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class)));

    }

    private void setCheckRemeberMe() {

        if(rememberMe.isChecked()){
            sessionManager.setRememberMe(true);
        }else{
            sessionManager.setRememberMe(false);
        }
    }

    /**
     * Controllo se l'utente ha deciso di salvare le credenziali
     * @return true se le ha salvate, altrimenti false
     */
    private boolean checkRemember() {
        boolean ricorda = sessionManager.checkLogin();

        if (ricorda) {
            rememberMe.setChecked(true);
            editTextUsername.setText(sessionManager.getSessionUsername());
            editTextPassword.setText(sessionManager.getSessionPassword());
            ricorda = true;
        }
        return ricorda;
    }

    /**
     * Inizializzazione dei campi presenti nel layout
     */
    private void initializeVariables() {

        //Acquisizione credeziali per il login
        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);

        //bottone per il login
        mLoginButton = findViewById(R.id.button_login);

        //Checkbox per ricordare le credenziali
        rememberMe = findViewById(R.id.remember);

        //Variabile per reimpostare la password
        forgotTextLink = findViewById(R.id.lost_password);

        //ProgressBar
        progressBar = findViewById(R.id.progress_view);
    }

    /**
     * Controllo effettuato sullo username.
     * @return true se lo username è correttamente stata inserito, altrimenti false
     */
    private boolean validateUsername() {
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

    /**
     * Controllo effettuato sulla password.
     * La password deve avere almeno 6 caratteri altrimenti è errata
     *
     * @return true se la password è correttamente stata inserita, altrimenti false
     */
    private boolean validatePassword() {

        boolean isCorrect = true; //variabile di controllo per stabilire se la Password è corretta

        password = editTextPassword.getText().toString().trim();

        TextInputLayout textInputLayout = findViewById(R.id.text_input_password);

        //Se la password è più corta di 6 caratteri
        if (password.length() < 6) {
            textInputLayout.setError(getResources().getString(R.string.invalid_password));
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