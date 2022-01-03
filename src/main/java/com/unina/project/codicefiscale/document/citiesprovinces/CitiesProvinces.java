package com.unina.project.codicefiscale.document.citiesprovinces;

import com.unina.project.codicefiscale.engine.Utils;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Agostino on 30/12/2021.
 */
@SuppressWarnings("serial")
public class CitiesProvinces extends TreeMap<String, String> {


    public CitiesProvinces(){
        this.put("AG", "AGRIGENTO");
        this.put("AL", "ALESSANDRIA");
        this.put("AN", "ANCONA");
        this.put("AO", "AOSTA");
        this.put("AR", "AREZZO");
        this.put("AP", "ASCOLI PICENO");
        this.put("AT", "ASTI");
        this.put("AV", "AVELLINO");
        this.put("BA", "BARI");
        this.put("BT", "BARLETTA-ANDRIA-TRANI");
        this.put("BL", "BELLUNO");
        this.put("BN", "BENEVENTO");
        this.put("BG", "BERGAMO");
        this.put("BI", "BIELLA");
        this.put("BO", "BOLOGNA");
        this.put("BZ", "BOLZANO");
        this.put("BS", "BRESCIA");
        this.put("BR", "BRINDISI");
        this.put("CA", "CAGLIARI");
        this.put("CL", "CALTANISSETTA");
        this.put("CB", "CAMPOBASSO");
        this.put("CI", "CARBONIA-IGLESIAS");
        this.put("CE", "CASERTA");
        this.put("CT", "CATANIA");
        this.put("CZ", "CATANZARO");
        this.put("CH", "CHIETI");
        this.put("CO", "COMO");
        this.put("CS", "COSENZA");
        this.put("CR", "CREMONA");
        this.put("KR", "CROTONE");
        this.put("CN", "CUNEO");
        this.put("EN", "ENNA");
        this.put("FM", "FERMO");
        this.put("FE", "FERRARA");
        this.put("FI", "FIRENZE");
        this.put("FG", "FOGGIA");
        this.put("FC", "FORLI’-CESENA");
        this.put("FR", "FROSINONE");
        this.put("GE", "GENOVA");
        this.put("GO", "GORIZIA");
        this.put("GR", "GROSSETO");
        this.put("IM", "IMPERIA");
        this.put("IS", "ISERNIA");
        this.put("SP", "LA SPEZIA");
        this.put("AQ", "L’AQUILA");
        this.put("LT", "LATINA");
        this.put("LE", "LECCE");
        this.put("LC", "LECCO");
        this.put("LI", "LIVORNO");
        this.put("LO", "LODI");
        this.put("LU", "LUCCA");
        this.put("MC", "MACERATA");
        this.put("MN", "MANTOVA");
        this.put("MS", "MASSA-CARRARA");
        this.put("MT", "MATERA");
        this.put("VS", "MEDIO CAMPIDANO");
        this.put("ME", "MESSINA");
        this.put("MI", "MILANO");
        this.put("MO", "MODENA");
        this.put("MB", "MONZA E BRIANZA");
        this.put("NA", "NAPOLI");
        this.put("NO", "NOVARA");
        this.put("NU", "NUORO");
        this.put("OG", "OGLIASTRA");
        this.put("OT", "OLBIA-TEMPIO");
        this.put("OR", "ORISTANO");
        this.put("PD", "PADOVA");
        this.put("PA", "PALERMO");
        this.put("PR", "PARMA");
        this.put("PV", "PAVIA");
        this.put("PG", "PERUGIA");
        this.put("PU", "PESARO E URBINO");
        this.put("PE", "PESCARA");
        this.put("PC", "PIACENZA");
        this.put("PI", "PISA");
        this.put("PT", "PISTOIA");
        this.put("PN", "PORDENONE");
        this.put("PZ", "POTENZA");
        this.put("PO", "PRATO");
        this.put("RG", "RAGUSA");
        this.put("RA", "RAVENNA");
        this.put("RC", "REGGIO CALABRIA");
        this.put("RE", "REGGIO EMILIA");
        this.put("RI", "RIETI");
        this.put("RN", "RIMINI");
        this.put("RM", "ROMA");
        this.put("RO", "ROVIGO");
        this.put("SA", "SALERNO");
        this.put("SS", "SASSARI");
        this.put("SV", "SAVONA");
        this.put("SI", "SIENA");
        this.put("SR", "SIRACUSA");
        this.put("SO", "SONDRIO");
        this.put("TA", "TARANTO");
        this.put("TE", "TERAMO");
        this.put("TR", "TERNI");
        this.put("TO", "TORINO");
        this.put("TP", "TRAPANI");
        this.put("TN", "TRENTO");
        this.put("TV", "TREVISO");
        this.put("TS", "TRIESTE");
        this.put("UD", "UDINE");
        this.put("VA", "VARESE");
        this.put("VE", "VENEZIA");
        this.put("VB", "VERBANO-CUSIO-OSSOLA");
        this.put("VC", "VERCELLI");
        this.put("VR", "VERONA");
        this.put("VV", "VIBO VALENTIA");
        this.put("VI", "VICENZA");
        this.put("VT", "VITERBO");

    }

    public String getKey(String s){
        String res = Utils.ERROR;
        for (Map.Entry<String, String> e : this.entrySet()) {
            if(s.equals(e.getValue()))
                res = e.getKey();
        }
        return res;
    }

}
