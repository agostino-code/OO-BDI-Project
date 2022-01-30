package com.unina.project;

import java.util.List;

public class Gestore {
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public String getCodGestore() {
        return codGestore;
    }

    public List<Corso> getCorsi() {
        return corsi;
    }

    private String nome;
    private String descrizione;
    private String telefono;
    private String email;

    public void setCodGestore(String codGestore) {
        this.codGestore = codGestore;
    }

    public String codGestore;

    public void setCorsi(List<Corso> corsi) {
        this.corsi = corsi;
    }

    public List<Corso> corsi;

}
