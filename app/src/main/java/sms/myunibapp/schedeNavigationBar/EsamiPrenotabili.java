package sms.myunibapp.schedeNavigationBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

import sms.myunibapp.CompoundViews.InfoEsamePrenotabili;
import sms.myunibapp.Esami;

public class EsamiPrenotabili extends AppCompatActivity {

    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esami_prenotabili);

        LinearLayout listaEsami =findViewById(R.id.lista_esami_prenotabili);

        db= FirebaseDatabase.getInstance();
        DatabaseReference ref= db.getReference("Studente/"+FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll(".", "_")+"/Esami da superare");//a firebase non piace "."

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot data) {

                Resources r=getResources();

                TextView nEsami= findViewById(R.id.n_esami);
                int count= (int)data.getChildrenCount();

                nEsami.setText(r.getQuantityString(R.plurals.n_esami_da_superare, count, count));
                if(count==1){
                    nEsami.setTextColor(Color.GREEN);
                }

                for(DataSnapshot d:data.getChildren()){
                    InfoEsamePrenotabili esame=new InfoEsamePrenotabili(EsamiPrenotabili.this);
                    Esami.Esame exam= Esami.getEsame(d.getValue(String.class));

                    esame.inflate();
                    esame.setTitoloEsame(exam.getNome());//da ottenere la risorsa in base alla lingua
                    esame.setLink(exam.getNome());
                    esame.setCfu(exam.getCfu());
                    esame.setEsamiPrenotabili(exam.getDate().size());
                    esame.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
                    esame.setClickable(true);
                    esame.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            InfoEsamePrenotabili i=(InfoEsamePrenotabili)v;
                            Toast.makeText(EsamiPrenotabili.this, i.getLink(),Toast.LENGTH_SHORT).show();
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

        //TODO: ottenere le informazioni degli esami dalla rete e non manualmente da codice
        /*String titoliEsami []= new String[]{"Calcolo", "Analisi", "Matematica discreta", "Statistica", "Architettura", "Programmazione 2"};
        String links []=new String[]{"myuniba.exams.calculus","myuniba.exams.analisys","myuniba.exams.discreteMath",
                "myuniba.exams.statistycs","myuniba.exams.architecture","myuniba.exams.dataScience2"};
        int cfu[]=new int[]{12, 9, 6, 6, 9, 12};
        int nEsamiPrenotabili[]=new int[]{3, 15, 6, 8, 20, 21};*/

    }

}