package sms.myunibapp.CompoundViews;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myunibapp.R;

import sms.myunibapp.EsitoEsameCustomView;

public class EsitoEsame extends LinearLayout {

    private Context ctx;

    private TextView nomeEsame, data, docente, refuseBack;
    private EsitoEsameCustomView esito;
    private Button accetta;

    public EsitoEsame(Context context) {
        super(context);
        initializeViews(context);
    }

    public EsitoEsame(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public EsitoEsame(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    public EsitoEsame(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        ctx = context;
        ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.esito_esame_template, this, true);
    }

    public void inflate() {

        nomeEsame = findViewById(R.id.nome_esame_esito);
        data = findViewById(R.id.data_esito_esame);
        docente = findViewById(R.id.docente_bacheca_esiti);
        refuseBack = findViewById(R.id.refuse_back);
        esito = findViewById(R.id.esito);
        accetta = findViewById(R.id.accettazione);

    }

    public void setEsito(String esito){
        //se supera l'esame il bottone di accettazione rimane visibile
        try{
            if(Integer.parseInt(esito)<18){
                accetta.setVisibility(View.GONE);
                refuseBack.setText("Torna indietro");
            }
        }catch (NumberFormatException e){
            accetta.setVisibility(View.GONE);
            refuseBack.setText("Torna indietro");
        }
        this.esito.setEsito(esito);
    }

    public String getEsito(){
        return esito.getEsito();
    }

    public void setNomeEsame(String nomeEsame) {
        this.nomeEsame.setText(nomeEsame);
    }

    public void setData(String data) {
        this.data.setText(data);
    }

    public void setDocente(String docente) {
        this.docente.setText(docente);
    }

    public void setRefuseBack(String refuseBack) {
        this.refuseBack.setText(refuseBack);
    }
}