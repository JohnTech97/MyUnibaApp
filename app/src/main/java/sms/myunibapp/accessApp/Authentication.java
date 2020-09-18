package sms.myunibapp.accessApp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myunibapp.R;
import com.google.firebase.auth.FirebaseAuth;

import sms.myunibapp.SessionManager;
import sms.myunibapp.principale.HomeActivity;
import sms.myunibapp.principale.ProfessorHome;

class Authentication extends AppCompatActivity {

    public static final String EMAIL_UNIBA = "@studenti.uniba.it";

    //Collegamento al database
    protected FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static String userEmailFB;

    public static SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Valorizzo il session manager
        sessionManager = new SessionManager(getApplicationContext());
    }

    /**
     * Funzione che serve per verificare i dati su Firebase realtime
     * @param username
     * @param pass
     */
    protected void authenticate(String username, String pass) {

        String access = username;
        System.out.println(access);

        if (!username.contains("@")) {
            access = username + EMAIL_UNIBA;
        }

        firebaseAuth.signInWithEmailAndPassword(access, pass).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

                Toast.makeText(this, getResources().getString(R.string.login_success) + " " + username, Toast.LENGTH_SHORT).show();
                Class target;

                if (username.endsWith("@uniba.it")) {//verifico se l'utente sia uno studente o un professore
                    userEmailFB = username.replace(".", "_");
                    target = ProfessorHome.class;
                } else if (username.endsWith("@studenti.uniba.it")) {
                    userEmailFB = username.replace(".", "_");
                    target = HomeActivity.class;
                } else {
                    userEmailFB = username.concat(EMAIL_UNIBA).replace(".", "_");//perché a firebase da problemi il simbolo "."
                    target = HomeActivity.class;
                }
                sessionManager.createLoginSession(username, userEmailFB, pass);
                startActivity(new Intent(getApplicationContext(), target));

                finish();

            } else {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                Toast.makeText(this, "Error! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
