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

    public String getVia() {
        return via;
    }

    public String getCivico() {
        return civico;
    }

    public String getCitta() {
        return citta;
    }

    public String getProvincia() {
        return provincia;
    }

    private String via;
    private String civico;
    private String citta;
    private String provincia;

    public String getIndirizzo(){

        return "VIA "+via+", "+civico+", "+citta+", "+provincia+", "+ Utils.getCitiesCodes().getKey(provincia);
    }

}
