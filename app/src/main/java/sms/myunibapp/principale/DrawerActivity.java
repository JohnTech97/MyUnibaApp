package sms.myunibapp.principale;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import sms.myunibapp.SessionManager;


/**
 * Questa classe contiene solo il drawer. E' una classe base. Tutte le classi legate al drawer, dovranno estendere
 * questa classe, per avere anche esse il drawer.
 */
public class DrawerActivity extends AppCompatActivity  {

    protected SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Valorizzo il session manager
        sessionManager = new SessionManager(getApplicationContext());

    }
}
