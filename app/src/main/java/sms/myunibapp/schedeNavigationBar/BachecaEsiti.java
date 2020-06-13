package sms.myunibapp.schedeNavigationBar;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myunibapp.R;

import sms.myunibapp.CompoundViews.EsitoEsame;

public class BachecaEsiti extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_esito_esame);
        LinearLayout listaEsiti = findViewById(R.id.esiti);

        String nomi[]=new String[]{"Calcolo", "Data Mining", "Economia"};
        String date[]=new String[]{"05/07/2020", "10/03/2018", "21/04/2019"};
        String docenti[]=new String[]{"Nicola Fanizzi", "Vincenzo Nardozza", "Filippo Tangorra"};
        int voti[]=new int[]{30, 5, 22};

        for(int i=0;i<nomi.length;i++){
            EsitoEsame esito=new EsitoEsame(this);
            esito.inflate();

            esito.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
            esito.setGravity(Gravity.CENTER_HORIZONTAL);
            esito.setMinimumWidth(1200);

            esito.setOrientation(LinearLayout.VERTICAL);
            esito.setBackground(getDrawable(R.drawable.widgets_background));


            esito.setNomeEsame(nomi[i]);
            esito.setData(date[i]);
            esito.setDocente(docenti[i]);
            esito.setEsito(voti[i]);
            esito.setPadding(30, 30, 30, 30);

            ((LinearLayout.LayoutParams)esito.getLayoutParams()).leftMargin=40;

            listaEsiti.addView(esito);
        }

    }

}
