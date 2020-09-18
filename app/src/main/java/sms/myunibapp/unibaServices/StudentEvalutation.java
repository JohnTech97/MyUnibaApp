package sms.myunibapp.unibaServices;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import sms.myunibapp.oggetti.DrawerActivity;
import sms.myunibapp.principale.ProfessorHome;
import sms.myunibapp.SpinnerData;

public class StudentEvalutation extends DrawerActivity {

    private boolean isStarting1 = true;
    private boolean isStarting2 = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_evalutation);

        Spinner insegnamento = findViewById(R.id.spinner_insegnamento_valutazione);
        Spinner esame = findViewById(R.id.spinner_esame_valutazione);
        Spinner studente = findViewById(R.id.spinner_studente_valutazione);
        EditText esito = findViewById(R.id.evaluation);
        Button commit = findViewById(R.id.commit_esito);
        Button indietro = findViewById(R.id.indietro_esito);
        ProfessorHome.ProfessorData dati = ProfessorHome.getProfessor();
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Studente");

        ArrayAdapter<String> insegnamenti = new ArrayAdapter(this, android.R.layout.simple_spinner_item, dati.insegnamenti.keySet().toArray());
        insegnamenti.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        insegnamento.setAdapter(insegnamenti);

        if (insegnamento.getCount() == 1) {
            isStarting1 = false;
        }
        insegnamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isStarting1) {
                    isStarting1 = false;
                } else {
                    ArrayAdapter<String> esami = new ArrayAdapter(StudentEvalutation.this, android.R.layout.simple_spinner_item, dati.insegnamenti.get(parent.getSelectedItem()));
                    esami.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    esame.setAdapter(esami);

                    if (esame.getCount() == 1) {
                        isStarting2 = false;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        esame.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isStarting2) {
                    isStarting2 = false;
                } else {
                    dr.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot d : snapshot.getChildren()) {
                                String datiStudente = d.child("Matricola").getValue(String.class);
                                datiStudente += ": " + d.child("Cognome").getValue(String.class);
                                ArrayList<SpinnerData> studentiDati = new ArrayList();
                                DataSnapshot esamiPrenotati = d.child("Esami prenotati");
                                for (int i = 1; i <= esamiPrenotati.getChildrenCount() / 4; i++) {
                                    String nome = esamiPrenotati.child("nome" + i).getValue(String.class);
                                    if (nome.equals(parent.getItemAtPosition(position))) {
                                        SpinnerData dato = new SpinnerData();
                                        dato.informazioneDaMostrare = datiStudente;
                                        dato.abbreviazione = "" + i;
                                        dato.altro = d.getKey();//per poter andare a ripescarlo dopo immediatamente
                                        dato.altro2 = esamiPrenotati.child("data" + i).getValue(String.class);
                                        studentiDati.add(dato);
                                    }
                                }
                                ArrayAdapter<String> studenti = new ArrayAdapter(StudentEvalutation.this, android.R.layout.simple_spinner_item, studentiDati);
                                studenti.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                studente.setAdapter(studenti);

                            }
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

        commit.setOnClickListener((View v) -> {
            if (esito.getText().toString().isEmpty() || studente.getSelectedItemPosition()==AdapterView.INVALID_POSITION) {
                Toast.makeText(this, getResources().getString(R.string.aggiunta_esito_error), Toast.LENGTH_SHORT).show();
            } else {
                SpinnerData dato = (SpinnerData) studente.getSelectedItem();
                DatabaseReference esamiPrenotati = dr.child(dato.altro).child("Esami prenotati");
                DatabaseReference esamiSuperati = dr.child(dato.altro).child("Esami superati");

                esamiSuperati.child("" + esame.getSelectedItem()).child("data").setValue(dato.altro2);
                esamiSuperati.child("" + esame.getSelectedItem()).child("voto").setValue(esito.getText().toString());

                //eliminazione esame selezionato dalla lista e scalatura dei restanti
                esamiPrenotati.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot s) {
                        HashMap<String, Object> aule = new HashMap<>();
                        HashMap<String, Object> date = new HashMap<>();
                        HashMap<String, Object> edifici = new HashMap<>();
                        HashMap<String, Object> nomi = new HashMap<>();
                        for (int j = 1; j <= s.getChildrenCount() / 4; j++) {
                            aule.put("aula" + j, s.child("aula" + j).getValue(String.class));
                            date.put("data" + j, s.child("data" + j).getValue(String.class));
                            edifici.put("edificio" + j, s.child("edificio" + j).getValue(String.class));
                            nomi.put("nome" + j, s.child("nome" + j).getValue(String.class));
                        }
                        String key = (String) esame.getSelectedItem();
                        for (int k = 1; k <= s.getChildrenCount() / 4; k++) {
                            if (nomi.get("nome" + k).equals(key)) {
                                for (int j = k; j <= s.getChildrenCount() / 4; j++) {
                                    aule.replace("aula" + j, aule.get("aula" + (j + 1)));
                                    date.replace("data" + j, date.get("data" + (j + 1)));
                                    edifici.replace("edificio" + j, edifici.get("edificio" + (j + 1)));
                                    nomi.replace("nome" + j, nomi.get("nome" + (j + 1)));
                                }
                                aule.replace("aula" + aule.size(), null);
                                date.replace("data" + aule.size(), null);
                                edifici.replace("edificio" + aule.size(), null);
                                nomi.replace("nome" + aule.size(), null);
                                break;
                            }
                        }
                        s.getRef().updateChildren(aule);
                        s.getRef().updateChildren(date);
                        s.getRef().updateChildren(edifici);
                        s.getRef().updateChildren(nomi);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        indietro.setOnClickListener(v -> {
            finish();
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}