package sms.myunibapp;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Esami {

    public static class Esame{
        //firebase
        private int anno, cfu;
        private String docente, nome;
        private Map<String, String> date=new HashMap<>();

        public Esame(){

        }

        public int getAnno() {
            return anno;
        }

        public void setAnno(int anno) {
            this.anno = anno;
        }

        public int getCfu() {
            return cfu;
        }

        public void setCfu(int cfu) {
            this.cfu = cfu;
        }

        public String getDocente() {
            return docente;
        }

        public void setDocente(String docente) {
            this.docente = docente;
        }

        public Map<String, String> getDate() {
            return date;
        }

        public void setDate(Map<String, String> date) {
            this.date = date;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }
    }

    private static Map<String, Esame> esami=new HashMap<>();

    private Esami(){}
    static Context ct;
    static String nomecorso;
    public static String initializeData(Context c){
        FirebaseDatabase db= FirebaseDatabase.getInstance();
        
        ct=c;
        db.getReference().child("CorsoDiLaurea").child("L-31").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot data) {
                nomecorso= data.child("NomeCorso").getValue(String.class);

                /*for(int i=1; i<=data.getChildrenCount(); i++) {
                    Esame e = new Esame();
                    DataSnapshot esameN=data.child(""+i);
                    e.setAnno(esameN.child("anno").getValue(Integer.class));
                    e.setCfu(esameN.child("cfu").getValue(Integer.class));
                    e.setDocente(esameN.child("docente").getValue(String.class));
                    e.setNome(esameN.child("nome").getValue(String.class));
                    Map <String, String>exams=new HashMap();
                    esameN=esameN.child("date appelli");
                    for(int j=1;j<=esameN.getChildrenCount();j++){
                        exams.put(""+j, esameN.child(""+j).getValue(String.class));
                    }
                    e.setDate(exams);
                    esami.put(e.getNome(), e);
                    Toast.makeText(c, e.getNome(),Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return nomecorso;
    }

    public static Esame getEsame(String nomeEsame){
        return esami.get(nomeEsame);
    }

    public static Esame getEsame(int esame){
        return (Esame) esami.values().toArray()[esame];
    }

    public static int getSize(){
        return esami.size();
    }
}
