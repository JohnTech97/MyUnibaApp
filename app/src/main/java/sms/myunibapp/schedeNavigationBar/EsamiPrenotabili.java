package sms.myunibapp.schedeNavigationBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myunibapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sms.myunibapp.CompoundViews.EsamePrenotabile;
import sms.myunibapp.DettaglioEsame;
import sms.myunibapp.Esami;

public class EsamiPrenotabili extends AppCompatActivity {

    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esami_prenotabili);

        LinearLayout listaEsami =findViewById(R.id.lista_esami_prenotabili);

        db= FirebaseDatabase.getInstance();
        DatabaseReference ref= db.getReference().child("Studente").child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "_")).child("Esami da superare");//a firebase non piace "."
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot data) {

                Resources r=getResources();

                TextView nEsami= findViewById(R.id.n_esami);
                int count= (int)data.getChildrenCount();

                nEsami.setText(r.getQuantityString(R.plurals.n_esami_da_superare, count, count));
                if(count==1 || count==0){
                    nEsami.setTextColor(Color.GREEN);
                }

                for(DataSnapshot d:data.getChildren()){
                    EsamePrenotabile esame=new EsamePrenotabile(EsamiPrenotabili.this);
                    Esami.Esame exam= Esami.getEsame(d.getValue(String.class));

                    esame.inflate();
                    esame.setTitoloEsame(exam.getNome());//TODO da ottenere la risorsa in base alla lingua
                    esame.setLink(exam.getNome());
                    esame.setCfu(exam.getCfu());
                    esame.setEsamiPrenotabili(exam.getDate().size());
                    esame.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
                    esame.setClickable(true);
                    esame.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EsamePrenotabile ep=(EsamePrenotabile)v;
                            Intent i=new Intent(EsamiPrenotabili.this, DettaglioEsame.class);
                            Esami.Esame exam= Esami.getEsame(ep.getLink());
                            i.putExtra("nomeEsame", exam.getNome());
                            i.putExtra("tipo", exam.getTipo());
                            i.putExtra("dateAppelli", exam.getDate());
                            i.putExtra("edifici", exam.getEdifici());
                            i.putExtra("aule", exam.getAule());
                            i.putExtra("docente", exam.getDocente());
                            startActivity(i);
                        }
                    });
                    esame.setBackground(getDrawable(R.drawable.widgets_background));
                    esame.setPadding(0,0,0, 24);
                    ((ConstraintLayout.LayoutParams)esame.getLayoutParams()).topMargin=24;

                    listaEsami.addView(esame);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}