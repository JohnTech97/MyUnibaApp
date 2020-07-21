package sms.myunibapp.unibaServices;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myunibapp.R;

import sms.myunibapp.advancedViews.ExamOutcomeDetails;

public class OutcomeBoard extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_exam_outcome);
        LinearLayout listaEsiti = findViewById(R.id.esiti);

        String nomi[]=new String[]{"Calcolo", "Data Mining", "Economia", "Fisica"};
        String date[]=new String[]{"05/07/2020", "10/03/2018", "21/04/2019", "07/09/2017"};
        String docenti[]=new String[]{"Nicola Fanizzi", "Vincenzo Nardozza", "Filippo Tangorra", "Piergiorgio Fusco"};
        String voti[]=new String[]{"30", "5", "22", "Ritirato"};
        //se non ci sono esami rimarrà la scritta "nessun esito da mostrare"
        if (nomi.length!=0){
            findViewById(R.id.esiti_da_mostrare).setVisibility(View.GONE);
            for(int i=0;i<nomi.length;i++){
                ExamOutcomeDetails esito=new ExamOutcomeDetails(this);
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
}
