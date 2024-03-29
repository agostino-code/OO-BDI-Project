package com.unina.project;

import java.time.LocalDate;

public class Utente {
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getSesso() {
        return sesso;
    }

    public void setSesso(String sesso) {
        this.sesso = sesso;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getComuneDiNascita() {
        return comuneDiNascita;
    }

    public void setComuneDiNascita(String comuneDiNascita) {
        this.comuneDiNascita = comuneDiNascita.toUpperCase();
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public String getEmail() {
        return email;
    }

    private String email;
    private String nome;
    private String cognome;

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public LocalDate dataNascita;
    public String sesso;
    public String comuneDiNascita;
    public String codiceFiscale;

    public int getDataNascitaGiorno() {
        return dataNascita.getDayOfMonth();
    }

    public int getDataNascitaMese() {
        return dataNascita.getMonthValue();
    }

    public int getDataNascitaAnno() {
        return dataNascita.getYear();
    }
}
