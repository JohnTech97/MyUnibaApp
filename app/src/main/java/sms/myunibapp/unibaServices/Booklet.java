package sms.myunibapp.unibaServices;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myunibapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sms.myunibapp.ExamsData;
import sms.myunibapp.Login;

public class Booklet extends AppCompatActivity {

    private boolean isShowDetailsSelected=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libretto);

        ScrollView scrollView = findViewById(R.id.scrollview_libretto);

        TableLayout tabellaEsami=findViewById(R.id.tabella_libretto);

        Button showDetails=findViewById(R.id.show_details);

        showDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //inverto le colonne mostrate
                tabellaEsami.setColumnCollapsed(0, !isShowDetailsSelected);
                tabellaEsami.setColumnCollapsed(1, isShowDetailsSelected);
                tabellaEsami.setColumnCollapsed(2, !isShowDetailsSelected);
                tabellaEsami.setColumnCollapsed(3, isShowDetailsSelected);
                tabellaEsami.setColumnCollapsed(4, !isShowDetailsSelected);
                tabellaEsami.setColumnCollapsed(5, isShowDetailsSelected);

                if(isShowDetailsSelected){
                    showDetails.setText("Maggiori informazioni");
                }else{
                    showDetails.setText("Nascondi dettagli");
                }

                isShowDetailsSelected=!isShowDetailsSelected;

            }
        });

        LinearLayout.LayoutParams altezza = (LinearLayout.LayoutParams) scrollView.getLayoutParams();

        altezza.height = Resources.getSystem().getDisplayMetrics().heightPixels-300;
        System.out.println(altezza.height);

        scrollView.setLayoutParams(altezza);

        DatabaseReference dr= FirebaseDatabase.getInstance().getReference().child("Studente").child(Login.getUsername());

        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot daSuperare=snapshot.child("Esami da superare");
                DataSnapshot prenotati=snapshot.child("Esami prenotati");
                DataSnapshot superati=snapshot.child("Esami superati");

                for(DataSnapshot d:daSuperare.getChildren()){
                    ExamsData.Esame esame= ExamsData.getEsame(d.getValue(String.class));
                    TableRow riga= new TableRow(Booklet.this);
                    riga.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

                    TextView nome=null, anno=null, cfu=null, stato=null, voto=null, data=null;
                    nome=initializeTextView(nome);
                    anno=initializeTextView(anno);
                    cfu=initializeTextView(cfu);
                    stato=initializeTextView(stato);
                    voto=initializeTextView(voto);
                    data=initializeTextView(data);

                    nome.setText(esame.getNome());
                    anno.setText(""+esame.getAnno());
                    cfu.setText(""+esame.getCfu());
                    stato.setText("Non in programma");//perché si trova negli esami da superare
                    voto.setText("/");
                    data.setText("/");

                    riga.addView(nome);
                    riga.addView(anno);
                    riga.addView(cfu);
                    riga.addView(stato);
                    riga.addView(voto);
                    riga.addView(data);

                    tabellaEsami.addView(riga);

                }

                for(int i=1; i<=prenotati.getChildrenCount()/4;i++){
                    ExamsData.Esame esame= ExamsData.getEsame(prenotati.child("nome"+i).getValue(String.class));
                    TableRow riga= new TableRow(Booklet.this);
                    riga.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

                    TextView nome=null, anno=null, cfu=null, stato=null, voto=null, data=null;
                    nome=initializeTextView(nome);
                    anno=initializeTextView(anno);
                    cfu=initializeTextView(cfu);
                    stato=initializeTextView(stato);
                    voto=initializeTextView(voto);
                    data=initializeTextView(data);

                    nome.setText(esame.getNome());
                    anno.setText(""+esame.getAnno());
                    cfu.setText(""+esame.getCfu());
                    stato.setText("Programmato");//perché si trova negli esami prenotati
                    voto.setText("/");
                    data.setText("/");

                    riga.addView(nome);
                    riga.addView(anno);
                    riga.addView(cfu);
                    riga.addView(stato);
                    riga.addView(voto);
                    riga.addView(data);

                    tabellaEsami.addView(riga);

                }

                for(DataSnapshot d:superati.getChildren()){
                    ExamsData.Esame esame= ExamsData.getEsame(d.getKey());
                    TableRow riga= new TableRow(Booklet.this);
                    riga.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

                    TextView nome=null, anno=null, cfu=null, stato=null, voto=null, data=null;
                    nome=initializeTextView(nome);
                    anno=initializeTextView(anno);
                    cfu=initializeTextView(cfu);
                    stato=initializeTextView(stato);
                    voto=initializeTextView(voto);
                    data=initializeTextView(data);

                    nome.setText(esame.getNome());
                    anno.setText(""+esame.getAnno());
                    cfu.setText(""+esame.getCfu());
                    stato.setText("Superato");//perché si trova negli esami superati
                    voto.setText(""+d.child("voto").getValue(Integer.class));
                    data.setText(d.child("data").getValue(String.class));

                    riga.addView(nome);
                    riga.addView(anno);
                    riga.addView(cfu);
                    riga.addView(stato);
                    riga.addView(voto);
                    riga.addView(data);

                    tabellaEsami.addView(riga);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private TextView initializeTextView(TextView t){
        t=new TextView(this);
        t.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        t.setTextSize(20f);

        return t;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
