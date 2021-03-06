package sms.myunibapp.principale;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myunibapp.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import sms.myunibapp.Constants.FirebaseDb;
import sms.myunibapp.advancedViews.DashboardWidgets;
import sms.myunibapp.oggetti.DrawerActivity;
import sms.myunibapp.profileUser.Profile;
import sms.myunibapp.unibaServices.BookableExams;
import sms.myunibapp.unibaServices.BookingsBoard;
import sms.myunibapp.unibaServices.Booklet;
import sms.myunibapp.unibaServices.ChatActivity;
import sms.myunibapp.unibaServices.LuoghiDiInteresse;
import sms.myunibapp.unibaServices.OutcomeBoard;
import sms.myunibapp.unibaServices.Secretary;


public class HomeActivity extends DrawerActivity {

    private DrawerLayout drawer;
    private GridLayout layout;
    private AlertDialog dialog;
    private SharedPreferences editor;

    /**
     * TITOLO NOME UTENTE
     */
    private TextView nome, matricola;
    private CircleImageView profilePic;
    private FirebaseStorage storage;


    /* ACCESSO AL DATABASE */
    private DatabaseReference userReference;

    //riguardo la fine del caricamento dei dati
    private static boolean isFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_dashboard);

        layout = findViewById(R.id.widgets);

        //Creazione della parte superiore della pagina
        initializeView();
        createHeaderDash();

        /* INIZIALIZZAZIONE DATI ESAMI */
        ExamsData.initializeData(this);

        //il timer per determinare se il caricamento è completo parte da ora

        //se si seleziona una schermata troppo velocemente il sistema rischia di crashare
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                isFinished = true;
            }
        }.start();

        //Immagine cliccabile e si può modificare
        profilePic.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, Profile.class)));
        getSupportActionBar().setTitle("Dashboard");

        /**
         * AGGIUNTA DEI WIDGETS
         */
        FloatingActionButton fb = findViewById(R.id.add_widget);
        fb.setOnClickListener((View v) -> dialog.show());

        //nav.bringToFront();
        //nav.setNavigationItemSelectedListener(getNavigationBarListener(this));
        editor = getSharedPreferences("Widgets", MODE_PRIVATE);

        initializeWidgetsCustomizationPanel();
        initializeWidgets();

    }

    /**
     * Inizializzazione dei valori dell'header della dash
     */
    private void initializeView() {
        matricola = findViewById(R.id.matricola);
        nome = findViewById(R.id.utente_nome);

        //DATI PER IL CARICAMENTO DELL'IMMAGINE
        profilePic = findViewById(R.id.image_of_profile);
        storage = FirebaseStorage.getInstance();
    }

    private void createHeaderDash(){

        /* ACCESSO AL DATABASE */
        userReference = FirebaseDatabase.getInstance().getReference().child("Studente").child(sessionManager.getSessionEmail());

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nome.setText(snapshot.child(FirebaseDb.USER_NOME).getValue(String.class));
                matricola.setText(snapshot.child(FirebaseDb.USER_MATRICOLA).getValue(String.class));
                String link = snapshot.child(FirebaseDb.USER_AVATAR).getValue(String.class);
                showImage(link);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void showImage(String link) {

        Drawable image = getResources().getDrawable(R.drawable.icon_man);

        if(link.isEmpty()){
            profilePic.setImageDrawable(image);
        }else {
            int size = profilePic.getWidth();
            Picasso.get().load(link).resize(size, size).into(profilePic);
        }

    }

    //informazioni essenziali per automatizzare il più possibile la creazione dinamica dei widget
    private Class classes[] = new Class[]{
            Secretary.class,
            ChatActivity.class,
            Booklet.class,
            BookableExams.class,
            BookingsBoard.class,
            OutcomeBoard.class,
            Profile.class,
    };

    private String iconNames[] = new String[]{
            "segretary_icon",
            "chat_icon",
            "career_icon",
            "exam_list_icon",
            "exam_booking_icon",
            "exam_results_icon",
            "user_icon"
    };
    private String widgetMessages[] = new String[classes.length];
    private CheckBox items[] = new CheckBox[classes.length];

    private void initializeWidgetsCustomizationPanel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ViewGroup view = (ViewGroup) getLayoutInflater().inflate(R.layout.widget_customization_panel, null);

        Resources res = getResources();
        //prima inizializzo le checkbox
        for (int i = 0; i < classes.length; i++) {
            items[i] = view.findViewById(res.getIdentifier("option" + (i + 1), "id", getPackageName()));
            items[i].setChecked(editor.getBoolean("IsSelected" + (i + 1), false));
            widgetMessages[i] = items[i].getText().toString();
        }

        builder.setView(view).setNegativeButton(getResources().getString(R.string.dialogDecline), null)
                .setPositiveButton(getResources().getString(R.string.dialogConfirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor edit = editor.edit();
                        edit.clear().apply();//ripulisco le informazioni precedenti prima di inserire i nuovi
                        layout.removeAllViews();
                        int count = 1;
                        for (int i = 0; i < classes.length; i++) {
                            if (items[i].isChecked()) {
                                edit.putString("icon" + count, iconNames[i]);
                                edit.putString("ClasseTarget" + count, classes[i].getName());
                                edit.putBoolean("IsSelected" + (i + 1), true);//per fare in modo di far ritrovare le stesse opzioni selezionate
                                //la prossima volta che si accede alla schermata
                                count++;
                            }
                        }

                        count--;

                        edit.putInt("numberOfWidgets", count);
                        edit.apply();

                        initializeWidgets();
                    }
                });
        dialog = builder.create();
    }


    /**
     * Questo metodo serve a inizializzare gli oggetti
     */
    private void initializeWidgets() {
        Resources res = getResources();
        if (!editor.contains("numberOfWidgets")) {//controllo se esistono le shared preferences, in modo da creare le 4 scorciatoie di default se non ci fossero (primo avvio)
            DashboardWidgets profilo, libretto, calendario, esiti;
            profilo = new DashboardWidgets(this);
            profilo.inflate();
            profilo.setIcon(getDrawable(R.drawable.missing_icon));//placeholder
            profilo.setNomeWidget(items[0].getText().toString());
            profilo.setTarget(Profile.class);
            profilo.setClickable(true);

            layout.addView(profilo);

            libretto = new DashboardWidgets(this);
            libretto.inflate();
            libretto.setIcon(getDrawable(R.drawable.missing_icon));//placeholder
            libretto.setNomeWidget(items[1].getText().toString());
            libretto.setTarget(Secretary.class);
            libretto.setClickable(true);

            layout.addView(libretto);

            calendario = new DashboardWidgets(this);
            calendario.inflate();
            calendario.setIcon(getDrawable(R.drawable.missing_icon));//placeholder
            calendario.setNomeWidget(items[3].getText().toString());
            calendario.setTarget(BookableExams.class);
            calendario.setClickable(true);

            layout.addView(calendario);

            esiti = new DashboardWidgets(this);
            esiti.inflate();
            esiti.setIcon(getDrawable(R.drawable.missing_icon));//placeholder
            esiti.setNomeWidget(items[4].getText().toString());
            esiti.setTarget(OutcomeBoard.class);
            esiti.setClickable(true);

            View.OnClickListener click = (View v) -> {
                if (isFinished) {
                    DashboardWidgets dw = (DashboardWidgets) v;
                    startActivity(new Intent(HomeActivity.this, dw.getTarget()));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            };

            profilo.setOnClickListener(click);
            libretto.setOnClickListener(click);
            calendario.setOnClickListener(click);
            esiti.setOnClickListener(click);

            items[0].setChecked(true);//seleziono i 4 item di default
            items[1].setChecked(true);
            items[2].setChecked(false);
            items[3].setChecked(true);
            items[4].setChecked(true);
            items[5].setChecked(false);

            layout.addView(esiti);
        } else {

            int numberOfWidgets = editor.getInt("numberOfWidgets", 0);
            int[] indici = new int[numberOfWidgets];
            //capisco quali checkbox sono stati selezionati, per poi inizializzare solamente i widget associati alle checkbox selezionate
            for (int i = 0, count = 0; i < items.length; i++) {
                if (items[i].isChecked()) {
                    indici[count] = i;
                    count++;
                }
            }
            String descr[]=res.getStringArray(R.array.home_widgets_descriptions);
            for (int i = 0; i < numberOfWidgets; i++) {
                String value = editor.getString("icon" + (i+1), "");
                Drawable icon = getDrawable(res.getIdentifier(value, "drawable", getPackageName()));

                String titolo = items[indici[i]].getText().toString();
                String des = descr[indici[i]];
                Class target = null;
                try {
                    //ottengo la classe con un sistema automatico, senza quindi dover usare uno switch che comprometterebbe l'automazione, e quindi la scalabilità
                    target = Class.forName(editor.getString("ClasseTarget" + (i+1), ""));
                } catch (ClassNotFoundException e) {
                }
                //ho tutte le informazioni necessarie per inizializzare un widget
                DashboardWidgets widget = new DashboardWidgets(this);
                widget.inflate();
                widget.setIcon(icon);
                widget.setNomeWidget(titolo);
                widget.setDescrizione(des);
                widget.setTarget(target);
                widget.setClickable(true);
                widget.setOnClickListener(v -> {
                    DashboardWidgets dw = (DashboardWidgets) v;
                    startActivity(new Intent(HomeActivity.this, dw.getTarget()));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                });

                layout.addView(widget);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            close();
        }
    }

    /**
     * Funzione di chiusura dell'applicazione
     */
    private void close() {
        Resources res = getResources();
        AlertDialog.Builder conferma = new AlertDialog.Builder(getApplicationContext());
        conferma.setTitle(res.getString(R.string.exitDialogTitle));
        conferma.setMessage(res.getString(R.string.exitDialogMessage));
        conferma.setPositiveButton(res.getString(R.string.dialogConfirm), (dialog, which) -> {
            finish();
            sessionManager.logout();
        });
        conferma.setNegativeButton(res.getString(R.string.dialogDecline), (dialog, which) -> dialog.cancel());
        conferma.show();
    }
}