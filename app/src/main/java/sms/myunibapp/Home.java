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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myunibapp.R;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;

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

        ExamsData.initializeData(this);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle mainMenu = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(mainMenu);
        mainMenu.syncState();


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < items.length; i++) {
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
        ScrollView scrollView = findViewById(R.id.scrollview_home);
        LinearLayout.LayoutParams altezza = (LinearLayout.LayoutParams) scrollView.getLayoutParams();

        altezza.height = Resources.getSystem().getDisplayMetrics().heightPixels - 500;

        scrollView.setLayoutParams(altezza);

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

        builder.setView(view).setNegativeButton(getResources().getString(R.string.dialogDecline), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton(getResources().getString(R.string.dialogConfirm), new DialogInterface.OnClickListener() {
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

        int numberOfWidgets = editor.getInt("numberOfWidgets", 0);
        Resources res = getResources();
        for (int i = 1; i <= numberOfWidgets; i++) {
            String value = editor.getString("icon" + i, "");
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
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
        Class target = null;

        switch (item.getItemId()) {
            case R.id.home:
                //do nothing
                break;
            case R.id.segreteria:
                target = Secretary.class;
                break;
            case R.id.lista_esami:
                target = BookableExams.class;
                break;
            case R.id.bacheca_prenotazioni:
                target = BookingsBoard.class;
                break;
            case R.id.bacheca_esiti:
                target = OutcomeBoard.class;
                break;
            case R.id.carriera:
                target = Booklet.class;
                break;
            case R.id.profilo_menu:
                target = Profile.class;
                break;
            case R.id.who_we_are:

                break;
            case R.id.impostazioni:
                target = Settings.class;
                break;
            case R.id.logout:
                SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
                editor.putBoolean("Remember", false);
                editor.apply();
                startActivity(new Intent(Home.this, Login.class));
                finish();
                break;
        }

        if (target != null) {
            startActivity(new Intent(Home.this, target));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void close() {
        Resources res= getResources();
        AlertDialog.Builder conferma = new AlertDialog.Builder(Home.this);
        conferma.setTitle(res.getString(R.string.exitDialogTitle));
        conferma.setMessage(res.getString(R.string.exitDialogMessage));
        conferma.setPositiveButton(res.getString(R.string.dialogConfirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        });
        conferma.setNegativeButton(res.getString(R.string.dialogDecline), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        conferma.show();
    }
}