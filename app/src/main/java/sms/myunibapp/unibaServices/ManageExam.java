package sms.myunibapp.unibaServices;

import android.app.AlertDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myunibapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import sms.myunibapp.DateTimePicker;
import sms.myunibapp.oggetti.DrawerActivity;
import sms.myunibapp.principale.ProfessorHome;
import sms.myunibapp.SpinnerData;

public class ManageExam extends DrawerActivity {

    private ArrayList<SpinnerData> datiMateria;
    private int numeroEsameSelezionato;

    private boolean isSpinnerStarting1 = true;//perché quando inizializzati, gli spinner lanciano il listener, cosa da evitare
    private boolean isSpinnerStarting2 = true;
    private boolean isSpinnerStarting3 = true;

    //oggetti che servono in fase di eliminazione appello

    private HashMap<String, Object> aule;
    private HashMap<String, Object> date;
    private HashMap<String, Object> edifici;
    //per l'eliminazione
    DatabaseReference eliminazione=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_exam);

        Spinner elencoInsegnamenti = findViewById(R.id.insegnamenti_manage);
        Spinner materie = findViewById(R.id.materia_spinner_manage);
        Spinner numeroEsame = findViewById(R.id.materia_spinner_numero_manage);
        EditText aula = findViewById(R.id.aula_input_manage);
        EditText edificio = findViewById(R.id.edificio_input_manage);
        EditText data = findViewById(R.id.data_input_manage);
        TextView t1 = findViewById(R.id.materia_professore_manage);
        TextView t2 = findViewById(R.id.materia_numero_manage);
        TextView t3 = findViewById(R.id.aula_aggiunta_manage);
        TextView t4 = findViewById(R.id.edificio_aggiunta_manage);
        TextView t5 = findViewById(R.id.data_aggiunta_manage);
        Button commit = findViewById(R.id.modify_exam);
        Button elimina = findViewById(R.id.rimozione_professore_manage);
        Button indietro = findViewById(R.id.indietro_professore_manage);
        String lingua = getResources().getConfiguration().locale.getLanguage();
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("CorsoDiLaurea");

        ArrayAdapter<String> elenco = new ArrayAdapter(this, android.R.layout.simple_spinner_item, ProfessorHome.getProfessor().insegnamenti.keySet().toArray());
        elenco.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        elencoInsegnamenti.setAdapter(elenco);

        if (elencoInsegnamenti.getCount() == 1) {//se c'è un solo item, per via del funzionamento degli spinner, sarebbe impossibile proseguire
            isSpinnerStarting1 = false;
        }

        elencoInsegnamenti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selezione = "" + parent.getItemAtPosition(position);
                if (!isSpinnerStarting1) {
                    dr.child(selezione).child("Esami").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot s) {
                            int size = (int) s.getChildrenCount();
                            datiMateria = new ArrayList();
                            DataSnapshot d;
                            for (int i = 1, count=0; i <= size; i++) {
                                d = s.child("" + i);
                                String key=d.child("key").getValue(String.class);
                                if(ProfessorHome.checkIfPresent(selezione, key)){
                                    SpinnerData dt= new SpinnerData();
                                    dt.informazioneDaMostrare = d.child("nome" + lingua).getValue(String.class);
                                    dt.abbreviazione = "" + (count+1);
                                    datiMateria.add(dt);
                                    count++;
                                }
                            }
                            ArrayAdapter<String> elencoMaterie = new ArrayAdapter(ManageExam.this, android.R.layout.simple_spinner_item, datiMateria);
                            elencoMaterie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            materie.setAdapter(elencoMaterie);
                            materie.setVisibility(View.VISIBLE);
                            t1.setVisibility(View.VISIBLE);
                            isSpinnerStarting2 = materie.getCount() != 1;//stesso motivo di cui sopra
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    isSpinnerStarting1 = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        materie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isSpinnerStarting2) {
                    isSpinnerStarting2 = false;
                } else {
                    date = new HashMap<>();
                    aule = new HashMap<>();
                    edifici = new HashMap<>();
                    t2.setVisibility(View.VISIBLE);
                    numeroEsame.setVisibility(View.VISIBLE);
                    //faccio riferimento alla variabile eliminazione per comodità nell'eseguire il click listener del bottone di eliminazione
                    eliminazione=dr.child(""+elencoInsegnamenti.getSelectedItem()).child("Esami").child(datiMateria.get(position).abbreviazione).child("appelli");
                    eliminazione.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot s) {
                            ArrayAdapter<String> numeri = new ArrayAdapter<>(ManageExam.this, android.R.layout.simple_spinner_item);
                            numeri.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            for (int j = 1; j <= s.getChildrenCount() / 3; j++) {
                                date.put("data" + j, s.child("data" + j).getValue(String.class));
                                aule.put("aula" + j, s.child("aula" + j).getValue(String.class));
                                edifici.put("edificio" + j, s.child("edificio" + j).getValue(String.class));
                                numeri.add(""+j);
                            }
                            numeroEsame.setAdapter(numeri);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        numeroEsame.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isSpinnerStarting3) {
                    isSpinnerStarting3 = false;
                } else {
                    aula.setText((String)aule.get("aula"+(position+1)));
                    edificio.setText((String)edifici.get("edificio"+(position+1)));
                    data.setText((String)date.get("data"+(position+1)));
                    aula.setVisibility(View.VISIBLE);
                    edificio.setVisibility(View.VISIBLE);
                    data.setVisibility(View.VISIBLE);
                    t3.setVisibility(View.VISIBLE);
                    t4.setVisibility(View.VISIBLE);
                    t5.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        data.setOnFocusChangeListener((View v, boolean hasFocus) -> {//quando si seleziona il campo data deve mostrare il date time picker
            if (hasFocus) {
                DateTimePicker.getDateTime(ManageExam.this, data);
            }
        });

        indietro.setOnClickListener(v -> {
            finish();
        });

        commit.setOnClickListener((View v) -> {
            Resources res = getResources();
            if (t4.getVisibility() == View.VISIBLE) {
                if (edificio.isFocusable()) {
                    String insegnamento = (String) elencoInsegnamenti.getSelectedItem();
                    String numeroCorso = "" + (materie.getSelectedItemPosition() + 1);
                    String aulaValue, edificioValue, dataValue;
                    aulaValue = aula.getText().toString();
                    edificioValue = edificio.getText().toString();
                    dataValue = data.getText().toString();
                    int numeroAppello = numeroEsame.getSelectedItemPosition()+1;

                    if (!aulaValue.isEmpty() && !edificioValue.isEmpty() && !dataValue.isEmpty()) {
                        DatabaseReference appello = dr.child(insegnamento).child("Esami").child(numeroCorso).child("appelli");
                        appello.child("aula" + numeroAppello).setValue(aulaValue);
                        appello.child("data" + numeroAppello).setValue(dataValue);
                        appello.child("edificio" + numeroAppello).setValue(edificioValue);
                        finish();
                    } else {
                        Toast.makeText(ManageExam.this, res.getString(R.string.aggiunta_esame_error), Toast.LENGTH_SHORT).show();
                    }
                } else {//chiede conferma per l'azione da compiere
                    new AlertDialog.Builder(this)
                            .setTitle(res.getString(R.string.modifica_esame_conferma_titolo))
                            .setMessage(res.getString(R.string.modifica_esame_conferma_messaggio))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                                aula.setFocusableInTouchMode(true);
                                edificio.setFocusableInTouchMode(true);
                                data.setFocusableInTouchMode(true);
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }
            } else {
                Toast.makeText(ManageExam.this, res.getString(R.string.aggiunta_esame_error), Toast.LENGTH_SHORT).show();
            }
        });

        elimina.setOnClickListener((View v) -> {
            if (t4.getVisibility() == View.VISIBLE) {
                Resources res = getResources();
                new AlertDialog.Builder(this)
                        .setTitle(res.getString(R.string.modifica_esame_conferma_titolo))
                        .setMessage(res.getString(R.string.modifica_esame_conferma_messaggio))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                            for(int i=numeroEsame.getSelectedItemPosition()+1; i<date.size();i++){//per scalare gli oggetti nel database
                                date.replace("data"+i, date.get("data"+(i+1)));
                                aule.replace("aula"+i, aule.get("aula"+(i+1)));
                                edifici.replace("edificio"+i, edifici.get("edificio"+(i+1)));
                            }
                            date.replace("data"+date.size(), null);
                            aule.replace("aula"+aule.size(), null);
                            edifici.replace("edificio"+edifici.size(), null);
                            eliminazione.updateChildren(date);
                            eliminazione.updateChildren(aule);
                            eliminazione.updateChildren(edifici);
                            finish();
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}