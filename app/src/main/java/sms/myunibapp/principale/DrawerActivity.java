package sms.myunibapp.principale;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myunibapp.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import sms.myunibapp.Constants.FirebaseDb;
import sms.myunibapp.SessionManager;

/**
 * Questa classe contiene solo il drawer. E' una classe base. Tutte le classi legate al drawer, dovranno estendere
 * questa classe, per avere anche esse il drawer.
 */
public class DrawerActivity extends AppCompatActivity  {

    public static final String EMAIL_UNIBA = "@studenti.uniba.it";
    private static final int DELAY = 4000;

    public static SessionManager sessionManager;

    //Collegamento al database
    protected FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    /* ACCESSO AL DATABASE */
    protected DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child(FirebaseDb.TABLE_STUDENTE);

    private static String userEmailFB;

    BottomAppBar bottomAppBar; //Barra di navigazione
    FloatingActionButton floatingActionButton;

    protected FrameLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Valorizzo il session manager
        sessionManager = new SessionManager(getApplicationContext());

        //Progress Bar
        progressBar = findViewById(R.id.progress_view);

        //BOTTOM APP BAR
        bottomAppBar = findViewById(R.id.bottomAppBar);
        floatingActionButton = findViewById(R.id.add_widget);
        setSupportActionBar(bottomAppBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.informazioni :
                Toast.makeText(DrawerActivity.this, "INFO! ", Toast.LENGTH_LONG).show();
                break;
            case R.id.menuSettings :
                Toast.makeText(DrawerActivity.this, "SETTINGS! ", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_navigazione :
                Toast.makeText(DrawerActivity.this, "MENU! ", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Funzione che serve per verificare i dati su Firebase realtime
     * @param username
     * @param pass
     */
    protected void authenticate(String username, String pass) {

        String access = username;

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
                Toast.makeText(this, "Error! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}
