package sms.myunibapp.schedeNavigationBar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myunibapp.R;

public class Profilo extends Activity {

    private TextView matricolaTextView, nomeTextView, cognomeTextView, corsoDiLaureaTextView, percorsoTextView, sedeTextView,
                     iscrizioneTextView, statoTextView, classeTextView, annoDiRegolamentoTextView, ordinamentoTextView, normativaTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);
    }
}
