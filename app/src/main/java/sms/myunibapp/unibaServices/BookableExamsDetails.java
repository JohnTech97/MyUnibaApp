package sms.myunibapp.unibaServices;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myunibapp.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import sms.myunibapp.principale.HomeActivity;
import sms.myunibapp.accessApp.LoginActivity;
import sms.myunibapp.advancedViews.BookableExamDetails;

public class BookableExamsDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedStateInstance) {
        super.onCreate(savedStateInstance);
        setContentView(R.layout.activity_exams_details);
        LinearLayout listaEsami = findViewById(R.id.prenotabili);
        DrawerLayout drawer=findViewById(R.id.menu_navigazione_exam_details);
        Toolbar toolbar=findViewById(R.id.menu_starter_exam_details);
        NavigationView nav= findViewById(R.id.navigation_menu_exam_details);
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
                DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Studente").child(LoginActivity.getUsername());

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
}