package sms.myunibapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myunibapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //Credenziali di accesso
    private EditText editTextUsername;
    private EditText editTextPassword;

    //Operazioni per l'accesso
    FirebaseAuth firebaseAuth;
    Button mLoginButton;
    ProgressBar progressBar;

    //Modalità OSPITE


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);

        mLoginButton = findViewById(R.id.button_login);

        firebaseAuth = FirebaseAuth.getInstance(); //collegamento a firebase

        findViewById(R.id.ospite).setOnClickListener(this);
        findViewById(R.id.lost_credenziali).setOnClickListener(this);

        //Evento che si verifica se clicco il tasto LOGIN
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                //Se il campo username è vuoto
                if(username.isEmpty()){
                    editTextUsername.setError("Inserisci username");
                    editTextUsername.requestFocus();
                    return;
                }

                //Se il campo password è vuoto
                if(password.isEmpty()){
                    editTextPassword.setError("Inserisci password");
                    editTextPassword.requestFocus();
                    return;
                }

                //Se la password è più corta di 6 caratteri
                if(password.length() < 6){
                    editTextPassword.setError("La password deve contenere almeno 6 caratteri");
                    editTextPassword.requestFocus();
                }

                //Autenticazione dell'utente
                username += "@studenti.uniba.it";
                firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Login avvenuto con successo", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Home.class));
                        } else {
                            Toast.makeText(LoginActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    private void ospiteLogin(){

    }
    private void lostCredenziali(){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ospite: ospiteLogin(); break;
            case R.id.lost_credenziali: lostCredenziali(); break;
        }
    }
}