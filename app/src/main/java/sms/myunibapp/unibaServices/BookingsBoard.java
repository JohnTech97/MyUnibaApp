package sms.myunibapp.unibaServices;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myunibapp.R;

public class BookingsBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_booked_exam);

        //TODO: ricevere dati dalla rete o dal bundle e modificare le view in modo da mostrare le prenotazioni
        //come nelle classi bacheca esiti ed esami prenotabili

        String provvisorio[]=new String[]{""};

        for(int i=0;i<provvisorio.length;i++){
            /*BookableExamItem esamePrenotato=new BookableExamItem(this);
            esamePrenotato.inflate();

            esamePrenotato.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
            esamePrenotato.setGravity(Gravity.CENTER_HORIZONTAL);
            esamePrenotato.setMinimumWidth(1200);

            esamePrenotato.setOrientation(LinearLayout.VERTICAL);
            esamePrenotato.setBackground(getDrawable(R.drawable.widgets_background));


            esamePrenotato.setNomeEsame(nomi[i]);
            esamePrenotato.setData(date[i]);
            esamePrenotato.setDocente(docenti[i]);
            esamePrenotato.setEsito(voti[i]);
            esamePrenotato.setPadding(30, 30, 30, 30);

            ((LinearLayout.LayoutParams)esamePrenotato.getLayoutParams()).leftMargin=40;

            listaEsiti.addView(esamePrenotato);*/
        }

    }
}