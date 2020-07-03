package sms.myunibapp;

public class Utente {

    private int matricola, annoDiRegolamento, ordinamento;
    private String username, nome, cognome, corsoDiLaurea, percorso, sede, iscrizione, classe, gender, normativa;
    private boolean stato;

    public Utente(int matricola, int annoDiRegolamento, int ordinamento, String username, String nome, String cognome,
                  String corsoDiLaurea, String percorso, String sede, String iscrizione, String classe, String gender, String normativa, boolean stato) {
        this.matricola = matricola;
        this.annoDiRegolamento = annoDiRegolamento;
        this.ordinamento = ordinamento;
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.corsoDiLaurea = corsoDiLaurea;
        this.percorso = percorso;
        this.sede = sede;
        this.iscrizione = iscrizione;
        this.classe = classe;
        this.gender = gender;
        this.normativa = normativa;
        this.stato = stato;
    }

    public int getMatricola() {
        return matricola;
    }

    public int getAnnoDiRegolamento() {
        return annoDiRegolamento;
    }

    public int getOrdinamento() {
        return ordinamento;
    }

    public String getUsername() {
        return username;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getCorsoDiLaurea() {
        return corsoDiLaurea;
    }

    public String getPercorso() {
        return percorso;
    }

    public String getSede() {
        return sede;
    }

    public String getIscrizione() {
        return iscrizione;
    }

    public String getClasse() {
        return classe;
    }

    public String getGender() {
        return gender;
    }

    public String getNormativa() {
        return normativa;
    }

    public boolean isStato() {
        return stato;
    }
}
