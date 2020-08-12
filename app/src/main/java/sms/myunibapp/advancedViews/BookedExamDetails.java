package sms.myunibapp.advancedViews;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myunibapp.R;

public class BookedExamDetails extends LinearLayout {

    private TextView titoloEsame, aula, docente, edificio, tipo, data;
    private Button prenotazione;
    private String keyEsame;

    public BookedExamDetails(Context c) {
        super(c);
        initializeViews(c);
    }

    public BookedExamDetails(Context c, @Nullable AttributeSet attrs) {
        super(c, attrs);
        initializeViews(c);
    }

    public BookedExamDetails(Context c, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(c, attrs, defStyleAttr);
        initializeViews(c);
    }

    private void initializeViews(Context context) {
        ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.template_exam_details, this, true);
    }

    public void inflate() {
        titoloEsame = findViewById(R.id.nome_esame_prenotabile);
        tipo = findViewById(R.id.tipo_esame_prenotabile);
        data = findViewById(R.id.data_esame_prenotabile);
        edificio = findViewById(R.id.edificio_prenotabile);
        aula = findViewById(R.id.aula_prenotabile);
        docente = findViewById(R.id.docente_prenotabile);
        prenotazione = findViewById(R.id.prenotazione);
    }

    public String getTitoloEsame() {
        return titoloEsame.getText().toString();
    }

    public void setTitoloEsame(String s) {
        titoloEsame.setText(s);
    }

    public String getAula() {
        return aula.getText().toString();
    }

    public void setAula(String aula) {
        this.aula.setText(aula);
    }

    public String getDocente() {
        return docente.getText().toString();
    }

    public void setDocente(String docente) {
        this.docente.setText(docente);
    }

    public String getEdificio() {
        return edificio.getText().toString();
    }

    public void setEdificio(String edificio) {
        this.edificio.setText(edificio);
    }

    public String getTipo() {
        return tipo.getText().toString();
    }

    public void setTipo(String tipo) {
        this.tipo.setText(tipo);
    }

    public String getData() {
        return data.getText().toString();
    }

    public void setData(String data) {
        this.data.setText(data);
    }

    public Button getBottone() {return prenotazione;}

    public String getKeyEsame(){
        return keyEsame;
    }

    public void setKey(String key){
        keyEsame=key;
    }
}