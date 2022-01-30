package com.unina.project;

import java.util.List;

public class Studente extends Utente{
    public void setCodStudente(String codStudente) {
        this.codStudente = codStudente;
    }

    public void setIdoneo(Boolean idoneo) {
        Idoneo = idoneo;
    }

    private String codStudente;

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

    public String getCodStudente() {
        return codStudente;
    }

    public List<Corso> getCorsi() {
        return corsi;
    }

    private Boolean Idoneo;

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

    private Boolean Presente;

    public void setCorsi(List<Corso> corsi) {
        this.corsi = corsi;
    }

    private List<Corso> corsi;
}
