package logika;

import logika.Zeton;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import logika.Polje;
import splosno.Poteza;

public class Igra {

	// tu se mora ustvariti nova igra
	public static int velikostPlosce = 19;

	public int crniTocke;
	public int beliTocke;
	
	public int consecutivePassesBeli;
	
	public int consecutivePassesCrni;
	
	public int tipZmage = -1;
	
	public boolean captureGo;
	
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
		crniTocke = 0;
		beliTocke = 2; //kompenzacija, ker zacne kasneje
		consecutivePassesCrni = 0;
		consecutivePassesBeli = 0;
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
		if(naPotezi == Igralec.BELI) consecutivePassesBeli++;
		else consecutivePassesCrni++;
		naPotezi = naPotezi.nasprotnik();
		stanje = this.konecIgre();
	}
	
	public void resign() {
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
		if (naPotezi == Igralec.ČRNI) {
			zeton = Zeton.CRNI;
		}
		else {
			zeton = Zeton.BELI;
		}
		// Preverimo, če je poteza veljavna
		if (!jeVeljavnaPoteza(x, y)) {
			return false;
		}
		if(semiSuicide(x,y)) semi = true;
		// Izvedemo potezo
		polje.dodajZeton(x, y, zeton);
		naPotezi = naPotezi.nasprotnik();		
		if(polje.odstraniUjeteZetone(captureGo)) {
			tipZmage = 2;
			if(zeton == zeton.BELI) stanje = stanje.ZMAGA_BELI;
			else stanje = stanje.ZMAGA_CRNI;
		}
		if(semi) polje.dodajZeton(x, y, zeton); //ce je semiSuicide je treba se enkrat dodat kamen, drugace ga tretira kot ujetega
		
		if(zeton == Zeton.CRNI) consecutivePassesCrni = 0;
		if(zeton == Zeton.BELI) consecutivePassesBeli = 0;
		
		// Preverimo, če je kateri od igralcev ujel nasprotnikov kamen, uporabljali za capturego
		
		prejsnjaStanja.add(polje.kopija());
		konecIgre();
		return true;
	}
	
	    

	public boolean jeVeljavnaPoteza(int x, int y) {
		// Pogledamo, če so koordinate veljavne in je prazno polje
		Zeton igralec;
		if(x != -1) igralec = polje.grid[x][y];
		if (x < 0 || x >= velikostPlosce + 1 || y < 0 || y >= velikostPlosce + 1 || polje.grid[x][y] != null) {
			return false;
		}

		 if (isSuicideViolation(x,y) || isKoRuleViolation(x, y) ) {
			 return false;
		 }

		// Če je poteza veljavna, vrni true
		return true;
	}
	

	public Set<Point> identifyGroup(int x, int y, Zeton player, Set<Point> visited, Polje polje) {
	    Set<Point> group = new HashSet<>(); // Set to store the group of stones

	    // Call the DFS method to identify the group
	    dfs(x, y, player, group, visited, polje);

	    return group;
	}

	private void dfs(int x, int y, Zeton player, Set<Point> group, Set<Point> visited, Polje polje) {
	    if (x < 1 || x > velikostPlosce || y < 1 || y > velikostPlosce || visited.contains(new Point(x, y))) {
	        return;
	    }

	    Point current = new Point(x, y);

	    if (polje.grid[x][y] != player) {
	        visited.add(current);
	        return;
	    }

	    visited.add(current);
	    group.add(current);

	    dfs(x - 1, y, player, group, visited, polje);
	    dfs(x + 1, y, player, group, visited, polje);
	    dfs(x, y - 1, player, group, visited, polje);
	    dfs(x, y + 1, player, group, visited, polje);
	}
	
	public boolean hasLiberty(int x, int y) {
		Polje kopija = polje.kopija();
		Zeton zeton = zetonNaPotezi();
		kopija.dodajZeton(x, y, zeton);
		Set<Point> visited = new HashSet<>();
	    Set<Point> group = identifyGroup(x, y, zeton, visited, kopija);
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
	    if (y > 1 && kopija.isCaptured(x, y - 1)) {  // Check the stone above
	        captureFound = true;
	    }
	    if (y < velikostPlosce && kopija.isCaptured(x, y + 1)) {  // Check the stone below
	        captureFound = true;
	    }
	    if (x > 1 &&kopija.isCaptured(x - 1, y)) {  // Check the stone to the left
	        captureFound = true;
	    }
	    if (x < velikostPlosce &&kopija.isCaptured(x + 1, y) ) {  // Check the stone to the right
	        captureFound = true;
	    }
	    return captureFound;
	}
	

	public boolean isSuicideViolation(int x, int y) {
		if (hasLiberty(x,y) || semiSuicide(x,y)) { 
			return false;
		}
		return true;
	}
	
	public boolean isKoRuleViolation(int x, int y) {
		boolean semi = false;
		Polje trenutnoPolje = polje.kopija();
		if(semiSuicide(x,y)) semi = true;
		// Izvedemo potezo
		trenutnoPolje.dodajZeton(x, y, zetonNaPotezi());
		trenutnoPolje.odstraniUjeteZetone(captureGo);
		if(semi) trenutnoPolje.dodajZeton(x, y, zetonNaPotezi());
		for (Polje prejsnjePolje: prejsnjaStanja) {
			if (prejsnjePolje.equals(trenutnoPolje)) {
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
		kopija.consecutivePassesBeli = this.consecutivePassesBeli;
		kopija.consecutivePassesCrni = this.consecutivePassesCrni;
		return kopija;
	}

	public Polje dobiPolje() {
		return polje;
	}
	
	public Set<Set<Point>> identifyTerritory(Polje polje) {
	    Set<Set<Point>> territories = new HashSet<>();
	    Set<Point> visited = new HashSet<>();

	    for (int x = 1; x <= velikostPlosce; x++) {
	        for (int y = 1; y <= velikostPlosce; y++) {
	            Point position = new Point(x, y);
	            if (!visited.contains(position) && polje.grid[x][y] == null) {
	                Set<Point> territory = identifyGroup(x, y, null, visited, polje);
	                territories.add(territory);
	            }
	        }
	    }
	    return territories;
	}
	public Map<Zeton, Set<Set<Point>>> getTerritoryOwners(Polje polje) {
	    Map<Zeton, Set<Set<Point>>> territoryOwners = new HashMap<>();
	    Set<Set<Point>> territories = identifyTerritory(polje);
	    
	    
	    for (Set<Point> territory : territories) {
	        Zeton owner = determineTerritoryOwner(territory, polje);

	        if (!territoryOwners.containsKey(owner)) {
	            territoryOwners.put(owner, new HashSet<>());
	        }
	        territoryOwners.get(owner).add(territory);
	    }

	    return territoryOwners;
	}
	
	private Zeton determineTerritoryOwner(Set<Point> territory, Polje polje) {
	    Zeton owner = null;

	    for (Point position : territory) {
	        Set<Zeton> adjacentStones = getAdjacentStones(position, polje);

	        if (adjacentStones.size() == 1) {
	            owner = adjacentStones.iterator().next();
	        } else {
	            owner = null; // Territory has multiple adjacent players' stones, cannot determine ownership
	            break;
	        }
	    }

	    return owner;
	}
	private Set<Zeton> getAdjacentStones(Point position, Polje polje) {
	    int x = position.x;
	    int y = position.y;
	    int size = velikostPlosce;

	    Set<Zeton> adjacentStones = new HashSet<>();

	    if (x > 1)
	        addStone(adjacentStones, polje.grid[x - 1][y]);
	    if (x < size)
	        addStone(adjacentStones, polje.grid[x + 1][y]);
	    if (y > 1)
	        addStone(adjacentStones, polje.grid[x][y - 1]);
	    if (y < size)
	        addStone(adjacentStones, polje.grid[x][y + 1]);

	    return adjacentStones;
	}
	
	private void addStone(Set<Zeton> stones, Zeton stone) {
	    if (stone != null) {
	        stones.add(stone);
	    }
	}
	
	public Map<Zeton, Integer> calculateScore(Polje polje) {
	    Map<Zeton, Set<Set<Point>>> territoryOwners = getTerritoryOwners(polje);
	    Map<Zeton, Integer> scores = new HashMap<>();

//	    for (Zeton player : Zeton.values()) {
//	        scores.put(player, 0);
//	    }

	    for (Entry<Zeton, Set<Set<Point>>> entry : territoryOwners.entrySet()) {
	        Zeton owner = entry.getKey();
	        for(Set<Point> territory : entry.getValue()) {
		        if (owner == Zeton.BELI) {
		            int territoryScore = territory.size();
		            beliTocke += territoryScore;
		        }
		        if (owner == Zeton.CRNI) {
		            int territoryScore = territory.size();
		            crniTocke += territoryScore;
	        }
	        }
	    }
	    scores.put(Zeton.CRNI, crniTocke);
	    scores.put(Zeton.BELI, beliTocke);

	    return scores;
	}

	public Stanje konecIgre() {
		int consecutivePasses = (consecutivePassesBeli + consecutivePassesCrni);
		Stanje stanje = Stanje.V_TEKU;
        if(consecutivePassesBeli == 2) {
        	tipZmage = 0;
        	stanje = Stanje.ZMAGA_CRNI;
        }
        else if(consecutivePassesCrni == 2 ) {
        	tipZmage = 0;
        	stanje = Stanje.ZMAGA_BELI;
        }
        else if(consecutivePasses == 2) {
        	Map<Zeton, Integer> rezultat = calculateScore(polje);
        	if(crniTocke > beliTocke) {
	        	tipZmage = 1;
	        	stanje = Stanje.ZMAGA_CRNI;
        	}
        	else if(crniTocke < beliTocke) {
        		tipZmage = 1;
        		stanje = Stanje.ZMAGA_BELI;
        	}
        }
        return stanje;
        
	}
	
	
}
