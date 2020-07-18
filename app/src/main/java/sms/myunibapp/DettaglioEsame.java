package sms.myunibapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.example.myunibapp.R;

import java.util.ArrayList;

import sms.myunibapp.CompoundViews.EsamePrenotabile;
import sms.myunibapp.CompoundViews.InfoEsamePrenotabile;

public class DettaglioEsame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedStateInstance) {
        super.onCreate(savedStateInstance);
        setContentView(R.layout.activity_dettaglio_esame);
        LinearLayout listaEsami = findViewById(R.id.prenotabili);

        Bundle b=getIntent().getExtras();
        String nomeEsame=b.getString("nomeEsame");
        String tipoProva=b.getString("tipo");
        ArrayList<String> dateAppelli=b.getStringArrayList("dateAppelli");
        ArrayList<String> edifici=b.getStringArrayList("edifici");
        ArrayList<String> aule=b.getStringArrayList("aule");
        String docente =b.getString("docente");

        for(int i=0; i<dateAppelli.size();i++){
            InfoEsamePrenotabile esame=new InfoEsamePrenotabile(this);

            esame.inflate();
            esame.setTitoloEsame(nomeEsame);
            esame.setTipo(tipoProva);
            esame.setData(dateAppelli.get(i));
            esame.setEdificio(edifici.get(i));
            esame.setAula(aule.get(i));
            esame.setDocente(docente);

            esame.setLayoutParams(new LinearLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
            esame.setBackground(getDrawable(R.drawable.widgets_background));
            esame.setGravity(Gravity.CENTER_HORIZONTAL);
            esame.setMinimumWidth(1200);
            esame.setPadding(0,0,0, 24);
            ((LinearLayout.LayoutParams)esame.getLayoutParams()).leftMargin=40;

            listaEsami.addView(esame);

        }

    }
}