package com.unina.project;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Lezione {
    private String titolo;
    private String descrizione;

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setDataoraInizio(LocalDateTime dataoraInizio) {
        this.dataoraInizio = dataoraInizio;
    }

    public void setDurata(LocalTime durata) {
        this.durata = durata;
    }

    public void setCodLezione(String codLezione) {
        this.codLezione = codLezione;
    }

    public LocalDate getDataInizio() {
        if(dataoraInizio!=null){
            return dataoraInizio.toLocalDate();
        }
        else{
            return null;
        }
    }

    public LocalTime getOraInizio() {
        if(dataoraInizio!=null){
            return dataoraInizio.toLocalTime();
        }
        else{
            return null;
        }
    }


    public String getTitolo() {
        return titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public LocalDateTime getDataoraInizio() {
        return dataoraInizio;
    }

    public LocalTime getDurata() {
        return durata;
    }

    public String getCodLezione() {
        return codLezione;
    }

    public String getCodCorso() {
        return codCorso;
    }

    private LocalDateTime dataoraInizio;
    private LocalTime durata;
    private String codLezione;

    public void setCodCorso(String codCorso) {
        this.codCorso = codCorso;
    }

    private String codCorso;

}
