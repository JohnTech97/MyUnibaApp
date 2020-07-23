package sms.myunibapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;

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
import sms.myunibapp.unibaServices.BookableExams;
import sms.myunibapp.unibaServices.BookingsBoard;
import sms.myunibapp.unibaServices.Booklet;
import sms.myunibapp.unibaServices.OutcomeBoard;
import sms.myunibapp.unibaServices.Profile;
import sms.myunibapp.unibaServices.Secretary;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView nav;
    private Toolbar toolbar;
    private GridLayout layout;
    private AlertDialog dialog;
    private SharedPreferences editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawer = findViewById(R.id.menu_navigazione);
        nav = findViewById(R.id.navigation_menu);
        toolbar = findViewById(R.id.menu_starter);
        layout = findViewById(R.id.widgets);
        Button b = findViewById(R.id.personalizzazione_widget);

        //inizializzazione dati esami da firebase

        ExamsData.initializeData();
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle mainMenu = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(mainMenu);
        mainMenu.syncState();


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<items.length;i++){
                    items[i].setChecked(false);
                }
                dialog.show();
            }
        });
        nav.bringToFront();
        nav.setNavigationItemSelectedListener(this);
        editor = getSharedPreferences("Widgets", MODE_PRIVATE);
        initializeWidgetsCustomizationPanel();
        initializeWidgets();


    }

    private Class classes[] = new Class[]{
            Secretary.class,
            Booklet.class,
            BookableExams.class,
            BookingsBoard.class,
            OutcomeBoard.class,
            Profile.class};
    private String iconNames[] = new String[]{
            "segretary_icon",
            "career_icon",
            "exam_list_icon",
            "exam_booking_icon",
            "exam_results_icon",
            "user_icon",};
    private String widgetMessages[] = new String[classes.length];
    private CheckBox items[] = new CheckBox[classes.length];

    private void initializeWidgetsCustomizationPanel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ViewGroup view = (ViewGroup) getLayoutInflater().inflate(R.layout.widget_customization_panel, null);

        Resources res = getResources();
        for (int i = 0; i < classes.length; i++) {
            items[i] = view.findViewById(res.getIdentifier("option" + (i + 1), "id", getPackageName()));
            widgetMessages[i] = items[i].getText().toString();
        }

        builder.setView(view).setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor edit = editor.edit();
                edit.clear().apply();//ripulisco le informazioni precedenti prima di inserire i nuovi
                layout.removeAllViews();
                int count = 1;
                for (int i = 0; i < classes.length; i++) {
                    if (items[i].isChecked()) {
                        edit.putString("icon" + count, iconNames[i]);
                        edit.putString("nomeWidget" + count, widgetMessages[i]);
                        edit.putString("ClasseTarget" + count, classes[i].getName());
                        count++;
                    }
                }

                count--;

                edit.putInt("numberOfWidgets", count);
                edit.apply();

                initializeWidgets();
            }
        });
        dialog = builder.create();
    }

    private void initializeWidgets() {

        /*editor.putInt("numberOfWidgets", 4);
        editor.putString("icon1", "home_icon");
        editor.putString("nomeWidget1", "Torna alla home");
        editor.putString("ClasseTarget1", getClass().getName());

        editor.putString("icon2", "segretary_icon");
        editor.putString("nomeWidget2", "Segreteria");
        editor.putString("ClasseTarget2", Secretary.class.getName());

        editor.putString("icon3", "career_icon");
        editor.putString("nomeWidget3", "Libretto");
        editor.putString("ClasseTarget3", Booklet.class.getName());

        editor.putString("icon4", "exam_list_icon");
        editor.putString("nomeWidget4", "Esami prenotabili");
        editor.putString("ClasseTarget4", BookableExams.class.getName());*/

        int numberOfWidgets = editor.getInt("numberOfWidgets", 0);
        Resources res = getResources();
        System.out.println(numberOfWidgets);
        for (int i = 1; i <= numberOfWidgets; i++) {
            String value= editor.getString("icon" + i, "");
            Drawable icon = getDrawable(res.getIdentifier(value, "drawable", getPackageName()));

            String testo = editor.getString("nomeWidget" + i, "");
            Class target = null;
            try {
                target = Class.forName(editor.getString("ClasseTarget" + i, ""));
            } catch (ClassNotFoundException e) {
            }
            DashboardWidgets widget = new DashboardWidgets(this);
            widget.inflate();
            widget.setIcona(icon);
            widget.setNomeWidget(testo);
            widget.setTarget(target);
            widget.setClickable(true);
            widget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DashboardWidgets dw = (DashboardWidgets) v;
                    startActivity(new Intent(Home.this, dw.getTarget()));

                }
            });

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
                SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
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