package sms.myunibapp.CompoundViews;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myunibapp.R;

public class InfoEsamePrenotabile extends LinearLayout {

    private TextView titoloEsame, aula, docente, edificio, tipo, data;

    public InfoEsamePrenotabile(Context c) {
        super(c);
        initializeViews(c);
    }

    public InfoEsamePrenotabile(Context c, @Nullable AttributeSet attrs) {
        super(c, attrs);
        initializeViews(c);
    }

    public InfoEsamePrenotabile(Context c, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(c, attrs, defStyleAttr);
        initializeViews(c);
    }


    /**
     * Inflates the views in the layout.
     *
     * @param context the current context for the view.
     */
    private void initializeViews(Context context) {
        ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dettaglio_esame_template, this, true);
    }

    public void inflate() {

        // Sets the images for the previous and next buttons. Uses
        // built-in images so you don't need to add images, but in
        // a real application your images should be in the
        // application package so they are always available.
        titoloEsame = findViewById(R.id.nome_esame_prenotabile);
        tipo = findViewById(R.id.tipo_esame_prenotabile);
        data = findViewById(R.id.data_esame_prenotabile);
        edificio = findViewById(R.id.edificio_prenotabile);
        aula = findViewById(R.id.aula_prenotabile);
        docente = findViewById(R.id.docente_prenotabile);
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
}