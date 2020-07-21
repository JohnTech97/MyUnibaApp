package sms.myunibapp;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExamsData {

    public static class Esame{
        //firebase
        private int anno, cfu;
        private String docente, nome, tipo;
        private ArrayList<String> date=new ArrayList<>();
        private ArrayList<String> aule=new ArrayList<>();
        private ArrayList<String> edifici=new ArrayList<>();

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

        public ArrayList<String> getDate() {
            return date;
        }

        public void setDate(ArrayList<String> date) {
            this.date = date;
        }

        public ArrayList<String> getAule() {
            return aule;
        }

        public void setAule(ArrayList<String> aule) {
            this.aule = aule;
        }

        public ArrayList<String> getEdifici() {
            return edifici;
        }

        public void setEdifici(ArrayList<String> edifici) {
            this.edifici = edifici;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }
    }

    private static Map<String, Esame> esami=new HashMap<>();

    private ExamsData(){}

    public static void initializeData(){

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("CorsoDiLaurea").child("L-31").child("Esami");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot data) {
                for(int i=1; i<=data.getChildrenCount(); i++) {
                    Esame e = new Esame();
                    DataSnapshot esameN=data.child(""+i);
                    e.setAnno(esameN.child("anno").getValue(Integer.class));
                    e.setCfu(esameN.child("cfu").getValue(Integer.class));
                    e.setDocente(esameN.child("docente").getValue(String.class));
                    e.setNome(esameN.child("nome").getValue(String.class));
                    e.setTipo(esameN.child("tipo").getValue(String.class));
                    ArrayList <String> examsDate=new ArrayList<>();
                    ArrayList <String> examsAule=new ArrayList<>();
                    ArrayList <String> examsEdifici=new ArrayList<>();
                    esameN=esameN.child("appelli");
                    for(int j=1;j<=esameN.getChildrenCount()/3;j++){//diviso 3 perché ci saranno sempre 3 informazioni diverse da ricavare: aula, data e edificio
                        examsDate.add(esameN.child("data"+j).getValue(String.class));
                        examsAule.add(esameN.child("aula"+j).getValue(String.class));
                        examsEdifici.add(esameN.child("edificio"+j).getValue(String.class));
                    }
                    e.setDate(examsDate);
                    e.setAule(examsAule);
                    e.setEdifici(examsEdifici);
                    esami.put(e.getNome(), e);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
