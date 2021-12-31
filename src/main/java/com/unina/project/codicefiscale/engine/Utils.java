package com.unina.project.codicefiscale.engine;

import com.unina.project.codicefiscale.document.CitiesCodes;
import com.unina.project.codicefiscale.document.citiesprovinces.CitiesProvinces;

public class Utils {

	private static final CitiesCodes cc = new CitiesCodes();
	private static final CitiesProvinces cp = new CitiesProvinces();
	public static final String ERROR = "#EE#";

	public static CitiesCodes getCitiesCodes(){
		return cc;
	}
	public static CitiesProvinces getCitiesProvinces(){
		return cp;
	}

}
