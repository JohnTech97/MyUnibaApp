package sms.myunibapp.unibaServices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import sms.myunibapp.ExamsData;
import sms.myunibapp.Login;
import sms.myunibapp.advancedViews.BookableExamDetails;
import sms.myunibapp.advancedViews.BookableExamItem;

public class BookingsBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_exams);

        LinearLayout listaEsami=findViewById(R.id.prenotati);

        DatabaseReference dr= FirebaseDatabase.getInstance().getReference().child("Studente").child(Login.getUsername()).child("Esami prenotati");

        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot s) {
                for(int i=1;i<=s.getChildrenCount()/4;i++){
                    BookableExamDetails esamePrenotato=new BookableExamDetails(BookingsBoard.this);//si utilizza lo stesso layout di EsamiPrenotabili perché le informazioni da mostrare sono le stesse
                    //per poi modificare la scritta all'interno del bottone presente alla fine dell'impaginazione
                    ExamsData.Esame esame=ExamsData.getEsame(s.child("nome"+i).getValue(String.class));

                    esamePrenotato.inflate();

                    esamePrenotato.setTitoloEsame(esame.getNome());
                    esamePrenotato.setTipo(esame.getTipo());
                    esamePrenotato.setData(s.child("data"+i).getValue(String.class));
                    esamePrenotato.setEdificio(s.child("edificio"+i).getValue(String.class));
                    esamePrenotato.setAula(s.child("aula"+i).getValue(String.class));
                    esamePrenotato.setDocente(esame.getDocente());

                    Button b= new Button(BookingsBoard.this);
                    b.setText("Disdici prenotazione");
                    esamePrenotato.setBottone(b);
                    esamePrenotato.getBottone().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //todo cancellazione prenotazione
                        }
                    });

                    listaEsami.addView(esamePrenotato);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}