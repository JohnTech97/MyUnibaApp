package sms.myunibapp.unibaServices;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
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

    private boolean isShowDetailsSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libretto);

        ScrollView scrollView = findViewById(R.id.scrollview_libretto);

        TableLayout tabellaEsami = findViewById(R.id.tabella_libretto);

        Button showDetails = findViewById(R.id.show_details);

        TextView ma=findViewById(R.id.media_aritmetica);

        TextView mp=findViewById(R.id.media_ponderata);

        TextView perc=findViewById(R.id.percentuale_esami);

        showDetails.setOnClickListener(v -> {
            //inverto le colonne mostrate
            tabellaEsami.setColumnCollapsed(0, !isShowDetailsSelected);
            tabellaEsami.setColumnCollapsed(1, isShowDetailsSelected);
            tabellaEsami.setColumnCollapsed(2, !isShowDetailsSelected);
            tabellaEsami.setColumnCollapsed(3, isShowDetailsSelected);
            tabellaEsami.setColumnCollapsed(4, !isShowDetailsSelected);
            tabellaEsami.setColumnCollapsed(5, isShowDetailsSelected);

            if (isShowDetailsSelected) {
                showDetails.setText(getResources().getString(R.string.show_details));
            } else {
                showDetails.setText(getResources().getString(R.string.hide_details));
            }

            isShowDetailsSelected = !isShowDetailsSelected;

        });

        LinearLayout.LayoutParams altezza = (LinearLayout.LayoutParams) scrollView.getLayoutParams();

        altezza.height = Resources.getSystem().getDisplayMetrics().heightPixels - 600;

        scrollView.setLayoutParams(altezza);

        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Studente").child(Login.getUsername());

        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot daSuperare = snapshot.child("Esami da superare");
                DataSnapshot prenotati = snapshot.child("Esami prenotati");
                DataSnapshot superati = snapshot.child("Esami superati");

                int nEsami=0;

                for (DataSnapshot d : daSuperare.getChildren()) {
                    ExamsData.Esame esame = ExamsData.getEsame(d.getValue(String.class));
                    TableRow riga = new TableRow(Booklet.this);
                    riga.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

                    TextView nome = null, anno = null, cfu = null, stato = null, voto = null, data = null;
                    nome = initializeTextView(nome);
                    anno = initializeTextView(anno);
                    cfu = initializeTextView(cfu);
                    stato = initializeTextView(stato);
                    voto = initializeTextView(voto);
                    data = initializeTextView(data);

                    nome.setText(esame.getNome());
                    anno.setText("" + esame.getAnno());
                    cfu.setText("" + esame.getCfu());
                    stato.setText("Non in programma");//perché si trova negli esami da superare
                    voto.setText("/");
                    data.setText("/");

                    riga.addView(nome);
                    riga.addView(anno);
                    riga.addView(cfu);
                    riga.addView(stato);
                    riga.addView(voto);
                    riga.addView(data);

                    nEsami++;

                    tabellaEsami.addView(riga);

                }

                for (int i = 1; i <= prenotati.getChildrenCount() / 4; i++) {
                    ExamsData.Esame esame = ExamsData.getEsame(prenotati.child("nome" + i).getValue(String.class));
                    TableRow riga = new TableRow(Booklet.this);
                    riga.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

                    TextView nome = null, anno = null, cfu = null, stato = null, voto = null, data = null;
                    nome = initializeTextView(nome);
                    anno = initializeTextView(anno);
                    cfu = initializeTextView(cfu);
                    stato = initializeTextView(stato);
                    voto = initializeTextView(voto);
                    data = initializeTextView(data);

                    nome.setText(esame.getNome());
                    anno.setText("" + esame.getAnno());
                    cfu.setText("" + esame.getCfu());
                    stato.setText("Programmato");//perché si trova negli esami prenotati
                    voto.setText("/");
                    data.setText("/");

                    riga.addView(nome);
                    riga.addView(anno);
                    riga.addView(cfu);
                    riga.addView(stato);
                    riga.addView(voto);
                    riga.addView(data);

                    nEsami++;

                    tabellaEsami.addView(riga);

                }

                int sommaAritmetica=0;
                int sommaPesata=0;
                int sommaPesi=0;
                int percentuale=0;
                int nEsamiSuperati=0;

                for (DataSnapshot d : superati.getChildren()) {
                    ExamsData.Esame esame = ExamsData.getEsame(d.getKey());
                    TableRow riga = new TableRow(Booklet.this);
                    riga.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

                    TextView nome = null, anno = null, cfu = null, stato = null, voto = null, data = null;
                    nome = initializeTextView(nome);
                    anno = initializeTextView(anno);
                    cfu = initializeTextView(cfu);
                    stato = initializeTextView(stato);
                    voto = initializeTextView(voto);
                    data = initializeTextView(data);

                    nome.setText(esame.getNome());
                    anno.setText("" + esame.getAnno());
                    cfu.setText("" + esame.getCfu());
                    String esito = d.child("voto").getValue(String.class);
                    nEsami++;
                    try {//il child si chiama esami superati ma sarebbe meglio chiamarlo "esiti esami"
                        int votazione=Integer.parseInt(esito);
                        //calcolo media aritmetica
                        sommaAritmetica+=votazione;
                        nEsamiSuperati++;

                        //calcolo media pesata
                        sommaPesata+=votazione*esame.getCfu();
                        sommaPesi+=esame.getCfu();

                        percentuale++;
                        voto.setText(""+votazione);
                        stato.setText("Superato");//perché si trova negli esami superati
                        data.setText(d.child("data").getValue(String.class));
                    } catch (NumberFormatException e) {//se l'esito non è un numero vuol dire che non è stato superato
                        stato.setText("/");
                        voto.setText("/");
                        data.setText("/");
                    }

                    riga.addView(nome);
                    riga.addView(anno);
                    riga.addView(cfu);
                    riga.addView(stato);
                    riga.addView(voto);
                    riga.addView(data);

                    tabellaEsami.addView(riga);

                }


                float mediaAritmetica= 0f;
                float mediaPesata= 0f;
                if(nEsamiSuperati!=0){
                    mediaAritmetica=(float)sommaAritmetica/nEsamiSuperati;
                    mediaPesata=(float)sommaPesata/sommaPesi;
                }

                float percent=((float)percentuale/nEsami)*100f;

                Resources res=getResources();
                ma.setText(res.getString(R.string.arithmetic_mean, mediaAritmetica));
                mp.setText(res.getString(R.string.weighted_mean, mediaPesata));
                perc.setText(res.getString(R.string.percentage, (int)percent));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private TextView initializeTextView(TextView t) {
        t = new TextView(this);
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
