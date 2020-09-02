package sms.myunibapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import sms.myunibapp.accessApp.LoginActivity;

public class SessionManager {

    /**
     * Oggetto che conterrà tutte le preferenze salvate
     */
    SharedPreferences preferences;

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

    /**
     * Valori chiave
     */
    public static final String REMEMBER = "Remember";
    public static final String USERNAME = "Email";
    public static final String PASSWORD = "Pass";
    public static final String NOMECOMPLETO = "Username";
    public static final String FINGERPRINTS = "fingerprintsEnabled";
    public static final String LANGUAGE = "Language";

    public static final String WIDGETS = "Widgets";
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
        // Associo l'editor delle preferenze alla variabile di classe
        editor = preferences.edit();
    }

    /**
     * Funzione che salva i dati nel file di sessione
     */
    public void createLoginSession(Boolean rememberMe, String email, String pass, String nomeCompleto) {
        editor.putBoolean(REMEMBER, rememberMe);
        editor.putString(USERNAME, email);
        editor.putString(PASSWORD, pass);
        editor.putBoolean(FINGERPRINTS, true);
        editor.putString(NOMECOMPLETO, nomeCompleto);
        editor.commit();
    }

    /**
     * Funzione che salva i dati nel file di sessione
     */
    public void createLoginSession(String email, String pass, String nomeCompleto) {
        editor.putBoolean(REMEMBER, true);
        editor.putString(USERNAME, email);
        editor.putString(PASSWORD, pass);
        editor.putBoolean(FINGERPRINTS, true);
        editor.putString(NOMECOMPLETO, nomeCompleto);
        editor.commit();
    }

    public void setProfilePic(String pathfoto) {
        editor.putString(PATHFOTO, pathfoto);
        editor.commit();
    }

    public static String getWIDGETS() {
        return WIDGETS;
    }

    /**
     * Funzione che restituisce l'username di sessione
     *
     * @return username
     */
    public String getSessionEmail() {
        return preferences.getString(USERNAME, null);
    }

    /**
     * Funzione che restituisce il nome completo dell'utente
     *
     * @return uid
     */
    public String getNomeCompleto(){
        return preferences.getString(NOMECOMPLETO, null);
    }

    /**
     * Funzione che resituisce il tipo di sessione
     *
     * @return tipo utente
     */
    public int getSessionType() {
        return preferences.getInt(WIDGETS, -1);
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