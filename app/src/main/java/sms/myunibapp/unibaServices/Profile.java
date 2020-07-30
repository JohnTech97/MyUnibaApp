package sms.myunibapp.unibaServices;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myunibapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sms.myunibapp.Login;

public class Profile extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

        TextView matricola = findViewById(R.id.matricola);
        TextView nome = findViewById(R.id.utente_nome);
        TextView cognome = findViewById(R.id.utente_cognome);
        TextView corsoDiLaurea = findViewById(R.id.corso_di_laurea);
        TextView percorso = findViewById(R.id.percorso);
        TextView sede = findViewById(R.id.sede);
        TextView iscrizione = findViewById(R.id.iscrizione);
        TextView stato = findViewById(R.id.stato);
        TextView classe = findViewById(R.id.classe);
        TextView annoDiRegolamento = findViewById(R.id.ADR);
        TextView ordinamento = findViewById(R.id.ordinamento);
        TextView normativa = findViewById(R.id.normativa);

        DatabaseReference dr= FirebaseDatabase.getInstance().getReference().child("Studente").child(Login.getUsername());

        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot s) {
                nome.setText(s.child("Nome").getValue(String.class));
                cognome.setText(s.child("Cognome").getValue(String.class));
                matricola.setText(s.child("Matricola").getValue(String.class));
                corsoDiLaurea.setText(s.child("CorsoDiLaurea").getValue(String.class));
                percorso.setText(s.child("Percorso").getValue(String.class));
                sede.setText(s.child("Sede").getValue(String.class));
                iscrizione.setText(s.child("Iscrizione").getValue(String.class));
                stato.setText(s.child("Stato").getValue(String.class));
                classe.setText(s.child("Classe").getValue(String.class));
                annoDiRegolamento.setText(""+s.child("AnnoDiRegolamento").getValue(Integer.class));
                ordinamento.setText(""+s.child("Ordinamento").getValue(Integer.class));
                normativa.setText(s.child("Normativa").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
