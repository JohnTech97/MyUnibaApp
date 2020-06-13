package sms.myunibapp;

public class Utente {

    private int matricola;
    private String username, email, gender;

    public Utente(int matricola, String username) {
        this.matricola = matricola;
        this.username = username;
        this.email = email;
        this.gender = gender;
    }

    public int getMatricola() {
        return matricola;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }
}
