package sms.myunibapp.unibaServices;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myunibapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sms.myunibapp.ExamsData;
import sms.myunibapp.Login;
import sms.myunibapp.advancedViews.ExamOutcomeDetails;

public class OutcomeBoard extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_exam_outcome);
        LinearLayout listaEsiti = findViewById(R.id.esiti);

        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Studente").child(Login.getUsername()).child("Esami superati");

        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot s) {
                //se non ci sono esami rimarrà la scritta "nessun esito da mostrare"
                if (s.getChildrenCount()!=0){
                    findViewById(R.id.esiti_da_mostrare).setVisibility(View.GONE);
                    for(DataSnapshot d: s.getChildren()){
                        ExamsData.Esame esame= ExamsData.getEsame(d.getKey());

                        ExamOutcomeDetails esito=new ExamOutcomeDetails(OutcomeBoard.this);
                        esito.inflate();

                        esito.setNomeEsame(esame.getNome());
                        esito.setEsito(d.child("voto").getValue(String.class));
                        esito.setData(d.child("data").getValue(String.class));
                        esito.setDocente(esame.getDocente());
                        listaEsiti.addView(esito);
                    }
                }
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
