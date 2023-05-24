package logika;

import logika.Zeton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import logika.Polje;
import splosno.Poteza;

public class Igra {
	
	//tu se mora ustvariti nova igra
	public static int velikostPlosce = 9;
	
	//mozne poteze

	
	//igralno polje
	public Polje polje;
	
	
	//igralec, ki je trenutno na potezi
	public Igralec naPotezi;
	
	
	public Stanje stanje;
	
	public Igra() {

		int N = velikostPlosce;
		polje = new Polje(N);
		stanje = Stanje.V_TEKU;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				polje.grid[i][j] = null;
			}
		}
		naPotezi = Igralec.ČRNI;
	}
	
	
	public Igralec naPotezi () {
		return naPotezi;
	}
	
	public Igralec nasprotnik() {
		if(naPotezi == Igralec.BELI) return Igralec.ČRNI;
		else return Igralec.BELI;
				
	}
	
	public boolean aktivna(Stanje stanje) {
		if (stanje == Stanje.V_TEKU) return true;
		else {
			return false;
		}
	}
	
	public Stanje dobiStanje() {
		return stanje;
		
	}
	
	public Polje getPlosca () {
		return polje;
	}
	
	
	public List<Poteza> moznePoteze() {
		final int N = velikostPlosce;
		LinkedList<Poteza> prostaPolja = new LinkedList<Poteza>();
		for (int i = 1; i < N+1; i++) {
			for (int j = 1; j < N+1; j++) {
				if (polje.grid[i][j] == null) {
					prostaPolja.add(new Poteza(i, j));
				}
			}
		}
		return prostaPolja;
	}
	
	public boolean odigraj(Poteza poteza) {
		Zeton zeton;
		int x = poteza.x();
		int y = poteza.y();
		if(naPotezi == Igralec.ČRNI)  zeton = Zeton.CRNI;
		else zeton = Zeton.BELI;
	    // Preverimo, če je poteza veljavna
	    if (!jeVeljavnaPoteza(x,y)) {
	        return false;
	    }

	    // Izvedemo potezo
	    polje.dodajZeton(x, y, zeton);
	    naPotezi = naPotezi.nasprotnik();

	    // Preverimo, če je kateri od igralcev ujel nasprotnikov kamen
	    if (polje.isCaptured(x, y)) {
	        if (zeton == Zeton.CRNI) {
	            stanje = Stanje.ZMAGA_CRNI;
	        } else {
	            stanje = Stanje.ZMAGA_BELI;
	        }
	    }
	    //System.out.println(moznePoteze());
	    return true;
	}
	
	
	
	public boolean jeVeljavnaPoteza(int x, int y) {
	    // Check if coordinates are valid and the position is empty
		Zeton igralec = polje.grid[x][y];
	    if (x < 0 || x >= velikostPlosce+1 || y < 0 || y >= velikostPlosce+1 || polje.grid[x][y] != null) {
	        return false;
	    }

	    // Check if the move violates the ko rule
	    if (isKoRuleViolation(x, y)) {
	        return false;
	    }

	    // Try to place the stone and see if it gets captured
	    polje.grid[x][y] = igralec;
	    boolean captured = polje.isCaptured(x, y);

	    // If the move gets the stone captured, return false
	    if (captured) {
	        polje.grid[x][y] = igralec;
	        return true;
	    }

	    // If the move is legal, return true
	    return true;
	}
	
	public boolean isKoRuleViolation(int x, int y) {
	    
	    // Rule 2: A player cannot play on a point that would cause their own stone to be captured
	    if (polje.isCaptured(x, y)) {
	        return true;
	    }
	    
	    // Rule 3: A player cannot play on a point that would cause a repetition of the current game state
	    // We can use a HashSet to keep track of visited game states
	    Set<String> visitedStates = new HashSet<>();
	    if (visitedStates.contains(stanje.toString())) {
	        return true;
	    }
	    visitedStates.add(stanje.toString());
	    
	    return false;
	}
	
	private static Random random = new Random ();
	
	public boolean odigrajNakljucnoPotezo() {
	    ArrayList<Poteza> prostaPolja = new ArrayList<>(moznePoteze());

	    Collections.shuffle(prostaPolja);

	    Poteza poteza = prostaPolja.get(0);
		Zeton zeton;
		int x = poteza.x();
		int y = poteza.y();
		if(naPotezi == Igralec.ČRNI)  zeton = Zeton.CRNI;
		else zeton = Zeton.BELI;
		if(prostaPolja.size() == 1) {
			odigraj(poteza);
			return false;
		}
	    if (prostaPolja.isEmpty()) {
	    	return false; // No possible moves, exit the method
	    } 
	    if (polje.isCaptured(x, y)) {
	        if (zeton == Zeton.CRNI) {
	            stanje = Stanje.ZMAGA_CRNI;
	        } else {
	            stanje = Stanje.ZMAGA_BELI;
	        }
	        return false;
	    }
	   odigraj(poteza);
	    return true;
	    
	}


	public Igra kopija() {
	    Igra kopija = new Igra();
	    kopija.velikostPlosce = this.velikostPlosce;
	    kopija.naPotezi = this.naPotezi;
	    kopija.stanje = this.stanje;
	    kopija.polje = this.polje.kopija();
	    return kopija;
	}


	public Polje getPolje() {
		// TODO Auto-generated method stub
		return polje;
	}

	


	//boolean odigraj(Poteza poteza). Metoda naj vrne true, če je poteza možna,
	//sicer pa false. Uporabite razred Poteza za 
	//koordinate od Poteza(0,0) (levo zgoraj) do Poteza(8,8) (desno spodaj).
	
	
}
   