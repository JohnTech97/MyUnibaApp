package sms.myunibapp.principale;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myunibapp.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import sms.myunibapp.SessionManager;

/**
 * Questa classe contiene solo il drawer. E' una classe base. Tutte le classi legate al drawer, dovranno estendere
 * questa classe, per avere anche esse il drawer.
 */
public class DrawerActivity extends AppCompatActivity  {

    protected SessionManager sessionManager;

    BottomAppBar bottomAppBar; //Barra di navigazione
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Valorizzo il session manager
        sessionManager = new SessionManager(getApplicationContext());

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
}
