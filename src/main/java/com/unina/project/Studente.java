package com.unina.project;

import java.util.List;

public class Studente extends Utente{
    public void setCodStudente(String codStudente) {
        this.codStudente = codStudente;
    }

    public void setIdoneo(Boolean idoneo) {
        Idoneo = idoneo;
    }

    public String codStudente;

    public String getIdoneo() {
        if (Idoneo == null) {
            return null;
        }
        if(Idoneo){
            return "Si";
        }
        else{
            return "No";
        }
    }

    public Boolean Idoneo;
    public String getPresente() {
        if(Presente){
            return "Si";
        }
        else{
            return "No";
        }
    }
    public void setPresente(Boolean presente) {
        Presente = presente;
    }

    public Boolean Presente;

    public void setCorsi(List<Corso> corsi) {
        this.corsi = corsi;
    }

    public List<Corso> corsi;
}
