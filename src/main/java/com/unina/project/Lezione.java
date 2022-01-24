package com.unina.project;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Lezione {
    public String titolo;
    public String descrizione;

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


    public LocalDateTime dataoraInizio;
    public LocalTime durata;
    public String codLezione;

    public void setCodCorso(String codCorso) {
        this.codCorso = codCorso;
    }

    public String codCorso;

}
