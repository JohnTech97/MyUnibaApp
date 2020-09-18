package sms.myunibapp.unibaServices;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import sms.myunibapp.oggetti.DrawerActivity;
import sms.myunibapp.principale.ExamsData;
import sms.myunibapp.principale.HomeActivity;
import sms.myunibapp.advancedViews.BookableExamItem;

public class BookableExams extends AppCompatActivity {

    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookable_exams_list);

        LinearLayout listaEsami = findViewById(R.id.lista_esami_prenotabili);

        db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference().child("Studente").child(DrawerActivity.sessionManager.getSessionEmail()).child("Esami da superare");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot data) {

                Resources r = getResources();

                TextView nEsami = findViewById(R.id.n_esami);
                int count = (int) data.getChildrenCount();

                nEsami.setText(r.getQuantityString(R.plurals.exams_to_pass, count, count));
                if (count == 1 || count == 0) {
                    nEsami.setTextColor(Color.GREEN);
                }

                //per ogni esame da superare ottengo le relative informazioni e le fornisco alla view
                for (DataSnapshot d : data.getChildren()) {
                    BookableExamItem esame = new BookableExamItem(BookableExams.this);
                    ExamsData.Esame exam = ExamsData.getEsame(d.getValue(String.class));

                    esame.inflate();
                    esame.setTitoloEsame(exam.getNome());
                    esame.setLink(exam.getKey());
                    esame.setCfu(exam.getCfu());
                    esame.setEsamiPrenotabili(exam.getDate().size());
                    esame.setClickable(true);
                    esame.setOnClickListener(v -> {
                        BookableExamItem ep = (BookableExamItem) v;
                        if (ep.getNEsamiPrenotabili() == 0) {//se non ci sono appelli disponibili
                            Toast.makeText(BookableExams.this, r.getString(R.string.zero_exams_to_book), Toast.LENGTH_SHORT).show();
                        } else {
                            Intent i = new Intent(BookableExams.this, BookableExamsDetails.class);
                            //fornisco queste informazioni nell'intent per evitare di dover leggere le stesse informazioni un'altra volta da firebase in seguito
                            ExamsData.Esame exam1 = ExamsData.getEsame(ep.getLink());
                            i.putExtra("key", ep.getLink());
                            i.putExtra("nomeEsame", exam1.getNome());
                            i.putExtra("tipo", exam1.getTipo());
                            i.putExtra("dateAppelli", exam1.getDate());
                            i.putExtra("edifici", exam1.getEdifici());
                            i.putExtra("aule", exam1.getAule());
                            i.putExtra("docente", exam1.getDocente());
                            i.putExtra("nEsami", ep.getNEsamiPrenotabili());
                            startActivityForResult(i, 0);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    });
                    Space space = new Space(BookableExams.this);
                    space.setMinimumHeight(24);
                    listaEsami.addView(space);
                    listaEsami.addView(esame);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            recreate();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}