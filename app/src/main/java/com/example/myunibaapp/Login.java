package com.example.myunibaapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {


    class Utente{
        public final String username;
        public final String password;

        public Utente(String user, String pass){
            username=user;
            password=pass;
        }
    }

    private static Utente utente;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //controllo se l'utente aveva già memorizzato le sue credenziali e se aveva scelto di mantenere l'accesso
        File f = new File(getCacheDir(), String.valueOf(R.string.credentials_file_name));
        if (f.exists()) {
            BufferedReader in;
            try {
                in = new BufferedReader(new FileReader(f));
                String ricorda, utente, pass;
                ricorda = in.readLine();
                utente = in.readLine();
                pass = in.readLine();
                in.close();
                //se l'utente aveva chiesto di ricordare le credenziali si passa direttamente alla schermata home
                if(ricorda.equals("true")){
                    login(utente, pass);
                }

            } catch (IOException e) {}
        }
        //se non esiste si procede con il login
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        Button exit=findViewById(R.id.exit_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Login.this, login(usernameEditText.getText().toString(), passwordEditText.getText().toString()), Toast.LENGTH_LONG).show();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
    }

    @Override
    public void onBackPressed() {
        close();
    }

    private void close(){
        AlertDialog.Builder conferma= new AlertDialog.Builder(Login.this);
        conferma.setTitle("Conferma");
        conferma.setMessage("Sei sicuro di voler chiudere l'applicazione?");
        conferma.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        conferma.show();
    }

    private String login(String username, String password){
        if(isUsernameValid(username)){
            if(isPasswordValid(password)){
                //TODO: ricezione dati da server remoto per poi verificare le credenziali

                utente=new Utente(username, password);

                startActivity(new Intent(this, Home.class));
                setContentView(R.layout.activity_home);
                finish();
                return getResources().getString(R.string.login_valid)+username;
            }else{
                return getResources().getString(R.string.invalid_password);
            }
        }else{
            return getResources().getString(R.string.invalid_username);
        }
    }

    private boolean isUsernameValid(String username){
        //se contiene @ è una mail, altrimenti un nome utente
        if(username.contains("@")) {
            //espressione regolare custom made per verificare la validità di una mail
            return Pattern.matches("([a-z\\d\\.-])+@([a-z\\d-]+)\\.([a-z]{2,8})(\\.[a-z]{2,8})?", username);
        }else{
            //TODO: informarsi meglio per la validità del nome utente di uniba
            return username.contains(".");
        }
    }

    private boolean isPasswordValid(String username){
        return username.length()>=8;
    }

    public static Utente getUserData(){
        return utente;
    }

    public void accessProblem(View view) {
        //TODO: (eventuale) apertura di un link per aiutare l'utente
    }
}