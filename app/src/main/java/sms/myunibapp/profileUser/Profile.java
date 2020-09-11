package sms.myunibapp.profileUser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myunibapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.io.File;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import sms.myunibapp.Constants.FirebaseDb;
import sms.myunibapp.principale.DrawerActivity;
import sms.myunibapp.principale.HomeActivity;
import sms.myunibapp.accessApp.LoginActivity;

public class Profile extends DrawerActivity {

    /* ACCESSO AL DATABASE */
    private DatabaseReference userReference = FirebaseDatabase.getInstance().getReference();

    private TextView matricola, nome, cognome, corsoDiLaurea, percorso, sede, iscrizione, stato, classe, annoDiRegolamento, ordinamento, normativa;
    private CircleImageView profilePic;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

       initDataLayout();

        /*DrawerLayout drawer=findViewById(R.id.menu_navigazione_profile);
        Toolbar toolbar=findViewById(R.id.menu_starter_profile);
        NavigationView nav= findViewById(R.id.navigation_menu_profile);
        ActionBarDrawerToggle mainMenu = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(mainMenu);
        mainMenu.syncState();
        nav.bringToFront();
        nav.setNavigationItemSelectedListener(HomeActivity.getNavigationBarListener(this));*/

        //Immagine cliccabile e si può modificare
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        userReference.child("Studente").child(sessionManager.getSessionEmail());

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot s) {
                nome.setText(s.child(FirebaseDb.USER_NOME).getValue(String.class));
                cognome.setText(s.child(FirebaseDb.USER_COGNOME).getValue(String.class));
                matricola.setText(s.child(FirebaseDb.USER_MATRICOLA).getValue(String.class));
                corsoDiLaurea.setText(s.child(FirebaseDb.USER_CORSODILAUREA).getValue(String.class));
                percorso.setText(s.child(FirebaseDb.USER_PERCORSO).getValue(String.class));
                sede.setText(s.child(FirebaseDb.USER_SEDE).getValue(String.class));
                iscrizione.setText(s.child(FirebaseDb.USER_ISCRIZIONE).getValue(String.class));
                stato.setText(s.child(FirebaseDb.USER_STATO).getValue(String.class));
                classe.setText(s.child(FirebaseDb.USER_CLASSE).getValue(String.class));
                annoDiRegolamento.setText(s.child(FirebaseDb.USER_ANNOREGOLAMENTO).getValue(String.class));
                ordinamento.setText(s.child(FirebaseDb.USER_ORDINAMENTO).getValue(String.class));
                normativa.setText(s.child(FirebaseDb.USER_NORMATIVA).getValue(String.class));
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

    /**
     * Inizializzazione dei parametri contenuti nel layout
     */
    private void initDataLayout() {
        matricola = findViewById(R.id.matricola);
        nome = findViewById(R.id.utente_nome);
        cognome = findViewById(R.id.utente_cognome);
        corsoDiLaurea = findViewById(R.id.corso_di_laurea);
        percorso = findViewById(R.id.percorso);
        sede = findViewById(R.id.sede);
        iscrizione = findViewById(R.id.iscrizione);
        stato = findViewById(R.id.stato);
        classe = findViewById(R.id.classe);
        annoDiRegolamento = findViewById(R.id.ADR);
        ordinamento = findViewById(R.id.ordinamento);
        normativa = findViewById(R.id.normativa);

        //DATI PER IL CARICAMENTO DELL'IMMAGINE
        profilePic = findViewById(R.id.image_of_profile);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
