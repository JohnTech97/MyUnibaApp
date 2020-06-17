package sms.myunibapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.myunibapp.R;

import org.w3c.dom.Text;

public class DettaglioEsame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_dettaglio_esame);

        //TODO: ricevere dati dalla rete o dal bundle e modificare le view in modo da mostrare i dettagli

        ((TextView)findViewById(R.id.nome_esame_dettaglio)).setText(b.getString(""));
        ((TextView)findViewById(R.id.tipo_esame_dettaglio)).setText(b.getString(""));
        ((TextView)findViewById(R.id.prenotazione_esame)).setText(b.getString(""));
        ((TextView)findViewById(R.id.edificio_prenotazione_dettaglio)).setText(b.getString(""));
        ((TextView)findViewById(R.id.aula_prenotazione_dettaglio)).setText(b.getString(""));
        ((TextView)findViewById(R.id.docente_dettaglio)).setText(b.getString(""));
    }
}