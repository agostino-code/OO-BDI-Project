package com.unina.project;

import com.unina.project.codicefiscale.engine.Utils;

public class Sede {
    public void setVia(String via) {
        this.via = via.toUpperCase();
    }

    public void setCivico(String civico) {
        this.civico = civico.toUpperCase();
    }

    public void setCitta(String citta) {
        this.citta = citta.toUpperCase();
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia.toUpperCase();
    }

    public String via;
    public String civico;
    public String citta;
    public String provincia;

    public String getIndirizzo(){

        return "VIA "+via+", "+civico+", "+citta+", "+provincia+", "+ Utils.getCitiesCodes().getKey(provincia);
    }

}
