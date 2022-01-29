package com.unina.project;

import java.util.List;

public class Operatore extends Utente{
    public void setCodOperatore(String codOperatore) {
        this.codOperatore = codOperatore;
    }

    public String codOperatore;

    public void setCorsi(List<Corso> corsi) {
        this.corsi = corsi;
    }

    public List<Corso> corsi;

    public void setRichiesta(Boolean richiesta) {
        Richiesta = richiesta;
    }

    public String getRichiesta() {
        if (Richiesta == null) {
            return null;
        }
        if(Richiesta){
            return "Accettata";
        }
        else{
            return "In attesa";
        }
    }

    public Boolean Richiesta;
}
