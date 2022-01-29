package com.unina.project;

public class Statistiche {

    public Integer getPercentualeRiempimento() {
        return percentualeRiempimento;
    }

    public void setPercentualeRiempimento(Integer percentualeRiempimento) {
        this.percentualeRiempimento = percentualeRiempimento;
    }

    public Integer getPresenzeMinime() {
        return presenzeMinime;
    }

    public void setPresenzeMinime(Integer presenzeMinime) {
        this.presenzeMinime = presenzeMinime;
    }

    public Integer getPresenzeMassime() {
        return presenzeMassime;
    }

    public void setPresenzeMassime(Integer presenzeMassime) {
        this.presenzeMassime = presenzeMassime;
    }

    public Float getPresenzeMedie() {
        return presenzeMedie;
    }

    public void setPresenzeMedie(Float presenzeMedie) {
        this.presenzeMedie = presenzeMedie;
    }

    public Integer percentualeRiempimento;
    public Integer presenzeMinime;
    public Integer presenzeMassime;
    public Float presenzeMedie;
}
