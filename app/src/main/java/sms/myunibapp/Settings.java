package sms.myunibapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myunibapp.R;

public class Settings extends AppCompatActivity {

    private class SpinnerData {
        public String lingua;
        public String abbreviazione;

        @Override
        public String toString(){
            return lingua;
        }
    }

    private boolean hasLanguageChanged = false;
    private Switch fingerprints;
    private Spinner s;
    private SpinnerData languages[];

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        s = findViewById(R.id.lingua);
        fingerprints = findViewById(R.id.impronte);

        String[] lingue = getResources().getStringArray(R.array.language_items);
        String[] abbreviazioni = getResources().getStringArray(R.array.language_abbreviations);

        languages = new SpinnerData[lingue.length];

        for (int i = 0; i < lingue.length; i++) {
            languages[i] = new SpinnerData();
            languages[i].lingua = lingue[i];
            languages[i].abbreviazione = abbreviazioni[i];
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        preferences=getSharedPreferences("Settings", MODE_PRIVATE);
        fingerprints.setChecked(preferences.getBoolean("fingerprintsEnabled", false));

        //cerco nell'array languages la posizione da usare per la selezione dello spinner
        //si potrebbe fare con adapter.getPosition(String), ma il toString definito in SpinnerData è già assegnato per ritornare
        //la lingua per esteso, e non l'abbreviazione che invece viene memorizzata nelle SharedPreferences
        int pos=0;
        String lingua= preferences.getString("Language", "");
        if(!lingua.equals("")) {//se si accede alle impostazioni per la prima volta
            for (; pos < languages.length; pos++) {
                if (languages[pos].abbreviazione.equals(lingua)) {
                    break;
                }
            }
        }
        s.setSelection(pos, false);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hasLanguageChanged=true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void annulla(View v) {
        finish();
    }

    public void conferma(View v) {
        if(hasLanguageChanged){
            Toast.makeText(this, getResources().getString(R.string.settings_message), Toast.LENGTH_SHORT).show();
        }
        SharedPreferences.Editor edit=preferences.edit();
        edit.putBoolean("fingerprintsEnabled", fingerprints.isChecked());
        edit.putString("Language", languages[s.getSelectedItemPosition()].abbreviazione);

        edit.apply();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}