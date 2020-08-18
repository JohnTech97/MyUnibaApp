package sms.myunibapp.unibaServices;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myunibapp.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sms.myunibapp.ExamsData;
import sms.myunibapp.Home;
import sms.myunibapp.Login;
import sms.myunibapp.advancedViews.ExamOutcomeDetails;

public class OutcomeBoard extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_exam_outcome);
        DrawerLayout drawer=findViewById(R.id.menu_navigazione_outcome);
        Toolbar toolbar=findViewById(R.id.menu_starter_outcome);
        NavigationView nav= findViewById(R.id.navigation_menu_outcome);
        ActionBarDrawerToggle mainMenu = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(mainMenu);
        mainMenu.syncState();
        nav.bringToFront();
        nav.setNavigationItemSelectedListener(Home.getNavigationBarListener(this));

        LinearLayout listaEsiti = findViewById(R.id.esiti);

        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Studente").child(Login.getUsername());

        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot sn) {
                DataSnapshot s=sn.child("Esami superati");
                //se non ci sono esami rimarrà la scritta "nessun esito da mostrare"
                if (s.getChildrenCount()!=0){
                    findViewById(R.id.esiti_da_mostrare).setVisibility(View.GONE);
                    for(DataSnapshot d: s.getChildren()){
                        ExamsData.Esame esame= ExamsData.getEsame(d.getKey());

                        ExamOutcomeDetails esito=new ExamOutcomeDetails(OutcomeBoard.this);
                        esito.inflate();

                        esito.setNomeEsame(esame.getNome());
                        esito.setEsito(d.child("voto").getValue(String.class));
                        esito.setData(d.child("data").getValue(String.class));
                        esito.setDocente(esame.getDocente());
                        //se l'esito non è positivo il pulsante avrà visibilità GONE, per cui non sarà possibile interagirvi
                        esito.getPulsanteAccetta().setOnClickListener((View v) -> {
                            finish();
                        });
                        esito.getRefuseBack().setOnClickListener((View v) -> {
                            Resources res=getResources();
                            if(esito.getRefuseBack().getText().equals(res.getString(R.string.refuse_and_back))){
                                dr.child("Esami da superare").child(""+(sn.child("Esami da superare").getChildrenCount()+1)).setValue(d.getKey());
                                s.getRef().child(d.getKey()).setValue(null);
                            }else{
                                Toast.makeText(OutcomeBoard.this, res.getString(R.string.back_message), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                        listaEsiti.addView(esito);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
