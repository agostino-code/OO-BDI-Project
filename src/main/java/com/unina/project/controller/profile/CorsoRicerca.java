package com.unina.project.controller.profile;

import com.unina.project.AreaTematica;
import java.util.List;

public class CorsoRicerca {
    public void setCodGestore(String codGestore) {
        this.codGestore = codGestore;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public void setCodCorso(String codCorso) {
        this.codCorso = codCorso;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public void setAreetematiche(List<AreaTematica> areetematiche) {
        this.areetematiche = areetematiche;
    }

    public String codGestore;
    public String descrizione;
    public String titolo;
    public String codCorso;
    public String nome;
    public String citta;
    public String provincia;

    public void setIscrizioniMassime(Integer iscrizioniMassime) {
        this.iscrizioniMassime = iscrizioniMassime;
    }

    public Integer iscrizioniMassime;

    public void setNumeroLezioni(Integer numeroLezioni) {
        this.numeroLezioni = numeroLezioni;
    }

    public void setTassoPresenzeMinime(Integer tassoPresenzeMinime) {
        this.tassoPresenzeMinime = tassoPresenzeMinime;
    }

    public Integer numeroLezioni;
    public Integer tassoPresenzeMinime;

    public String getPrivato() {
        if (Privato == null) {
            return null;
        }
        if(Privato){
            return "Privato";
        }
        else{
            return "Pubblico";
        }
    }

    public void setPrivato(Boolean privato) {
        Privato = privato;
    }

    public Boolean Privato;

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String tag="...";

    public List<AreaTematica> areetematiche;
}
