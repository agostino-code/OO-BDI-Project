package com.unina.project.codicefiscale.engine;

import com.unina.project.codicefiscale.document.CitiesCodes;

public class Utils {

	private static final CitiesCodes cc = new CitiesCodes();
	public static final String ERROR = "#EE#";

	public static CitiesCodes getCitiesCodes(){
		return cc;
	}

}
