package sms.myunibapp.profileUser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myunibapp.R;

import sms.myunibapp.principale.ProfessorHome;

public class ProfessorProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_profile);

        TextView nome, cognome, aula;

        cognome=findViewById(R.id.professore_cognome);
        nome=findViewById(R.id.professore_nome);
        aula=findViewById(R.id.aula_professore);

        ProfessorHome.ProfessorData dati=ProfessorHome.getProfessor();

        cognome.setText(dati.cognome);
        nome.setText(dati.nome);
        aula.setText(dati.aula);

        LinearLayout insegnamenti=findViewById(R.id.insegnamenti_professore);

        for(int i=0;i<dati.insegnamenti.size();i++){
            TextView insegmamento=new TextView(this);
            insegmamento.setText((i+1)+": "+dati.insegnamenti.keySet().toArray()[i]);
            insegnamenti.addView(insegmamento);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}