package com.unina.project;

import java.util.Iterator;
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

    public String nome;
    public String descrizione;
    public String telefono;
    public String email;

    public void setCorsi(List<Corso> corsi) {
        this.corsi = corsi;
    }

    public List<Corso> corsi;

    public void stampaCorsi(){
        Iterator<Corso> iterator = corsi.iterator();

        while(iterator.hasNext()){
            System.out.println(iterator.next().titolo);
        }
    }

}
