package sms.myunibapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myunibapp.R;

import com.google.firebase.auth.FirebaseAuth;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_login);

        initializeVariables(); //Inizializzazione delle variabili

        /*
        ACCESSO ALL'APPLICAZIONE
         */
        mLoginButton.setOnClickListener(v -> {

            validateUsername(); //Check Username

            validatePassword(); //Check Password

            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if(!validateUsername() | !validatePassword()){
                return;
            }else{
                username += EMAIL_UNIBA;
                firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        Toast.makeText(Login.this, "Login avvenuto con successo", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Home.class));
                    } else {
                        Toast.makeText(Login.this, "Error! " + task.getException().getMessage() , Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        //TODO se questa troia funziona ci devi mettere i permessi per la fingerprint

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

    private void initializeVariables() {
        //Acquisizione credeziali per il login
        this.editTextUsername = findViewById(R.id.username);
        this.editTextPassword = findViewById(R.id.password);

        this.mLoginButton = findViewById(R.id.button_login);  //bottone per il login

        this.forgotTextLink = findViewById(R.id.lost_password);  //variabile per il reimposta password

        this.firebaseAuth = FirebaseAuth.getInstance(); //collegamento a firebase
    }

    private Boolean validateUsername(){
        boolean isCorrect = true; //variabile di controllo per stabilire se lo Username è corretto

        String username = editTextUsername.getText().toString().trim();

        //Se il campo username è vuoto
        if (username.isEmpty()) {
            editTextUsername.setError("Inserisci username");
            editTextUsername.requestFocus();
            isCorrect = false;
        }

        return isCorrect;
    }

    private Boolean validatePassword(){
        boolean isCorrect = true; //variabile di controllo per stabilire se lo Password è corretta

        String password = editTextPassword.getText().toString().trim();

        //Se il campo password è vuoto
        if (password.isEmpty()) {
            editTextPassword.setError("Inserisci password");
            editTextPassword.requestFocus();
            isCorrect = false;
        }

        //Se la password è più corta di 6 caratteri
        if (password.length() < 6) {
            editTextPassword.setError("La password deve contenere almeno 6 caratteri");
            editTextPassword.requestFocus();
            isCorrect = false;
        }
        return isCorrect;
    }

}