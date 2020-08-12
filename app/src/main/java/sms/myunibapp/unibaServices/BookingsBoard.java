package sms.myunibapp.unibaServices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.myunibapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import sms.myunibapp.ExamsData;
import sms.myunibapp.Login;
import sms.myunibapp.advancedViews.BookableExamDetails;
import sms.myunibapp.advancedViews.BookableExamItem;
import sms.myunibapp.advancedViews.BookedExamDetails;

public class BookingsBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_exams);

        LinearLayout listaEsami=findViewById(R.id.prenotati);

        DatabaseReference dr= FirebaseDatabase.getInstance().getReference().child("Studente").child(Login.getUsername());

        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(@NonNull DataSnapshot sn) {
                DataSnapshot s=sn.child("Esami prenotati");
                for(int i=1;i<=s.getChildrenCount()/4;i++){
                    BookedExamDetails esamePrenotato=new BookedExamDetails(BookingsBoard.this);//si utilizza lo stesso layout di EsamiPrenotabili perché le informazioni da mostrare sono le stesse
                    //per poi modificare la scritta all'interno del bottone presente alla fine dell'impaginazione
                    ExamsData.Esame esame=ExamsData.getEsame(s.child("nome"+i).getValue(String.class));

                    esamePrenotato.inflate();

                    esamePrenotato.setKey(esame.getKey());
                    esamePrenotato.setTitoloEsame(esame.getNome());
                    esamePrenotato.setTipo(esame.getTipo());
                    esamePrenotato.setData(s.child("data"+i).getValue(String.class));
                    esamePrenotato.setEdificio(s.child("edificio"+i).getValue(String.class));
                    esamePrenotato.setAula(s.child("aula"+i).getValue(String.class));
                    esamePrenotato.setDocente(esame.getDocente());
                    esamePrenotato.getBottone().setText(getResources().getString(R.string.dismiss_booking));
                    esamePrenotato.getBottone().setId(i);
                    esamePrenotato.getBottone().setOnClickListener((View v) -> {
                        DataSnapshot esamiDaSuperare = sn.child("Esami da superare");
                        HashMap<String, Object> aule = new HashMap<>();
                        HashMap<String, Object> date = new HashMap<>();
                        HashMap<String, Object> edifici = new HashMap<>();
                        HashMap<String, Object> nomi = new HashMap<>();
                        for (int j = 1; j <= s.getChildrenCount()/4; j++) {
                            aule.put("aula" + j, s.child("aula" + j).getValue(String.class));
                            date.put("data" + j, s.child("data" + j).getValue(String.class));
                            edifici.put("edificio" + j, s.child("edificio" + j).getValue(String.class));
                            nomi.put("nome" + j, s.child("nome" + j).getValue(String.class));
                        }
                        String key= esamePrenotato.getKeyEsame();
                        for (int k = 1; k <= s.getChildrenCount()/4; k++) {
                            if (nomi.get("nome" + k).equals(key)) {
                                for (int j = k; j <= s.getChildrenCount()/4; j++) {
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

                        //inserimento dati esame in esami da superare

                        int num = (int) esamiDaSuperare.getChildrenCount() + 1;//ultima posizione disponibile
                        DatabaseReference ep = esamiDaSuperare.getRef();
                        ep.child(""+num).setValue(esame.getKey());

                        finish();
                    });

                    listaEsami.addView(esamePrenotato);
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
        setResult(1);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}