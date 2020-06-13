package sms.myunibapp.schedeNavigationBar;

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

import sms.myunibapp.CompoundViews.InfoEsamePrenotabili;

public class EsamiPrenotabili extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esami_prenotabili);

        LinearLayout listaEsami =findViewById(R.id.lista_esami_prenotabili);

        //TODO: ottenere le informazioni degli esami dalla rete e non manualmente da codice
        String titoliEsami []= new String[]{"Calcolo", "Analisi", "Matematica discreta", "Statistica", "Architettura", "Programmazione 2"};
        String links []=new String[]{"myuniba.exams.calculus","myuniba.exams.analisys","myuniba.exams.discreteMath",
                "myuniba.exams.statistycs","myuniba.exams.architecture","myuniba.exams.dataScience2"};
        int cfu[]=new int[]{12, 9, 6, 6, 9, 12};
        int nEsamiPrenotabili[]=new int[]{3, 15, 6, 8, 20, 21};


        Resources r=getResources();

        TextView nEsami= findViewById(R.id.n_esami);

        nEsami.setText(r.getQuantityString(R.plurals.n_esami_da_superare, titoliEsami.length, titoliEsami.length));
        if(titoliEsami.length==1){
            nEsami.setTextColor(Color.GREEN);
        }

        for(int i=0; i<titoliEsami.length; i++){
            InfoEsamePrenotabili esame=new InfoEsamePrenotabili(this);
            esame.inflate();
            esame.setTitoloEsame(titoliEsami[i]);
            esame.setLink(links[i]);
            esame.setCfu(cfu[i]);
            esame.setEsamiPrenotabili(nEsamiPrenotabili[i]);
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

}