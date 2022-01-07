package com.unina.project;

public class Corso{
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setIscrizioniMassime(Integer iscrizioniMassime) {
        this.iscrizioniMassime = iscrizioniMassime;
    }

    public void setTassoPresenzeMinime(Integer tassoPresenzeMinime) {
        this.tassoPresenzeMinime = tassoPresenzeMinime;
    }

    public void setNumeroLezioni(Integer numeroLezioni) {
        this.numeroLezioni = numeroLezioni;
    }

    public String titolo;
    public String descrizione;
    public Integer iscrizioniMassime;
    public Integer numeroLezioni;

    public String getTassoPresenzeMinime() {
        return String.valueOf(tassoPresenzeMinime)+"%";
    }

    public Integer tassoPresenzeMinime;
    public String codCorso;
}
