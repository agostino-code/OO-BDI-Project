package com.unina.project.codicefiscale.engine;

import com.unina.project.Utente;

import java.io.IOException;


public class Engine {

	private String code;
	
	private final String cognome;
	private final String nome;
	private final String sesso;
	private final int giorno;
	private final int mese;
	private final int anno;
	private final String citta;
	
	private String consonanti_COGNOME="";
	private String vocali_COGNOME="";
	private String consonanti_NOME="";
	private String vocali_NOME="";
	
	public Engine(Utente persona) {

		cognome= persona.getCognome().toUpperCase();
		nome= persona.getNome().toUpperCase();
		sesso= persona.getSesso();
		giorno= persona.getDataNascitaGiorno();
		mese= persona.getDataNascitaMese();
		anno= persona.getDataNascitaAnno();
		citta= persona.getComuneDiNascita().toUpperCase();
	
		popolazioneStringheConsonantiVocali();
		
		code=codiceCognome()+codiceNome()+codiceData()+codiceCitta();
		code+=controlCode(code);
	}

	public String getCode(){
		return codiceCitta().equals(Utils.ERROR) ? "*#ERRORE DI CALCOLO#*" : code;
	}
	
	private String controlCode(String s) {
		String c="";
		StringBuilder char_posPari= new StringBuilder();
		StringBuilder char_posDispari= new StringBuilder();
		int counter=0;
		for(int i=0;i<s.length();i++){
			if(i%2==0)
				char_posDispari.append(s.charAt(i)); //perchè per l'algoritmo la stringa comincia da 1 e non da 0
			else
				char_posPari.append(s.charAt(i));
		}
		for(int i=0;i<char_posDispari.length();i++){
			switch (char_posDispari.charAt(i)) {
				case '0' -> counter += 1;
				case '1' -> counter += 0;
				case '2' -> counter += 5;
				case '3' -> counter += 7;
				case '4' -> counter += 9;
				case '5' -> counter += 13;
				case '6' -> counter += 15;
				case '7' -> counter += 17;
				case '8' -> counter += 19;
				case '9' -> counter += 21;
				case 'A' -> counter += 1;
				case 'B' -> counter += 0;
				case 'C' -> counter += 5;
				case 'D' -> counter += 7;
				case 'E' -> counter += 9;
				case 'F' -> counter += 13;
				case 'G' -> counter += 15;
				case 'H' -> counter += 17;
				case 'I' -> counter += 19;
				case 'J' -> counter += 21;
				case 'K' -> counter += 2;
				case 'L' -> counter += 4;
				case 'M' -> counter += 18;
				case 'N' -> counter += 20;
				case 'O' -> counter += 11;
				case 'P' -> counter += 3;
				case 'Q' -> counter += 6;
				case 'R' -> counter += 8;
				case 'S' -> counter += 12;
				case 'T' -> counter += 14;
				case 'U' -> counter += 16;
				case 'V' -> counter += 10;
				case 'W' -> counter += 22;
				case 'X' -> counter += 25;
				case 'Y' -> counter += 24;
				case 'Z' -> counter += 23;
			}
		}
		for(int i=0;i<char_posPari.length();i++){
			switch (char_posPari.charAt(i)) {
				case '0' -> counter += 0;
				case '1' -> counter += 1;
				case '2' -> counter += 2;
				case '3' -> counter += 3;
				case '4' -> counter += 4;
				case '5' -> counter += 5;
				case '6' -> counter += 6;
				case '7' -> counter += 7;
				case '8' -> counter += 8;
				case '9' -> counter += 9;
				case 'A' -> counter += 0;
				case 'B' -> counter += 1;
				case 'C' -> counter += 2;
				case 'D' -> counter += 3;
				case 'E' -> counter += 4;
				case 'F' -> counter += 5;
				case 'G' -> counter += 6;
				case 'H' -> counter += 7;
				case 'I' -> counter += 8;
				case 'J' -> counter += 9;
				case 'K' -> counter += 10;
				case 'L' -> counter += 11;
				case 'M' -> counter += 12;
				case 'N' -> counter += 13;
				case 'O' -> counter += 14;
				case 'P' -> counter += 15;
				case 'Q' -> counter += 16;
				case 'R' -> counter += 17;
				case 'S' -> counter += 18;
				case 'T' -> counter += 19;
				case 'U' -> counter += 20;
				case 'V' -> counter += 21;
				case 'W' -> counter += 22;
				case 'X' -> counter += 23;
				case 'Y' -> counter += 24;
				case 'Z' -> counter += 25;
			}
		}
		switch (counter % 26) {
			case 0 -> c = "A";
			case 1 -> c = "B";
			case 2 -> c = "C";
			case 3 -> c = "D";
			case 4 -> c = "E";
			case 5 -> c = "F";
			case 6 -> c = "G";
			case 7 -> c = "H";
			case 8 -> c = "I";
			case 9 -> c = "J";
			case 10 -> c = "K";
			case 11 -> c = "L";
			case 12 -> c = "M";
			case 13 -> c = "N";
			case 14 -> c = "O";
			case 15 -> c = "P";
			case 16 -> c = "Q";
			case 17 -> c = "R";
			case 18 -> c = "S";
			case 19 -> c = "T";
			case 20 -> c = "U";
			case 21 -> c = "V";
			case 22 -> c = "W";
			case 23 -> c = "X";
			case 24 -> c = "Y";
			case 25 -> c = "Z";
		}
		return c;
	}
	
	private String codiceCitta() {
        return Utils.getCitiesCodes().getKey(citta);
    }

	private String codiceData(){
		String s="";
		String annoS=anno+"";
		s=s+annoS.charAt(2)+annoS.charAt(3);
		switch (mese) {
			case 1 -> s += "A";
			case 2 -> s += "B";
			case 3 -> s += "C";
			case 4 -> s += "D";
			case 5 -> s += "E";
			case 6 -> s += "H";
			case 7 -> s += "L";
			case 8 -> s += "M";
			case 9 -> s += "P";
			case 10 -> s += "R";
			case 11 -> s += "S";
			case 12 -> s += "T";
		}
		if(sesso.equals("M")){
			if(giorno<10)
				s+="0"+giorno;
			else
				s+=giorno;
		}
		else{
			s+=(giorno+40);
		}
		return s;
	}
	
	private String codiceNome() {
		StringBuilder s= new StringBuilder();
		if(consonanti_NOME.length()>3){
			s.append(consonanti_NOME.charAt(0)).append(consonanti_NOME.charAt(2)).append(consonanti_NOME.charAt(3));
			return s.toString();
		}
		if(consonanti_NOME.length()==3){
			for(int i=0;i<3;i++)
				s.append(consonanti_NOME.charAt(i));
			return s.toString();
		}
		if(consonanti_NOME.length()==2){
			s.append(consonanti_NOME.charAt(0)).append(consonanti_NOME.charAt(1)).append(vocali_NOME.charAt(0));
			return s.toString();
		}
		if(consonanti_NOME.length()==1){
			s.append(consonanti_NOME.charAt(0)).append(vocali_NOME.charAt(0)).append(vocali_NOME.charAt(1));
			return s.toString();
		}
		else{
			for(int i=0;i<3;i++)
				s.append(vocali_NOME.charAt(i));
			return s.toString();
		}
	}
	
	private String codiceCognome() {
		StringBuilder s= new StringBuilder();
		if(consonanti_COGNOME.length()>=3){
			for(int i=0;i<3;i++)
				s.append(consonanti_COGNOME.charAt(i));
			return s.toString();
		}
		if(consonanti_COGNOME.length()==2){
			s.append(consonanti_COGNOME.charAt(0)).append(consonanti_COGNOME.charAt(1)).append(vocali_COGNOME.charAt(0));
			return s.toString();
		}
		if(consonanti_COGNOME.length()==1){
			s.append(consonanti_COGNOME.charAt(0)).append(vocali_COGNOME.charAt(0)).append(vocali_COGNOME.charAt(1));
			return s.toString();
		}
		else{
			for(int i=0;i<3;i++)
				s.append(vocali_COGNOME.charAt(i));
			return s.toString();
		}
	}

	private void popolazioneStringheConsonantiVocali() {
//		COGNOME
		for(int i=0;i<cognome.length();i++)
			if(isVocal(cognome.charAt(i)))
				vocali_COGNOME=vocali_COGNOME+cognome.charAt(i);
			else
				if(cognome.charAt(i)!=' ')
					consonanti_COGNOME=consonanti_COGNOME+cognome.charAt(i);
//		NOME
		for(int i=0;i<nome.length();i++)
			if(isVocal(nome.charAt(i)))
				vocali_NOME=vocali_NOME+nome.charAt(i);
			else
				if(nome.charAt(i)!=' ')
					consonanti_NOME=consonanti_NOME+nome.charAt(i);
	}

	private boolean isVocal(char c){
		return c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U';
	}
	
}
