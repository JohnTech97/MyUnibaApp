package sms.myunibapp.oggetti;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myunibapp.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import sms.myunibapp.Constants.FirebaseDb;
import sms.myunibapp.SessionManager;
import sms.myunibapp.principale.HomeActivity;
import sms.myunibapp.principale.Settings;
import sms.myunibapp.profileUser.Profile;
import sms.myunibapp.unibaServices.BookableExams;
import sms.myunibapp.unibaServices.BookingsBoard;
import sms.myunibapp.unibaServices.Booklet;
import sms.myunibapp.unibaServices.LuoghiDiInteresse;
import sms.myunibapp.unibaServices.OutcomeBoard;
import sms.myunibapp.unibaServices.Secretary;

/**
 * Questa classe contiene solo il drawer. E' una classe base. Tutte le classi legate al drawer, dovranno estendere
 * questa classe, per avere anche esse il drawer.
 */
public class DrawerActivity extends AppCompatActivity  {


    public static SessionManager sessionManager;

    /* ACCESSO AL DATABASE */
    protected DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child(FirebaseDb.TABLE_STUDENTE);

    protected FrameLayout progressBar;

    ActionBarDrawerToggle mDrawerToggle;
    Context context;
    private NavigationView navigationView;
    protected DrawerLayout drawerLayout;
    public Toolbar toolbar; // Declaring the Toolbar Object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        // Valorizzo il session manager
        sessionManager = new SessionManager(getApplicationContext());
        progressBar = findViewById(R.id.progress_view);
    }

    protected boolean useToolbar() {
        return true;
    }

    @Override
    public void setContentView(int layoutResID) {
        context = this;

        DrawerLayout fullView = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_nav_drawer, null);
        FrameLayout activityContainer = (FrameLayout) fullView.findViewById(R.id.frame);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);

        super.setContentView(fullView);
        toolbar = (Toolbar) fullView.findViewById(R.id.tool_bar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setTitle("");
        this.getSupportActionBar().setElevation(0);

        //  toolbar.setLogo(R.drawable.ic_main);
        if (useToolbar()) {
            setSupportActionBar(toolbar);
            setTitle("Places Near Me");
        } else {
            toolbar.setVisibility(View.GONE);
        }

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_menu);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()){
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }

                //Classe di destinazione
                Intent intent = null;

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        intent = new Intent(getBaseContext(), HomeActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.segreteria:
                        intent = new Intent(getBaseContext(), Secretary.class);
                        startActivity(intent);
                        break;
                    case R.id.lista_esami:
                        intent = new Intent(getBaseContext(), BookableExams.class);
                        startActivity(intent);
                        break;
                    case R.id.bacheca_prenotazioni:
                        intent = new Intent(getBaseContext(), BookingsBoard.class);
                        startActivity(intent);
                        break;
                    case R.id.bacheca_esiti:
                        intent = new Intent(getBaseContext(), OutcomeBoard.class);
                        startActivity(intent);
                        break;
                    case R.id.carriera:
                        intent = new Intent(getBaseContext(), Booklet.class);
                        startActivity(intent);
                        break;
                    case R.id.profilo_menu:
                        intent = new Intent(getBaseContext(), Profile.class);
                        startActivity(intent);
                        break;
                    case R.id.who_we_are:
                        intent = new Intent(getBaseContext(), WhoAre.class);
                        startActivity(intent);
                        break;
                    case R.id.mappa:
                        intent = new Intent(getBaseContext(), LuoghiDiInteresse.class);
                        startActivity(intent);
                        break;
                    case R.id.impostazioni:
                        intent = new Intent(getBaseContext(), Settings.class);
                        startActivity(intent);
                        break;
                    case R.id.logout: sessionManager.logout();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Work in progress", Toast.LENGTH_SHORT).show();
                        return true;
                }

                return true;
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.menu_navigazione);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return mDrawerToggle.onOptionsItemSelected(item);
    }


}
