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

		// Izvedemo potezo
		polje.dodajZeton(x, y, zeton);
		naPotezi = naPotezi.nasprotnik();
		odstraniUjeteZetone();

		// Preverimo, če je kateri od igralcev ujel nasprotnikov kamen, uporabljali za capturego
		//if (polje.isCaptured(x, y)) {
		//	if (zeton == Zeton.CRNI) {
		//		stanje = Stanje.ZMAGA_CRNI;
		//	} else {
		//		stanje = Stanje.ZMAGA_BELI;
		//	}
		//}
		System.out.println(identifyGroup(x,y, polje));
		return true;
	}
	
	public void odstraniUjeteZetone() {
		Set<Koordinate> koordinateSet = new HashSet<>();
	    int N = velikostPlosce;
	    for (int i = 1; i < N + 1; i++) {
	        for (int j = 1; j < N + 1; j++) {
	            if (isCaptured(i, j)) {
	                 koordinateSet.add(new Koordinate(i,j));
	            }
	        }
	    }
	    for (Koordinate koordinate : koordinateSet) {
	    	polje.odstraniZeton(koordinate.getX(), koordinate.getY());
	    }
	}
	    

	public boolean jeVeljavnaPoteza(int x, int y) {
		// Pogledamo, če so koordinate veljavne in je prazno polje
		Zeton igralec = polje.grid[x][y];
		if (x < 0 || x >= velikostPlosce + 1 || y < 0 || y >= velikostPlosce + 1 || polje.grid[x][y] != null) {
			return false;
		}

		 if (isKoRuleViolation(x, y) || isSuicideViolation(x,y)) {
		 return false;
		 }

		polje.dodajZeton(x, y, igralec);

		// Če je poteza veljavna, vrni true
		return true;
	}
	
	public boolean isCaptured(int x, int y) {
		Zeton igralec = polje.grid[x][y];
		Set<Point> obiskane = new HashSet<>(); // obiskana presecisca
		Queue<Point> vrsta = new LinkedList<>(); // vrsta za bfs

		// dodamo k vrsti
		vrsta.add(new Point(x, y));

		while (!vrsta.isEmpty()) {
			Point p = vrsta.poll();
			// pogledamo ce na preseciscu nasprotnik ali prazno polje
			if (polje.grid[p.x][p.y] != igralec) {
				if (polje.grid[p.x][p.y] == null) {
					return false;
				}
				continue;
			}

			// pogledamo ce je to presecisce ze blo pregledano
			if (obiskane.contains(p)) {
				continue;
			}

			// dodamo k obiskanim
			obiskane.add(p);

			// dodamo sosednja presecisca v vrsto
			if (p.x > 1)
				vrsta.add(new Point(p.x - 1, p.y));
			if (p.x < velikostPlosce)
				vrsta.add(new Point(p.x + 1, p.y));
			if (p.y > 1)
				vrsta.add(new Point(p.x, p.y - 1));
			if (p.y < velikostPlosce)
				vrsta.add(new Point(p.x, p.y + 1));
		}

		// pogledamo ce so vse okoli nasprotnikovi zetoni
		for (Point p : obiskane) {// System.out.print(p.x);
			if ((p.x > 1 && polje.grid[p.x - 1][p.y] == null))
				return false;
			if (p.x < velikostPlosce && polje.grid[p.x + 1][p.y] == null)
				return false;
			if (p.y > 1 && polje.grid[p.x][p.y - 1] == null)
				return false;
			if (p.y < velikostPlosce && polje.grid[p.x][p.y + 1] == null)
				return false;
		}

		// ce pridemo do konca je true
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
	    System.out.println(kopija);
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



	public boolean isSuicideViolation(int x, int y) {
		if (hasLiberty(x,y)) { 
			return false;
		}
		System.out.println("Suicide");
		return true;
	}
	
	public boolean isKoRuleViolation(int x, int y) {
		Zeton zeton;
		Polje trenutnoPolje = polje.kopija();
		if (naPotezi == Igralec.ČRNI)
			zeton = Zeton.CRNI;
		else
			zeton = Zeton.BELI;
		trenutnoPolje.dodajZeton(x, y, zeton);
		for (Polje prejsnjePolje: prejsnjaStanja) {
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
		if (x!= -1 && isCaptured(x, y)) {
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
