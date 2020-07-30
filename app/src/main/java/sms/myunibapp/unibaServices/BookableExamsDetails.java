package sms.myunibapp.unibaServices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.myunibapp.R;

import java.util.ArrayList;

import sms.myunibapp.advancedViews.BookableExamDetails;

public class BookableExamsDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedStateInstance) {
        super.onCreate(savedStateInstance);
        setContentView(R.layout.activity_exams_details);
        LinearLayout listaEsami = findViewById(R.id.prenotabili);

        Bundle b=getIntent().getExtras();
        String nomeEsame=b.getString("nomeEsame");
        String tipoProva=b.getString("tipo");
        ArrayList<String> dateAppelli=b.getStringArrayList("dateAppelli");
        ArrayList<String> edifici=b.getStringArrayList("edifici");
        ArrayList<String> aule=b.getStringArrayList("aule");
        String docente =b.getString("docente");

        for(int i=0; i<dateAppelli.size();i++){
            BookableExamDetails esame=new BookableExamDetails(this);

            esame.inflate();
            esame.setTitoloEsame(nomeEsame);
            esame.setTipo(tipoProva);
            esame.setData(dateAppelli.get(i));
            esame.setEdificio(edifici.get(i));
            esame.setAula(aule.get(i));
            esame.setDocente(docente);

            Button button=new Button(this);
            button.setText("Prenotati");
            esame.setBottone(button);
            esame.getBottone().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo aggiunta prenotazione
                }
            });

            listaEsami.addView(esame);

        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}