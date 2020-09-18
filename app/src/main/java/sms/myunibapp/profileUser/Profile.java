package sms.myunibapp.profileUser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myunibapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import sms.myunibapp.Constants.FirebaseDb;
import sms.myunibapp.oggetti.DrawerActivity;

public class Profile extends DrawerActivity {

    private TextView matricola, nome, cognome, corsoDiLaurea, percorso, sede, iscrizione, stato, classe, annoDiRegolamento, ordinamento, normativa;
    private CircleImageView profilePic;
    public Uri imageUri;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

       initDataLayout();

        getSupportActionBar().setTitle("Profilo");

        /*
         * Immagine cliccabile e si può modificare
         */
        profilePic.setOnClickListener(v -> choosePicture());

        /*
         * Acquisizione dal database delle informazioni dell'utente
         */
        DatabaseReference data = userReference.child(sessionManager.getSessionEmail());

        /*
         * Settaggio dei valori acquisiti dal database
         */
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nome.setText(dataSnapshot.child(FirebaseDb.USER_NOME).getValue(String.class));
                cognome.setText(dataSnapshot.child(FirebaseDb.USER_COGNOME).getValue(String.class));
                matricola.setText(dataSnapshot.child(FirebaseDb.USER_MATRICOLA).getValue(String.class));
                corsoDiLaurea.setText(dataSnapshot.child(FirebaseDb.USER_CORSODILAUREA).getValue(String.class));
                percorso.setText(dataSnapshot.child(FirebaseDb.USER_PERCORSO).getValue(String.class));
                sede.setText(dataSnapshot.child(FirebaseDb.USER_SEDE).getValue(String.class));
                iscrizione.setText(dataSnapshot.child(FirebaseDb.USER_ISCRIZIONE).getValue(String.class));
                stato.setText(dataSnapshot.child(FirebaseDb.USER_STATO).getValue(String.class));
                classe.setText(dataSnapshot.child(FirebaseDb.USER_CLASSE).getValue(String.class));
                annoDiRegolamento.setText(dataSnapshot.child(FirebaseDb.USER_ANNOREGOLAMENTO).getValue(String.class));
                ordinamento.setText(dataSnapshot.child(FirebaseDb.USER_ORDINAMENTO).getValue(String.class));
                normativa.setText(dataSnapshot.child(FirebaseDb.USER_NORMATIVA).getValue(String.class));
                String link = dataSnapshot.child(FirebaseDb.USER_AVATAR).getValue(String.class);
                int size=profilePic.getWidth();
                Picasso.get().load(link).resize(size, size).into(profilePic);
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

    /**
     * Aggiornamento dell'immagine del profilo
     */
    private void uploadPicture() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading image...");
        progressDialog.show();

        final String keyUser = sessionManager.getSessionUsername();
        StorageReference riversRef = storageReference.child(keyUser);

        riversRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    //Nel caso in cui il carimento sia avvenuto con successo
                    progressDialog.dismiss();
                    //https://firebasestorage.googleapis.com/v0/b/myunibaapp.appspot.com/o/images%2Fd.foule?alt=media&token=a26aa0bf-7288-4dd0-8de4-0a88f7b61ae1
                    updatePicturePath(keyUser);
                    Snackbar.make(findViewById(android.R.id.content), "Image Uploaded.", Snackbar.LENGTH_LONG).show();
                })
                .addOnFailureListener(exception -> {
                    //Nel caso in cui il carimento non sia avvenuto con successo
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_LONG).show();
                }).addOnProgressListener(snapshot -> {
                    //Barra di caricamento
                    double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressDialog.setMessage("Progress: " + (int) progressPercent + "%");
                });
    }

    /**
     * Aggiornamento dell'immagine del profilo
     * @param keyUser
     */
    private void updatePicturePath(String keyUser) {

        DatabaseReference updateData = userReference.child(sessionManager.getSessionEmail()).child(FirebaseDb.USER_AVATAR);

        final String[] path = new String[1];

        storageReference.child(keyUser).getDownloadUrl().addOnSuccessListener(uri -> {
            path[0] = uri.toString();// The string(file link) that you need
        });


        updateData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    updateData.setValue(path[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        profilePic = findViewById(R.id.pictureProfile);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
