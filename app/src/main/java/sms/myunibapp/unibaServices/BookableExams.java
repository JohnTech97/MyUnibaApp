package sms.myunibapp.unibaServices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.example.myunibapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sms.myunibapp.advancedViews.BookableExamItem;
import sms.myunibapp.ExamsData;

public class BookableExams extends AppCompatActivity {

    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookable_exams_list);

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
                    BookableExamItem esame=new BookableExamItem(BookableExams.this);
                    ExamsData.Esame exam= ExamsData.getEsame(d.getValue(String.class));

                    esame.inflate();
                    esame.setTitoloEsame(exam.getNome());//TODO da ottenere la risorsa in base alla lingua
                    esame.setLink(exam.getNome());
                    esame.setCfu(exam.getCfu());
                    esame.setEsamiPrenotabili(exam.getDate().size());
                    esame.setClickable(true);
                    esame.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BookableExamItem ep=(BookableExamItem)v;
                            Intent i=new Intent(BookableExams.this, BookableExamsDetails.class);
                            ExamsData.Esame exam= ExamsData.getEsame(ep.getLink());
                            i.putExtra("nomeEsame", exam.getNome());
                            i.putExtra("tipo", exam.getTipo());
                            i.putExtra("dateAppelli", exam.getDate());
                            i.putExtra("edifici", exam.getEdifici());
                            i.putExtra("aule", exam.getAule());
                            i.putExtra("docente", exam.getDocente());
                            startActivity(i);
                        }
                    });
                    Space space=new Space(BookableExams.this);
                    space.setMinimumHeight(24);
                    listaEsami.addView(space);
                    listaEsami.addView(esame);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}