package sms.myunibapp.unibaServices;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myunibapp.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;

import sms.myunibapp.SessionManager;
import sms.myunibapp.principale.DrawerActivity;
import sms.myunibapp.principale.HomeActivity;
import sms.myunibapp.accessApp.LoginActivity;
import sms.myunibapp.advancedViews.BookableExamDetails;

public class BookableExamsDetails extends AppCompatActivity {

    private final int CALENDAR = 1;

    @Override
    protected void onCreate(Bundle savedStateInstance) {
        super.onCreate(savedStateInstance);
        setContentView(R.layout.activity_exams_details);
        LinearLayout listaEsami = findViewById(R.id.prenotabili);
        DrawerLayout drawer = findViewById(R.id.menu_navigazione_exam_details);
        Toolbar toolbar = findViewById(R.id.menu_starter_exam_details);
        NavigationView nav = findViewById(R.id.navigation_menu_exam_details);
        ActionBarDrawerToggle mainMenu = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(mainMenu);
        mainMenu.syncState();
        nav.bringToFront();
        nav.setNavigationItemSelectedListener(HomeActivity.getNavigationBarListener(this));

        Bundle b = getIntent().getExtras();
        String key = b.getString("key");
        String nomeEsame = b.getString("nomeEsame");
        String tipoProva = b.getString("tipo");
        ArrayList<String> dateAppelli = b.getStringArrayList("dateAppelli");
        ArrayList<String> edifici = b.getStringArrayList("edifici");
        ArrayList<String> aule = b.getStringArrayList("aule");
        String docente = b.getString("docente");
        int nEsami = b.getInt("nEsami");

        TextView nesami = findViewById(R.id.numero_appelli_disponibili);

        nesami.setText(getResources().getQuantityString(R.plurals.bookable_exams, nEsami, nEsami));

        for (int i = 0; i < dateAppelli.size(); i++) {
            BookableExamDetails esame = new BookableExamDetails(this);

            esame.inflate();
            esame.setTitoloEsame(nomeEsame);
            esame.setTipo(tipoProva);
            esame.setData(dateAppelli.get(i));
            esame.setEdificio(edifici.get(i));
            esame.setAula(aule.get(i));
            esame.setDocente(docente);
            esame.getBottone().setText("Prenotati");
            esame.getBottone().setOnClickListener(v -> {
                DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Studente").child(DrawerActivity.sessionManager.getSessionEmail());

                //per scalare i dati nel database (ad esempio esame3 diventa esame2, ecc) la soluzione più semplice sembra
                //essere recuperare tutti i dati presenti per poi scalarli da codice con un array (in questo caso map, perché viene associato un key)
                //infine vengono aggiornati i dati immettendo in firebase i map così ottenuti
                dr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot s) {
                        DataSnapshot esamiDaSuperare = s.child("Esami da superare");
                        DataSnapshot esamiPrenotati = s.child("Esami prenotati");

                        HashMap<String, Object> update = new HashMap<>();
                        for (int j = 1; j <= esamiDaSuperare.getChildrenCount(); j++) {
                            update.put("" + j, esamiDaSuperare.child("" + j).getValue(String.class));
                        }
                        for (int k = 1; k <= esamiDaSuperare.getChildrenCount(); k++) {
                            if (update.get("" + k).equals(key)) {//scalo gli esami presenti da quello scelto in poi
                                for (int j = k; j <= esamiDaSuperare.getChildrenCount(); j++) {
                                    update.replace("" + j, update.get("" + (j + 1)));
                                }
                                update.replace("" + update.size(), null);//per poi eliminare quello in ultima posizione, cioè quello selezionato
                                break;
                            }
                        }
                        esamiDaSuperare.getRef().updateChildren(update);

                        //inserimento dati esame in esami da prenotare

                        int num = ((int) esamiPrenotati.getChildrenCount() / 4) + 1;//ultima posizione disponibile
                        DatabaseReference ep = esamiPrenotati.getRef();
                        ep.child("aula" + num).setValue(esame.getAula());
                        ep.child("data" + num).setValue(esame.getData());
                        ep.child("edificio" + num).setValue(esame.getEdificio());
                        ep.child("nome" + num).setValue(key);

                        //Inserimento data nel calendario di sistema

                        if (ContextCompat.checkSelfPermission(BookableExamsDetails.this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                            String calUriString = "content://com.android.calendar/events";
                            ContentValues values = new ContentValues();

                            values.put("calendar_id", 2); //id, We need to choose from our mobile for primary its 1
                            values.put("title", "Esame di: " + esame.getTitoloEsame());
                            values.put("description", "Hai prenotato questo esame per questa data");
                            values.put("eventLocation", esame.getEdificio() + " - " + esame.getAula());
                            long startTime = System.currentTimeMillis() + 1000 * 60 * 60 * 24;
                            try {
                                startTime = new SimpleDateFormat().parse(esame.getData()).getTime();
                            } catch (ParseException e) {
                            }
                            values.put("dtstart", startTime);
                            values.put("dtend", startTime);
                            values.put("eventTimezone", TimeZone.getDefault().getID());
                            values.put("allDay", 1); //If it is birthday alarm or such kind (which should remind me for whole day) 0 for false, 1 for true
                            values.put("eventStatus", 1); // This information is sufficient for most entries tentative (0), confirmed (1) or canceled (2):
                            values.put("hasAlarm", 1); // 0 for false, 1 for true

                            getApplicationContext().getContentResolver().insert(Uri.parse(calUriString), values);
                        } else {
                            ActivityCompat.requestPermissions(BookableExamsDetails.this,
                                    new String[]{Manifest.permission.WRITE_CALENDAR}, CALENDAR);
                        }
                        finish();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            });

            listaEsami.addView(esame);

        }

    }

    @Override
    public void finish() {
        super.finish();
        setResult(1);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // permission concessa: eseguiamo il codice
            Toast.makeText(this, "Il prossimo esame che andrai a prenotare sarà inserito nel calendario di sistema", Toast.LENGTH_SHORT).show();
        } else {
            // permission negata: non è di vitale importanza inserire una data nel calendario, l'esame risulterà soltanto in app
        }
        return;
    }
}