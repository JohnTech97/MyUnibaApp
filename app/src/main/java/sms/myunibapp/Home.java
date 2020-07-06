package sms.myunibapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myunibapp.R;
import com.google.android.material.navigation.NavigationView;

import sms.myunibapp.schedeNavigationBar.BachecaEsiti;
import sms.myunibapp.schedeNavigationBar.EsamiPrenotabili;
import sms.myunibapp.schedeNavigationBar.Libretto;
import sms.myunibapp.schedeNavigationBar.Profilo;
import sms.myunibapp.schedeNavigationBar.Segreteria;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView nav;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawer = findViewById(R.id.menu_navigazione);
        nav = findViewById(R.id.navigation_menu);
        toolbar = findViewById(R.id.menu_starter);

        //inizializzazione dati esami da firebase
        String n=Esami.initializeData(this);

        TextView t=findViewById(R.id.info_text);
        t.setText(n);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle mainMenu = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(mainMenu);
        mainMenu.syncState();

        nav.bringToFront();
        nav.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            close();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                //do nothing
                break;
            case R.id.segreteria:
                startActivity(new Intent(Home.this, Segreteria.class));
                break;
            case R.id.lista_esami:
                startActivity(new Intent(Home.this, EsamiPrenotabili.class));
                break;
            case R.id.bacheca_prenotazioni:
                //startActivity(new Intent(Home.this, BachecaEsiti.class));
                break;
            case R.id.bacheca_esiti:
                startActivity(new Intent(Home.this, BachecaEsiti.class));
                break;
            case R.id.carriera:
                startActivity(new Intent(Home.this, Libretto.class));
                break;
            case R.id.profilo_menu:
                startActivity(new Intent(Home.this, Profilo.class));
                break;
            case R.id.who_we_are:

                break;
            case R.id.logout:
                SharedPreferences.Editor editor= getSharedPreferences("Settings", MODE_PRIVATE).edit();
                editor.putBoolean("Remember", false);
                editor.apply();
                startActivity(new Intent(Home.this, Login.class));
                finish();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void close() {
        AlertDialog.Builder conferma = new AlertDialog.Builder(Home.this);
        conferma.setTitle("Conferma");
        conferma.setMessage("Sei sicuro di voler chiudere l'applicazione?");
        conferma.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        });
        conferma.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        conferma.show();
    }
}