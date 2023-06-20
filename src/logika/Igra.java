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

	// tu se mora ustvariti nova igra
	public static int velikostPlosce = 19;

	// mozne poteze

	// igralno polje
	public Polje polje;

	// igralec, ki je trenutno na potezi
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

	public Igralec naPotezi() {
		return naPotezi;
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

		// Preverimo, če je kateri od igralcev ujel nasprotnikov kamen
		if (polje.isCaptured(x, y)) {
			if (zeton == Zeton.CRNI) {
				stanje = Stanje.ZMAGA_CRNI;
			} else {
				stanje = Stanje.ZMAGA_BELI;
			}
		}
		// System.out.println(moznePoteze());
		return true;
	}
	    

	public boolean jeVeljavnaPoteza(int x, int y) {
		// Pogledamo, če so koordinate veljavne in je prazno polje
		Zeton igralec = polje.grid[x][y];
		if (x < 0 || x >= velikostPlosce + 1 || y < 0 || y >= velikostPlosce + 1 || polje.grid[x][y] != null) {
			return false;
		}

		 if (isKoRuleViolation(x, y)) {
		 return false;
		 }

		// Postavimo kamen in pogledamo, če je ujet
		polje.grid[x][y] = igralec;
		boolean captured = polje.isCaptured(x, y);

		// If the move gets the stone captured, return false
		if (captured) {
			polje.grid[x][y] = igralec;
			return true;
		}

		// Če je poteza veljavna, vrni true
		return true;
	}

	public boolean isKoRuleViolation(int x, int y) {

		if (polje.isCaptured(x, y)) {
			return true;
		}

		Set<String> visitedStates = new HashSet<>();
		if (visitedStates.contains(stanje.toString())) {
			return true;
		}
		visitedStates.add(stanje.toString());

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
