package logika;

import logika.Zeton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import logika.Polje;
import splosno.Poteza;

public class Igra {
	
	//tu se mora ustvariti nova igra
	public static int velikostPlosce = 9;
	
	public static int steviloPotez = 0;
	
	//mozne poteze
	
	//vse vrste
	private static final List<Vrsta> VRSTE = new LinkedList<Vrsta>();

	
	//igralno polje
	public static Polje polje = new Polje(velikostPlosce);
	
	
	//igralec, ki je trenutno na potezi
	public static Igralec naPotezi;
	
	
	public static Stanje stanje = Stanje.V_TEKU;
	
	public Igra() {

		int N = velikostPlosce;
		polje = new Polje(N);
		//stanje = stanje.V_TEKU;
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
	
	
	public static List<Koordinate> moznePoteze() {
		final int N = velikostPlosce;
		LinkedList<Koordinate> prostaPolja = new LinkedList<Koordinate>();
		for (int i = 1; i < N+1; i++) {
			for (int j = 1; j < N+1; j++) {
				if (polje.grid[i][j] == null) {
					prostaPolja.add(new Koordinate(i, j));
				}
			}
		}
		return prostaPolja;
	}
	
	public static boolean odigraj(Poteza poteza) {
		Zeton zeton;
		int x = poteza.x();
		int y = poteza.y();
		if(steviloPotez % 2 == 0)  zeton = Zeton.CRNI;
		else zeton = Zeton.BELI;
	    // Preverimo, če je poteza veljavna
	    if (!jeVeljavnaPoteza(x,y)) {
	        return false;
	    }

	    // Izvedemo potezo
	    polje.dodajZeton(x, y, zeton);
	    steviloPotez++;

	    // Preverimo, če je kateri od igralcev ujel nasprotnikov kamen
	    if (polje.isCaptured(x, y)) {
	        if (zeton == Zeton.CRNI) {
	            stanje = Stanje.ZMAGA_CRNI;
	        } else {
	            stanje = Stanje.ZMAGA_BELI;
	        }
	    }
		List<Koordinate> poteze = moznePoteze();
		for (Koordinate p : poteze) {
		    System.out.println(p.x + " " + p.y);
		}
	    return true;
	}
	
	
	
	public static boolean jeVeljavnaPoteza(int x, int y) {
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
	
	public static boolean isKoRuleViolation(int x, int y) {
	    
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
	
	public void odigrajNakljucnoPotezo() {
	    ArrayList<Koordinate> prostaPolja = (ArrayList<Koordinate>) moznePoteze();
		
	    
	    // Check if there are any possible moves
	    if (prostaPolja.isEmpty()) {
	        return; // No possible moves, exit the method
	    }
	    
	    // Generate a random index within the range of available moves
	    int randomIndex = (int) (Math.random() * prostaPolja.size());
	    
	    // Get the randomly selected move
	    Koordinate nakljucnaPoteza = prostaPolja.get(randomIndex);
	    
	    int a = nakljucnaPoteza.x;
	    int b = nakljucnaPoteza.y;
	    Poteza updated = new Poteza(a,b);
	    // Play the randomly selected move
	    odigraj(updated);
	}




	//boolean odigraj(Poteza poteza). Metoda naj vrne true, če je poteza možna,
	//sicer pa false. Uporabite razred Poteza za 
	//koordinate od Poteza(0,0) (levo zgoraj) do Poteza(8,8) (desno spodaj).
	
	
}
   