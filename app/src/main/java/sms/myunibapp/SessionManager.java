package sms.myunibapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.myunibapp.R;

import sms.myunibapp.accessApp.LoginActivity;

public class SessionManager {


    /**
     * Oggetto che conterrà tutte le preferenze salvate
     */
    SharedPreferences preferences;
    SharedPreferences settings;

    /**
     * Editor delle preferenze
     */
    SharedPreferences.Editor editor;

    /**
     * Oggetto che sarà valorizzato in ogni occasione col contesto corrente
     */
    Context myContext;

    /**
     * File che conterrà le preferenze
     */
    private static final String PREF_NAME = "SettingsAppPref";
    private static final String SETTINGS = "Settings";

    /**
     * Valori chiave
     */

    public static final String REMEMBER = "Remember";
    public static final String USERNAME = "Username";
    private static final String MAIL = "Mail";
    public static final String PASSWORD = "Pass";
    public static final String FINGERPRINTS = "fingerprintsEnabled";
    public static final String LANGUAGE = "Language";
    public static final String PATHFOTO = "pathfoto";


    /**
     * Valore per acquisire le preferenze in modalità privata
     */
    final int PRIVATE_MODE = 0;

    /**
     * Costruttore
     */
    public SessionManager(Context context) {
        this.myContext = context;

        // Associo alle preferenze legate al contesto corrente
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        settings = context.getSharedPreferences(SETTINGS, PRIVATE_MODE);

        // Associo l'editor delle preferenze alla variabile di classe
        editor = preferences.edit();
    }

    /**
     * Funzione che salva i dati nel file di sessione
     */
    public void createLoginSession(String username, String email, String pass) {
        editor.putString(MAIL, email);
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, pass);
        editor.putBoolean(FINGERPRINTS, true);
        editor.commit();
    }

    /**
     *
     * @param pathfoto
     */
    public void setProfilePic(String pathfoto) {
        editor.putString(PATHFOTO, pathfoto);
        editor.commit();
    }

    /**
     * Funzione che restituisce l'username di sessione
     *
     * @return username
     */
    public String getSessionEmail() {
        return preferences.getString(MAIL, "");
    }

    /**
     * Funzione che restituisce l'username di sessione
     *
     * @return username
     */
    public String getSessionUsername() {
        return preferences.getString(USERNAME, "");
    }
    /**
     * Funzione che restituisce l'username di sessione
     *
     * @return username
     */
    public String getSessionPassword() {
        return preferences.getString(PASSWORD, "");
    }


    /**
     * Funzione che restituisce l'url della foto profilo
     *
     * @return url foto profilo
     */
    public String getProfilePic() {
        return preferences.getString(PATHFOTO, null);
    }

    /**
     * Funzione che restituisce la lingua del sistema impostata
     *
     * @return lingua del sistema
     */
    public String getLanguage(String system) {
        return settings.getString(LANGUAGE, system);
    }

    /**
     * Funzione che restituisce la lingua del sistema impostata
     *
     * @return lingua del sistema
     */
    public boolean getFingerprintsEnable() {
        return preferences.getBoolean(FINGERPRINTS, true);
    }

    /**
     * Controlla se l'utente è loggato o no.
     */
    public void setRememberMe(boolean rememberMe) {
        editor.putBoolean(REMEMBER, rememberMe);
        editor.commit();
    }
    /**
     * Controlla se l'utente è loggato o no.
     */
    public boolean checkLogin() {
        return preferences.getBoolean(REMEMBER, false);
    }

    /**
     * Funzione di logout. Pulisce tutti i dati di sessione e reindirizza l'utente al login.
     */
    public void logout() {
        editor.clear();
        editor.commit();
        Intent login = new Intent(myContext, LoginActivity.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);// Inizio di una nuova "storia" d'uso
        login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myContext.startActivity(login);
    }

}