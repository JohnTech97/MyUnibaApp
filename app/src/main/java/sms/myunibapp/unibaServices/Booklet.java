package sms.myunibapp.unibaServices;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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

import sms.myunibapp.principale.ExamsData;
import sms.myunibapp.principale.HomeActivity;
import sms.myunibapp.accessApp.LoginActivity;

public class Booklet extends AppCompatActivity {

    private boolean isShowDetailsSelected = false;

    private int dimensioneStato=50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libretto);
        DrawerLayout drawer=findViewById(R.id.menu_navigazione_booklet);
        Toolbar toolbar=findViewById(R.id.menu_starter_booklet);
        NavigationView nav= findViewById(R.id.navigation_menu_booklet);
        ActionBarDrawerToggle mainMenu = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(mainMenu);
        mainMenu.syncState();
        nav.bringToFront();
        nav.setNavigationItemSelectedListener(HomeActivity.getNavigationBarListener(this));

        ScrollView scrollView = findViewById(R.id.scrollview_libretto);
        TableLayout tabellaEsami = findViewById(R.id.tabella_libretto);
        Button showDetails = findViewById(R.id.show_details);
        TextView ma=findViewById(R.id.media_aritmetica);
        TextView mp=findViewById(R.id.media_ponderata);
        TextView perc=findViewById(R.id.percentuale_esami);
        TextView libretto=findViewById(R.id.activity_libretto);

        libretto.setText(getResources().getString(R.string.booklet));

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

        altezza.height = Resources.getSystem().getDisplayMetrics().heightPixels - 1100; //per fare in modo di visualizzare la parte inferiore del content view senza che la scrollview prenda il sopravvento

        scrollView.setLayoutParams(altezza);

       /* DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Studente").child(LoginActivity.getUsername());

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

                    TextView nome = null, anno = null, cfu = null, voto = null, data = null;
                    ImageView stato;
                    nome = initializeTextView(nome);
                    anno = initializeTextView(anno);
                    cfu = initializeTextView(cfu);
                    stato = new ImageView(Booklet.this);
                    stato.setImageDrawable(getDrawable(R.drawable.non_in_programma));
                    stato.setAdjustViewBounds(true);
                    stato.setMaxWidth(dimensioneStato);
                    stato.setMaxHeight(dimensioneStato);
                    voto = initializeTextView(voto);
                    data = initializeTextView(data);

                    nome.setText(esame.getNome());
                    anno.setText("" + esame.getAnno());
                    cfu.setText("" + esame.getCfu());
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

                    TextView nome = null, anno = null, cfu = null, voto = null, data = null;
                    ImageView stato;
                    nome = initializeTextView(nome);
                    anno = initializeTextView(anno);
                    cfu = initializeTextView(cfu);
                    stato = new ImageView(Booklet.this);
                    stato.setImageDrawable(getDrawable(R.drawable.programmato));
                    stato.setAdjustViewBounds(true);
                    stato.setMaxWidth(dimensioneStato);
                    stato.setMaxHeight(dimensioneStato);
                    voto = initializeTextView(voto);
                    data = initializeTextView(data);

                    nome.setText(esame.getNome());
                    anno.setText("" + esame.getAnno());
                    cfu.setText("" + esame.getCfu());
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

                    TextView nome = null, anno = null, cfu = null, voto = null, data = null;
                    ImageView stato;
                    nome = initializeTextView(nome);
                    anno = initializeTextView(anno);
                    cfu = initializeTextView(cfu);
                    stato = new ImageView(Booklet.this);
                    stato.setImageDrawable(getDrawable(R.drawable.superato));
                    stato.setAdjustViewBounds(true);
                    stato.setMaxWidth(dimensioneStato);
                    stato.setMaxHeight(dimensioneStato);
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
                        data.setText(d.child("data").getValue(String.class));
                    } catch (NumberFormatException e) {//se l'esito non è un numero vuol dire che non è stato superato
                        stato.setImageDrawable(getDrawable(R.drawable.non_in_programma));
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

*/
    }

    private TextView initializeTextView(TextView t) {//metodo di utilità per non dover definire ogni volta gli stessi parametri per ogni singola textview
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
