package sms.myunibapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myunibapp.R;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    static final String EMAIL_UNIBA = "@studenti.uniba.it";

    //Credenziali di accesso
    private EditText editTextUsername;
    private EditText editTextPassword;

    //Operazioni per l'accesso
    FirebaseAuth firebaseAuth;
    Button mLoginButton;
    ProgressBar progressBar;
    TextView forgotTextLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_login);

        //Acquisizione credeziali per il login
        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);

        mLoginButton = findViewById(R.id.button_login);  //bottone per il login

        forgotTextLink = findViewById(R.id.lost_password);  //variabile per il reimposta password

        firebaseAuth = FirebaseAuth.getInstance(); //collegamento a firebase

        //findViewById(R.id.ospite).setOnClickListener(this);

        /*
        ACCESSO ALL'APPLICAZIONE
         */
        mLoginButton.setOnClickListener(v -> {

            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            //Se il campo username è vuoto
            if (username.isEmpty()) {
                editTextUsername.setError("Inserisci username");
                editTextUsername.requestFocus();
                return;
            }

            //Se il campo password è vuoto
            if (password.isEmpty()) {
                editTextPassword.setError("Inserisci password");
                editTextPassword.requestFocus();
                return;
            }

            //Se la password è più corta di 6 caratteri
            if (password.length() < 6) {
                editTextPassword.setError("La password deve contenere almeno 6 caratteri");
                editTextPassword.requestFocus();
            }

            //Autenticazione dell'utente
            username += EMAIL_UNIBA;
            firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(task -> {

                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Login avvenuto con successo", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Home.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
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

}