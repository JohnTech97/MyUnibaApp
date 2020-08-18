package sms.myunibapp.unibaServices;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;

import com.example.myunibapp.R;
import com.google.android.material.navigation.NavigationView;

import sms.myunibapp.Home;

public class Secretary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segreteria);
        DrawerLayout drawer=findViewById(R.id.menu_navigazione_secretary);
        Toolbar toolbar=findViewById(R.id.menu_starter_secretary);
        NavigationView nav= findViewById(R.id.navigation_menu_secretary);
        ActionBarDrawerToggle mainMenu = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(mainMenu);
        mainMenu.syncState();
        nav.bringToFront();
        nav.setNavigationItemSelectedListener(Home.getNavigationBarListener(this));

        
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}