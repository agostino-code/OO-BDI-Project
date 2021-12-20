package project;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

public class Lezione {
    public String titolo;
    public String descrizione;
    public Timestamp dataoraDiInizio;
    public Time durata;
    public List<Studente> studentiPartecipanti;
}
