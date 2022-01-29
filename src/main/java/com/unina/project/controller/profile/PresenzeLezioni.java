package com.unina.project.controller.profile;

public class PresenzeLezioni {
    public String getCodLezione() {
        return codLezione;
    }

    public void setCodLezione(String codLezione) {
        this.codLezione = codLezione;
    }

    public Integer getNumero_presenze() {
        return numero_presenze;
    }

    public void setNumero_presenze(Integer numero_presenze) {
        this.numero_presenze = numero_presenze;
    }

    public String codLezione;
    public Integer numero_presenze;
}
