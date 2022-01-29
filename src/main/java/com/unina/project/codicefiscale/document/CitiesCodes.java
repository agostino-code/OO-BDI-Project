package com.unina.project.codicefiscale.document;

import java.util.Map;
import java.util.TreeMap;

import com.unina.project.codicefiscale.engine.Utils;

/**
 * Created by Matteo on 12/11/2015.
 */
@SuppressWarnings("serial")
public class CitiesCodes extends TreeMap<String, String> {

    final CitiesCodes_A ccA = new CitiesCodes_A();
    final CitiesCodes_B ccB = new CitiesCodes_B();
    final CitiesCodes_C ccC = new CitiesCodes_C();
    final CitiesCodes_D ccD = new CitiesCodes_D();
    final CitiesCodes_E ccE = new CitiesCodes_E();
    final CitiesCodes_F ccF = new CitiesCodes_F();
    final CitiesCodes_G ccG = new CitiesCodes_G();
    final CitiesCodes_H ccH = new CitiesCodes_H();
    final CitiesCodes_I ccI = new CitiesCodes_I();
    final CitiesCodes_L ccL = new CitiesCodes_L();
    final CitiesCodes_M ccM = new CitiesCodes_M();

    public CitiesCodes(){
        this.putAll(ccA);
        this.putAll(ccB);
        this.putAll(ccC);
        this.putAll(ccD);
        this.putAll(ccE);
        this.putAll(ccF);
        this.putAll(ccG);
        this.putAll(ccH);
        this.putAll(ccI);
        this.putAll(ccL);
        this.putAll(ccM);
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
