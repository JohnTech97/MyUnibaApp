package sms.myunibapp.principale;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myunibapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import sms.myunibapp.Constants.FirebaseDb;
import sms.myunibapp.advancedViews.DashboardWidgets;
import sms.myunibapp.profileUser.Profile;
import sms.myunibapp.unibaServices.BookableExams;
import sms.myunibapp.unibaServices.BookingsBoard;
import sms.myunibapp.unibaServices.Booklet;
import sms.myunibapp.unibaServices.LuoghiDiInteresse;
import sms.myunibapp.unibaServices.OutcomeBoard;
import sms.myunibapp.unibaServices.Secretary;


public class HomeActivity extends DrawerActivity {

    private DrawerLayout drawer;
    private GridLayout layout;
    private AlertDialog dialog;
    private SharedPreferences editor;

    BottomAppBar bottomAppBar;

    /**
     * TITOLO NOME UTENTE
     */
    private TextView nome, matricola;
    private CircleImageView profilePic;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;


    /* ACCESSO AL DATABASE */
    private DatabaseReference userReference;

    //riguardo la fine del caricamento dei dati
    private static boolean isFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_dashboard);

        drawer = findViewById(R.id.menu_navigazione);
        NavigationView nav = findViewById(R.id.navigation_menu);
        Toolbar toolbar = findViewById(R.id.menu_starter);
        layout = findViewById(R.id.widgets);

        //Creazione della parte superiore della pagina
        initializeView();
        createHeaderDash();


        /* INIZIALIZZAZIONE DATI ESAMI */
        //ExamsData.initializeData(this);

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
        setSupportActionBar(toolbar);

       // ActionBarDrawerToggle mainMenu = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer);
       // drawer.addDrawerListener(mainMenu);
       // mainMenu.syncState();

        //Immagine cliccabile e si può modificare
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });


        /**
         * AGGIUNTA DEI WIDGETS
         */
        //FloatingActionButton FloatingActionButton = findViewById(R.id.add_widget);
        //FloatingActionButton.setOnClickListener((View v) -> dialog.show());

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
        storageReference = storage.getReference();
    }

    private void createHeaderDash(){

        /* ACCESSO AL DATABASE */
        userReference = FirebaseDatabase.getInstance().getReference().child("Studente").child(sessionManager.getSessionEmail());

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot s) {
                nome.setText(s.child(FirebaseDb.USER_NOME).getValue(String.class));
                matricola.setText(s.child(FirebaseDb.USER_MATRICOLA).getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * Funzione per scegliere l'immagine
     */
    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data!=null && data.getData()!= null){
            imageUri = data.getData();
            profilePic.setImageURI(imageUri);
            uploadPicture();
        }
    }

    private void uploadPicture() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading image...");
        progressDialog.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("images/");

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();

                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded.", Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_LONG).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.setMessage("Progress: " + (int) progressPercent + "%");
            }
        });
    }

    //serve un unico listener da utilizzare in tutte le schermate
    public static NavigationView.OnNavigationItemSelectedListener getNavigationBarListener(AppCompatActivity app) {
        return item -> {
            Class target = null;

            switch (item.getItemId()) {
                case R.id.home:
                    target = HomeActivity.class;
                    break;
                case R.id.segreteria:
                    target = Secretary.class;
                    break;
                case R.id.lista_esami:
                    target = BookableExams.class;
                    break;
                case R.id.bacheca_prenotazioni:
                    target = BookingsBoard.class;
                    break;
                case R.id.bacheca_esiti:
                    target = OutcomeBoard.class;
                    break;
                case R.id.carriera:
                    target = Booklet.class;
                    break;
                case R.id.profilo_menu:
                    target = Profile.class;
                    break;
                case R.id.who_we_are:

                    break;
                case R.id.mappa:
                    target = LuoghiDiInteresse.class;
                    break;
                case R.id.impostazioni:
                    target = Settings.class;
                    break;
                case R.id.logout:
                    break;
            }//il target deve essere diverso dalla schermata corrente, per evitare di tornare inutilmente nella stessa pagina
            if (target != null && target != app.getClass() && isFinished) {//se la progress bar è visibile vuol dire che non ha finito di caricare
                app.startActivity(new Intent(app, target));
                app.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
            return true;
        };
    }

    //informazioni essenziali per automatizzare il più possibile la creazione dinamica dei widget
    private Class classes[] = new Class[]{
            Secretary.class,
            Booklet.class,
            BookableExams.class,
            BookingsBoard.class,
            OutcomeBoard.class,
            Profile.class
    };

    private String iconNames[] = new String[]{
            "segretary_icon",
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
            profilo.setTarget(Secretary.class);
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
            for (int i = 1; i <= numberOfWidgets; i++) {
                String value = editor.getString("icon" + i, "");
                Drawable icon = getDrawable(res.getIdentifier(value, "drawable", getPackageName()));

                String testo = items[indici[i - 1]].getText().toString();
                Class target = null;
                try {
                    //ottengo la classe con un sistema automatico, senza quindi dover usare uno switch che comprometterebbe l'automazione, e quindi la scalabilità
                    target = Class.forName(editor.getString("ClasseTarget" + i, ""));
                } catch (ClassNotFoundException e) {
                }
                //ho tutte le informazioni necessarie per inizializzare un widget
                DashboardWidgets widget = new DashboardWidgets(this);
                widget.inflate();
                widget.setIcon(icon);
                widget.setNomeWidget(testo);
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
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            close();
        }
    }

    /**
     * Funzione di chiusura dell'applicazione
     */
    private void close() {
        Resources res = getResources();
        AlertDialog.Builder conferma = new AlertDialog.Builder(HomeActivity.this);
        conferma.setTitle(res.getString(R.string.exitDialogTitle));
        conferma.setMessage(res.getString(R.string.exitDialogMessage));
        conferma.setPositiveButton(res.getString(R.string.dialogConfirm), (dialog, which) -> {
            finish();
            System.exit(0);
        });
        conferma.setNegativeButton(res.getString(R.string.dialogDecline), (dialog, which) -> dialog.cancel());
        conferma.show();
    }
}