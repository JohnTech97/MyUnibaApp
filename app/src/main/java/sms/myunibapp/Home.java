package sms.myunibapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridLayout;
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

import sms.myunibapp.advancedViews.DashboardWidgets;
import sms.myunibapp.unibaServices.OutcomeBoard;
import sms.myunibapp.unibaServices.BookableExams;
import sms.myunibapp.unibaServices.Booklet;
import sms.myunibapp.unibaServices.Profile;
import sms.myunibapp.unibaServices.Secretary;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView nav;
    private Toolbar toolbar;
    private GridLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawer = findViewById(R.id.menu_navigazione);
        nav = findViewById(R.id.navigation_menu);
        toolbar = findViewById(R.id.menu_starter);
        layout = findViewById(R.id.widgets);

        //inizializzazione dati esami da firebase

        ExamsData.initializeData();
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle mainMenu = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(mainMenu);
        mainMenu.syncState();

        nav.bringToFront();
        nav.setNavigationItemSelectedListener(this);
        initializeWidgets();
    }

    private void initializeWidgets() {
        SharedPreferences editor = getSharedPreferences("Widgets", MODE_PRIVATE);
        /*editor.putInt("numberOfWidgets", 4);
        editor.putString("icon1", "home_icon");
        editor.putString("nomeWidget1", "Torna alla home");

        editor.putString("icon2", "segretary_icon");
        editor.putString("nomeWidget2", "Segreteria");

        editor.putString("icon3", "career_icon");
        editor.putString("nomeWidget3", "Libretto");

        editor.putString("icon4", "exam_list_icon");
        editor.putString("nomeWidget4", "Esami prenotabili");*/

        int numberOfWidgets=editor.getInt("numberOfWidgets", 0);
        Resources res=getResources();
        for(int i=1;i<=numberOfWidgets;i++){
            String nomeWidget=editor.getString("icon"+i, "");
            int id=res.getIdentifier(nomeWidget,"drawable", getPackageName());
            Drawable icon=getDrawable(id);
            String testo=editor.getString("nomeWidget"+i, "");
            DashboardWidgets widget= new DashboardWidgets(this);
            widget.inflate();
            widget.setIcona(icon);
            widget.setNomeWidget(testo);

            layout.addView(widget);
        }

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
                startActivity(new Intent(Home.this, Secretary.class));
                break;
            case R.id.lista_esami:
                startActivity(new Intent(Home.this, BookableExams.class));
                break;
            case R.id.bacheca_prenotazioni:
                //startActivity(new Intent(Home.this, BachecaEsiti.class));
                break;
            case R.id.bacheca_esiti:
                startActivity(new Intent(Home.this, OutcomeBoard.class));
                break;
            case R.id.carriera:
                startActivity(new Intent(Home.this, Booklet.class));
                break;
            case R.id.profilo_menu:
                startActivity(new Intent(Home.this, Profile.class));
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