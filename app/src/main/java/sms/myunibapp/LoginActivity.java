package sms.myunibapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;

import com.example.myunibapp.R;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextUsername;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);

        findViewById(R.id.button_login).setOnClickListener(this);
        findViewById(R.id.ospite).setOnClickListener(this);
        findViewById(R.id.lost_credenziali).setOnClickListener(this);
    }

    private void userLogin(){

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
    }

    private void ospiteLogin(){

    }
    private void lostCredenziali(){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_login: userLogin(); break;
            case R.id.ospite: ospiteLogin(); break;
            case R.id.lost_credenziali: lostCredenziali(); break;
        }
    }
}