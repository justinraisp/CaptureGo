package logika;

import logika.Zeton;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import logika.Polje;
import splosno.Poteza;

public class Igra {

	// tu se mora ustvariti nova igra
	public static int velikostPlosce = 19;

	// mozne poteze

	// igralno polje
	public Polje polje;

	// igralec, ki je trenutno na potezi
	public Igralec naPotezi;

	public Stanje stanje;
	
	public List<Polje> prejsnjaStanja;

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
		prejsnjaStanja = new ArrayList<>();
	}

	public Igralec naPotezi() {
		return naPotezi;
	}
	
	public Zeton zetonNaPotezi() {
		if(naPotezi == Igralec.BELI) return Zeton.BELI;
		else return Zeton.CRNI;
	}

	public Igralec nasprotnik() {
		if (naPotezi == Igralec.BELI)
			return Igralec.ČRNI;
		else
			return Igralec.BELI;

	}

	public boolean aktivna(Stanje stanje) {
		if (stanje == Stanje.V_TEKU)
			return true;
		else {
			return false;
		}
	}

	public Stanje dobiStanje() {
		return stanje;

	}

	public Polje dobiPlosca() {
		return polje;
	}

	public List<Poteza> moznePoteze() {
		final int N = velikostPlosce;
		LinkedList<Poteza> prostaPolja = new LinkedList<Poteza>();
		for (int i = 1; i < N + 1; i++) {
			for (int j = 1; j < N + 1; j++) {
				if (polje.grid[i][j] == null) {
					prostaPolja.add(new Poteza(i, j));
				}
			}
		}
		prostaPolja.add(new Poteza(-1,-1)); //dodamo potezo za pass
		return prostaPolja;
	}
	
	public void pass() {
		naPotezi = naPotezi.nasprotnik();
	}
	
	public void concede() {
	    if (naPotezi == Igralec.ČRNI) {
	        stanje = Stanje.ZMAGA_BELI;
	    } else {
	        stanje = Stanje.ZMAGA_CRNI;
	    }
	}

	public boolean odigraj(Poteza poteza) {
		Zeton zeton;
		int x = poteza.x();
		int y = poteza.y();
		boolean semi = false;
		
	    if (x == -1 && y == -1) {
	        pass();
	        return true;
	    }
		if (naPotezi == Igralec.ČRNI)
			zeton = Zeton.CRNI;
		else
			zeton = Zeton.BELI;
		// Preverimo, če je poteza veljavna
		if (!jeVeljavnaPoteza(x, y)) {
			return false;
		}
		if(semiSuicide(x,y)) semi = true;
		// Izvedemo potezo
		polje.dodajZeton(x, y, zeton);
		naPotezi = naPotezi.nasprotnik();
		polje.odstraniUjeteZetone();
		if(semi) polje.dodajZeton(x, y, zeton); //ce je semiSuicide je treba se enkrat dodat kamen, drugace ga tretira kot ujetega
		//prejsnjaStanja.add(polje);
		// Preverimo, če je kateri od igralcev ujel nasprotnikov kamen, uporabljali za capturego
		//if (polje.polje.isCaptured(x, y)) {
		//	if (zeton == Zeton.CRNI) {
		//		stanje = Stanje.ZMAGA_CRNI;
		//	} else {
		//		stanje = Stanje.ZMAGA_BELI;
		//	}
		//}
		prejsnjaStanja.add(polje.kopija());
		return true;
	}
	
	    

	public boolean jeVeljavnaPoteza(int x, int y) {
		// Pogledamo, če so koordinate veljavne in je prazno polje
		Zeton igralec = polje.grid[x][y];
		if (x < 0 || x >= velikostPlosce + 1 || y < 0 || y >= velikostPlosce + 1 || polje.grid[x][y] != null) {
			return false;
		}

		 if (isSuicideViolation(x,y) || isKoRuleViolation(x, y) ) {
			 return false;
		 }

		// Če je poteza veljavna, vrni true
		return true;
	}
	

	public Set<Point> identifyGroup(int x, int y, Polje polje) {
	    Zeton player = polje.grid[x][y];
	    Set<Point> group = new HashSet<>(); // Set to store the group of stones
	    Set<Point> visited = new HashSet<>(); // Set to keep track of visited intersections

	    // Call the DFS method to identify the group
	    dfs(x, y, player, group, visited, polje);

	    return group;
	}

	private void dfs(int x, int y, Zeton player, Set<Point> group, Set<Point> visited, Polje polje) {
	    // Check if the current intersection is outside the board or has a different player's stone
	    if (x < 0 || x >= velikostPlosce || y < 0 || y >= velikostPlosce || polje.grid[x][y] != player) {
	        return;
	    }

	    Point current = new Point(x, y);

	    // Check if the current intersection has already been visited
	    if (visited.contains(current)) {
	        return;
	    }

	    visited.add(current);
	    group.add(current);

	    // Recursively check the neighboring intersections
	    dfs(x - 1, y, player, group, visited,polje); // Check left
	    dfs(x + 1, y, player, group, visited,polje); // Check right
	    dfs(x, y - 1, player, group, visited,polje); // Check up
	    dfs(x, y + 1, player, group, visited,polje); // Check down
	}
	
	public boolean hasLiberty(int x, int y) {
		Polje kopija = polje.kopija();
		Zeton zeton = zetonNaPotezi();
		kopija.dodajZeton(x, y, zeton);
	    Set<Point> group = identifyGroup(x, y, kopija);
	    for (Point stone : group) {
	        // Check if any neighboring intersection is empty (has null value)
	        if (hasEmptyNeighbor(stone.x, stone.y)) {
	            return true; // The group has at least one liberty
	        }
	    }
	    return false; // No liberties found in the group
	}

	private boolean hasEmptyNeighbor(int x, int y) {
	    // Check if any neighboring intersection is empty (has null value)
	    if (x > 1 && polje.grid[x - 1][y] == null) {
	        return true; // Left neighbor is empty
	    }
	    if (x < velikostPlosce && polje.grid[x + 1][y] == null) {
	        return true; // Right neighbor is empty
	    }
	    if (y > 1 && polje.grid[x][y - 1] == null) {
	        return true; // Upper neighbor is empty
	    }
	    if (y < velikostPlosce && polje.grid[x][y + 1] == null) {
	        return true; // Lower neighbor is empty
	    }

	    return false; // No empty neighbors found
	}

	private boolean semiSuicide(int x, int y) {//ce bi igral zgleda kot suicide, vendar ujame nasprotnikovo grupo in ni suicide 
		boolean captureFound = false;
		Polje kopija = polje.kopija();
		kopija.dodajZeton(x, y, zetonNaPotezi());
	    if (kopija.isCaptured(x, y - 1)) {  // Check the stone above
	        captureFound = true;
	    }
	    if (kopija.isCaptured(x, y + 1)) {  // Check the stone below
	        captureFound = true;
	    }
	    if (kopija.isCaptured(x - 1, y)) {  // Check the stone to the left
	        captureFound = true;
	    }
	    if (kopija.isCaptured(x + 1, y)) {  // Check the stone to the right
	        captureFound = true;
	    }
	    return captureFound;
	}
	

	public boolean isSuicideViolation(int x, int y) {
		if (hasLiberty(x,y) || semiSuicide(x,y)) { 
			return false;
		}
		System.out.println("Suicide");
		return true;
	}
	
	public boolean isKoRuleViolation(int x, int y) {
		boolean semi = false;
		Polje trenutnoPolje = polje.kopija();
		if(semiSuicide(x,y)) semi = true;
		// Izvedemo potezo
		trenutnoPolje.dodajZeton(x, y, zetonNaPotezi());
		trenutnoPolje.odstraniUjeteZetone();
		if(semi) trenutnoPolje.dodajZeton(x, y, zetonNaPotezi());
		for (Polje prejsnjePolje: prejsnjaStanja) {
			System.out.println(prejsnjePolje.grid);
			if (prejsnjePolje.equals(trenutnoPolje)) {
				System.out.println("ko");
				return true;
			}
		}
		return false;
	}

	private static Random random = new Random();

	public boolean odigrajNakljucnoPotezo() {
		ArrayList<Poteza> prostaPolja = new ArrayList<>(moznePoteze());
		// Premesa mozne poteze in izberi prvega
		Collections.shuffle(prostaPolja);
		Poteza poteza = prostaPolja.get(0);
		Zeton zeton;
		int x = poteza.x();
		int y = poteza.y();
		if (naPotezi == Igralec.ČRNI)
			zeton = Zeton.CRNI;
		else
			zeton = Zeton.BELI;
		if (prostaPolja.size() == 1) {
			odigraj(poteza);
			return false;
		}
		if (prostaPolja.isEmpty()) {
			return false; // Ni moznih potez
		}
		if (x!= -1 && polje.isCaptured(x, y)) {
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

	public Polje dobiPolje() {
		return polje;
	}

}
