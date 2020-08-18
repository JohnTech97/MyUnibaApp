package sms.myunibapp.unibaServices;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import sms.myunibapp.DateTimePicker;
import sms.myunibapp.ProfessorHome;
import sms.myunibapp.SpinnerData;

public class AddExam extends AppCompatActivity {

    private ArrayList<String> datiMateria;
    private int[] numeroDate;

    private boolean isSpinnerStarting1=true;//perché quando inizializzati, gli spinner lanciano il listener, cosa da evitare
    private boolean isSpinnerStarting2=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exam);

        Spinner elencoInsegnamenti = findViewById(R.id.insegnamenti);
        Spinner materie = findViewById(R.id.materia_spinner);
        EditText aula = findViewById(R.id.aula_input);
        EditText edificio = findViewById(R.id.edificio_input);
        EditText data = findViewById(R.id.data_input);
        TextView t1 = findViewById(R.id.materia_professore);
        TextView t2 = findViewById(R.id.aula_aggiunta);
        TextView t3 = findViewById(R.id.edificio_aggiunta);
        TextView t4 = findViewById(R.id.data_aggiunta);
        Button commit = findViewById(R.id.commit_exam);
        Button indietro = findViewById(R.id.indietro_professore);
        String lingua = getResources().getConfiguration().locale.getLanguage();
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("CorsoDiLaurea");

        ArrayAdapter<String> elenco = new ArrayAdapter(this, android.R.layout.simple_spinner_item, ProfessorHome.getProfessor().insegnamenti.keySet().toArray());
        elenco.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        elencoInsegnamenti.setAdapter(elenco);

        if(elencoInsegnamenti.getCount()==1){//se c'è un solo item, per via del funzionamento degli spinner, sarebbe impossibile proseguire
            isSpinnerStarting1=false;
        }

        //quando viene selezionato un insegnamento
        elencoInsegnamenti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selezione = "" + parent.getItemAtPosition(position);
                if (!isSpinnerStarting1) {
                    //leggo i dati da firebase
                    dr.child(selezione).child("Esami").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot s) {
                            datiMateria = new ArrayList();
                            numeroDate = new int[(int) s.getChildrenCount()];
                            for (int i = 1, count =0; i <= s.getChildrenCount(); i++) {
                                String key=s.child(""+i).child("key").getValue(String.class);
                                if(ProfessorHome.checkIfPresent(selezione, key)){//se l'esame non è tra quelli assegnati al professore non viene aggiunto alla lista
                                    //per evitare che un professore inserisca esami ad altri professori
                                    datiMateria.add(s.child("" + i).child("nome" + lingua).getValue(String.class));
                                    numeroDate[count] = (int) (s.child("" + i).child("appelli").getChildrenCount()/3)+1;
                                    count++;
                                }
                            }
                            ArrayAdapter<String> elencoMaterie = new ArrayAdapter(AddExam.this, android.R.layout.simple_spinner_item, datiMateria);

                            elencoMaterie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            materie.setAdapter(elencoMaterie);

                            materie.setVisibility(View.VISIBLE);
                            t1.setVisibility(View.VISIBLE);
                            isSpinnerStarting2=materie.getCount()!=1;//stesso motivo di cui sopra
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else{
                    isSpinnerStarting1=false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //quando seleziono una materia
        materie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isSpinnerStarting2) {
                    isSpinnerStarting2 = false;
                } else {
                    aula.setVisibility(View.VISIBLE);
                    edificio.setVisibility(View.VISIBLE);
                    data.setVisibility(View.VISIBLE);
                    t2.setVisibility(View.VISIBLE);
                    t3.setVisibility(View.VISIBLE);
                    t4.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //quando la textview per la data viene selezionata, sia con un tocco che con il focus gain dovuto al next della tastiera
        data.setOnFocusChangeListener((View v, boolean hasFocus) -> {//quando si seleziona il campo data deve mostrare il date time picker
            if(hasFocus){
                DateTimePicker.getDateTime(AddExam.this, data);
            }
        });

        indietro.setOnClickListener(v -> {
            finish();
        });

        commit.setOnClickListener((View v) -> {
            Resources res = getResources();
            if (t4.getVisibility() == View.VISIBLE) {
                String insegnamento = (String) elencoInsegnamenti.getSelectedItem();
                String numeroCorso = "" + (materie.getSelectedItemPosition() + 1);
                String aulaValue, edificioValue, dataValue;
                aulaValue = aula.getText().toString();
                edificioValue = edificio.getText().toString();
                dataValue = data.getText().toString();
                int numeroAppello = numeroDate[materie.getSelectedItemPosition()];

                //inserisco i dati forniti dall'utente nel database, se sono validi
                if (!aulaValue.isEmpty() && !edificioValue.isEmpty() && !dataValue.isEmpty()) {
                    DatabaseReference appello = dr.child(insegnamento).child("Esami").child(numeroCorso).child("appelli");
                    appello.child("aula" + numeroAppello).setValue(aulaValue);
                    appello.child("data" + numeroAppello).setValue(dataValue);
                    appello.child("edificio" + numeroAppello).setValue(edificioValue);
                    finish();
                } else {
                    Toast.makeText(AddExam.this, res.getString(R.string.aggiunta_esame_error), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AddExam.this, res.getString(R.string.aggiunta_esame_error), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}