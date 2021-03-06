package sms.myunibapp.principale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;

import com.example.myunibapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sms.myunibapp.oggetti.DrawerActivity;
import sms.myunibapp.profileUser.ProfessorProfile;
import sms.myunibapp.advancedViews.DashboardWidgets;
import sms.myunibapp.unibaServices.AddExam;
import sms.myunibapp.unibaServices.ChatActivity;
import sms.myunibapp.unibaServices.ManageExam;
import sms.myunibapp.unibaServices.StudentEvalutation;

public class ProfessorHome extends DrawerActivity {

    public class ProfessorData{
        public String nome;
        public String cognome;
        public String aula;
        public String settore;
        public Map<String, ArrayList<String>> insegnamenti=new HashMap();//per ogni insegnamento va memorizzata la lista degli esami assegnati al professore
    }

    private static ProfessorData prof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_home);

        GridLayout widgets=findViewById(R.id.widgets_professor);

        Resources res=getResources();
        String descr[]=res.getStringArray(R.array.professor_home_widgets_descriptions);
        DashboardWidgets aggiuntaEsami, valutazioneStudenti, gestione, profilo, chat, impostazioni;
        aggiuntaEsami=new DashboardWidgets(this);
        aggiuntaEsami.inflate();
        aggiuntaEsami.setIcon(getDrawable(R.drawable.icon_add));
        aggiuntaEsami.setNomeWidget(res.getString(R.string.aggiunta_date));
        aggiuntaEsami.setDescrizione(descr[0]);
        aggiuntaEsami.setTarget(AddExam.class);
        aggiuntaEsami.setClickable(true);

        widgets.addView(aggiuntaEsami);

        valutazioneStudenti=new DashboardWidgets(this);
        valutazioneStudenti.inflate();
        valutazioneStudenti.setIcon(getDrawable(R.drawable.evaluate_icon));//placeholder
        valutazioneStudenti.setNomeWidget(res.getString(R.string.valutazione_studenti));
        valutazioneStudenti.setDescrizione(descr[1]);
        valutazioneStudenti.setTarget(StudentEvalutation.class);
        valutazioneStudenti.setClickable(true);

        widgets.addView(valutazioneStudenti);

        gestione=new DashboardWidgets(this);
        gestione.inflate();
        gestione.setIcon(getDrawable(R.drawable.manage_icon));//placeholder
        gestione.setNomeWidget(res.getString(R.string.gestione_appelli));
        gestione.setDescrizione(descr[2]);
        gestione.setTarget(ManageExam.class);
        gestione.setClickable(true);

        widgets.addView(gestione);

        profilo=new DashboardWidgets(this);
        profilo.inflate();
        profilo.setIcon(getDrawable(R.drawable.user_icon));//placeholder
        profilo.setNomeWidget(res.getString(R.string.profilo_professori));
        profilo.setDescrizione(descr[3]);
        profilo.setTarget(ProfessorProfile.class);
        profilo.setClickable(true);

        widgets.addView(profilo);

        chat=new DashboardWidgets(this);
        chat.inflate();
        chat.setIcon(getDrawable(R.drawable.chat_icon));//placeholder
        chat.setNomeWidget(res.getString(R.string.chat));
        chat.setDescrizione(descr[4]);
        chat.setTarget(ChatActivity.class);
        chat.setClickable(true);

        widgets.addView(chat);

        impostazioni=new DashboardWidgets(this);
        impostazioni.inflate();
        impostazioni.setIcon(getDrawable(R.drawable.settings_icon));//placeholder
        impostazioni.setNomeWidget(res.getString(R.string.settings));
        impostazioni.setDescrizione(descr[5]);
        impostazioni.setTarget(Settings.class);
        impostazioni.setClickable(true);

        widgets.addView(impostazioni);

        View.OnClickListener click= (View v) -> {
            DashboardWidgets dw = (DashboardWidgets) v;
            startActivity(new Intent(ProfessorHome.this, dw.getTarget()));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        };

        aggiuntaEsami.setOnClickListener(click);
        valutazioneStudenti.setOnClickListener(click);
        gestione.setOnClickListener(click);
        profilo.setOnClickListener(click);
        chat.setOnClickListener(click);
        impostazioni.setOnClickListener(click);

        prof=new ProfessorData();

        DatabaseReference dr= FirebaseDatabase.getInstance().getReference().child("Docenti").child(DrawerActivity.sessionManager.getSessionEmail());
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot s) {
                prof.aula=s.child("aula").getValue(String.class);
                prof.cognome=s.child("cognome").getValue(String.class);
                prof.nome=s.child("nome").getValue(String.class);
                prof.settore=s.child("settore").getValue(String.class);
                DataSnapshot ins=s.child("insegnamenti");
                for(DataSnapshot d:ins.getChildren()){
                    ArrayList<String> esamiAssociati=new ArrayList<>();
                    for(int i =1;i<=d.getChildrenCount();i++){
                        esamiAssociati.add(d.child(""+i).getValue(String.class));
                    }
                    prof.insegnamenti.put(d.getKey(), esamiAssociati);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static ProfessorData getProfessor() {
        return prof;
    }

    public static boolean checkIfPresent(String insegnamento, String key){
        boolean isPresent=false;
        ArrayList<String> esame=prof.insegnamenti.get(insegnamento);
        for(String s:esame){
            if(s.equals(key)){
                isPresent=true;
                break;
            }
        }
        return isPresent;
    }
}