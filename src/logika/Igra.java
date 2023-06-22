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

	public int zaporedniPassiBeli;

	public int zaporedniPassiCrni;

	public int tipZmage = -1;

	public boolean captureGo;

	public Polje polje;

	public Igralec naPotezi;

	public Stanje stanje;

	public List<Polje> prejsnjaStanja;

	public Igra() {

		int N = velikostPlosce;
		polje = new Polje(N);
		stanje = Stanje.V_TEKU;
		crniTocke = 0;
		beliTocke = velikostPlosce / 4; // kompenzacija, ker zacne kasneje
		zaporedniPassiCrni = 0;
		zaporedniPassiBeli = 0;
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
		if (naPotezi == Igralec.BELI)
			return Zeton.BELI;
		else
			return Zeton.CRNI;
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
		prostaPolja.add(new Poteza(-1, -1)); // dodamo potezo za pass
		return prostaPolja;
	}

	public void pass() {
		if (naPotezi == Igralec.BELI)
			zaporedniPassiBeli++;
		else
			zaporedniPassiCrni++;
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
		} else {
			zeton = Zeton.BELI;
		}
		// Preverimo, če je poteza veljavna
		if (!jeVeljavnaPoteza(x, y)) {
			return false;
		}
		if (semiSuicide(x, y))
			semi = true;
		// Izvedemo potezo
		polje.dodajZeton(x, y, zeton);
		naPotezi = naPotezi.nasprotnik();
		Set<Point> obiskane = new HashSet<>();
		Set<Point> grupa = najdiGrupo(x,y,zeton,obiskane,polje);
		if (polje.odstraniUjeteZetone(captureGo)) {
			tipZmage = 2;
			if (zeton == zeton.BELI)
				stanje = stanje.ZMAGA_BELI;
			else
				stanje = stanje.ZMAGA_CRNI;
		}
		if (semi) {
			for(Point point : grupa)
			polje.dodajZeton(point.x, point.y, zeton); // ce je semiSuicide je treba se enkrat dodat kamne iz te grupe, drugace ga tretira kot
											// ujetega
		}
		if (zeton == Zeton.CRNI)
			zaporedniPassiCrni = 0;
		if (zeton == Zeton.BELI)
			zaporedniPassiBeli = 0;

		prejsnjaStanja.add(polje.kopija());
		konecIgre();
		return true;
	}

	public boolean jeVeljavnaPoteza(int x, int y) {
		// Pogledamo, če so koordinate veljavne in je prazno polje
		Zeton igralec;
		if (x != -1)
			igralec = polje.grid[x][y];
		if (x < 0 || x >= velikostPlosce + 1 || y < 0 || y >= velikostPlosce + 1 || polje.grid[x][y] != null) {
			return false;
		}

		if (isSuicideViolation(x, y) || isKoRuleViolation(x, y)) {
			return false;
		}

		// Če je poteza veljavna, vrni true
		return true;
	}

	public Set<Point> najdiGrupo(int x, int y, Zeton player, Set<Point> obiskane, Polje polje) {
		Set<Point> grupa = new HashSet<>(); // Set to store the grupa of zetons

		// Poklicemo dfs da dobimo grupe
		dfs(x, y, player, grupa, obiskane, polje);

		return grupa;
	}

	private void dfs(int x, int y, Zeton player, Set<Point> grupa, Set<Point> obiskane, Polje polje) {
		if (x < 1 || x > velikostPlosce || y < 1 || y > velikostPlosce || obiskane.contains(new Point(x, y))) {
			return;
		}

		Point current = new Point(x, y);

		if (polje.grid[x][y] != player) {
			obiskane.add(current);
			return;
		}

		obiskane.add(current);
		grupa.add(current);

		dfs(x - 1, y, player, grupa, obiskane, polje);
		dfs(x + 1, y, player, grupa, obiskane, polje);
		dfs(x, y - 1, player, grupa, obiskane, polje);
		dfs(x, y + 1, player, grupa, obiskane, polje);
	}

	public boolean imaLiberty(int x, int y) {
		Polje kopija = polje.kopija();
		Zeton zeton = zetonNaPotezi();
		kopija.dodajZeton(x, y, zeton);
		Set<Point> obiskane = new HashSet<>();
		Set<Point> grupa = najdiGrupo(x, y, zeton, obiskane, kopija);
		for (Point tocka : grupa) {
			if (imaPraznegaSoseda(tocka.x, tocka.y)) {
				return true;
			}
		}
		return false;
	}

	private boolean imaPraznegaSoseda(int x, int y) {
		if (x > 1 && polje.grid[x - 1][y] == null) {
			return true; 
		}
		if (x < velikostPlosce && polje.grid[x + 1][y] == null) {
			return true; 
		}
		if (y > 1 && polje.grid[x][y - 1] == null) {
			return true;
		}
		if (y < velikostPlosce && polje.grid[x][y + 1] == null) {
			return true;
		}

		return false;
	}

	private boolean semiSuicide(int x, int y) {// ce bi igral zgleda kot suicide, vendar ujame nasprotnikovo grupo in ni
												// suicide
		boolean captureNajdeno = false;
		Polje kopija = polje.kopija();
		kopija.dodajZeton(x, y, zetonNaPotezi());
		if (y > 1 && kopija.isCaptured(x, y - 1)) { 
			captureNajdeno = true;
		}
		if (y < velikostPlosce && kopija.isCaptured(x, y + 1)) { 
			captureNajdeno = true;
		}
		if (x > 1 && kopija.isCaptured(x - 1, y)) { 
			captureNajdeno = true;
		}
		if (x < velikostPlosce && kopija.isCaptured(x + 1, y)) { 
			captureNajdeno = true;
		}
		return captureNajdeno;
	}

	public boolean isSuicideViolation(int x, int y) {
		Set<Point> obiskane = new HashSet<>();
		for(Point point : najdiGrupo(x,y,zetonNaPotezi(),obiskane, polje))
			if(imaLiberty(point.x,point.y) || semiSuicide(point.x, point.y)) return false;
		if (imaLiberty(x, y) || semiSuicide(x, y)) {
			return false;
		}
		return true;
	}

	public boolean isKoRuleViolation(int x, int y) {
		boolean semi = false;
		Polje trenutnoPolje = polje.kopija();
		if (semiSuicide(x, y))
			semi = true;
		// Izvedemo potezo
		trenutnoPolje.dodajZeton(x, y, zetonNaPotezi());
		trenutnoPolje.odstraniUjeteZetone(captureGo);
		if (semi)
			trenutnoPolje.dodajZeton(x, y, zetonNaPotezi());
		for (Polje prejsnjePolje : prejsnjaStanja) {
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
		if (x != -1 && polje.isCaptured(x, y)) {
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
		kopija.zaporedniPassiBeli = this.zaporedniPassiBeli;
		kopija.zaporedniPassiCrni = this.zaporedniPassiCrni;
		return kopija;
	}

	public Polje dobiPolje() {
		return polje;
	}

	public Set<Set<Point>> najdiTeritorij(Polje polje) {
		Set<Set<Point>> teritoriji = new HashSet<>();
		Set<Point> obiskane = new HashSet<>();

		for (int x = 1; x <= velikostPlosce; x++) {
			for (int y = 1; y <= velikostPlosce; y++) {
				Point pozicija = new Point(x, y);
				if (!obiskane.contains(pozicija) && polje.grid[x][y] == null) {
					Set<Point> teritorij = najdiGrupo(x, y, null, obiskane, polje);
					teritoriji.add(teritorij);
				}
			}
		}
		return teritoriji;
	}

	public Map<Zeton, Set<Set<Point>>> dobiLastnikaTeritorija(Polje polje) {
		Map<Zeton, Set<Set<Point>>> lastnikiTeritorijev = new HashMap<>();
		Set<Set<Point>> teritoriji = najdiTeritorij(polje);

		for (Set<Point> teritorij : teritoriji) {
			Zeton owner = determineTerritoryOwner(teritorij, polje);

			if (!lastnikiTeritorijev.containsKey(owner)) {
				lastnikiTeritorijev.put(owner, new HashSet<>());
			}
			lastnikiTeritorijev.get(owner).add(teritorij);
		}

		return lastnikiTeritorijev;
	}

	private Zeton determineTerritoryOwner(Set<Point> teritorij, Polje polje) {
		Zeton owner = null;

		for (Point position : teritorij) {
			Set<Zeton> sosednjiZetoni = getAdjacentStones(position, polje);

			if (sosednjiZetoni.size() == 1) {
				owner = sosednjiZetoni.iterator().next();
			} else {
				owner = null; // Obmocje ima kamne obeh igralcev
				break;
			}
		}

		return owner;
	}

	private Set<Zeton> getAdjacentStones(Point position, Polje polje) {
		int x = position.x;
		int y = position.y;
		int size = velikostPlosce;

		Set<Zeton> sosednjiZetoni = new HashSet<>();

		if (x > 1)
			dodajZeton(sosednjiZetoni, polje.grid[x - 1][y]);
		if (x < size)
			dodajZeton(sosednjiZetoni, polje.grid[x + 1][y]);
		if (y > 1)
			dodajZeton(sosednjiZetoni, polje.grid[x][y - 1]);
		if (y < size)
			dodajZeton(sosednjiZetoni, polje.grid[x][y + 1]);

		return sosednjiZetoni;
	}

	private void dodajZeton(Set<Zeton> zetons, Zeton zeton) {
		if (zeton != null) {
			zetons.add(zeton);
		}
	}

	public Map<Zeton, Integer> izracunajRezultat(Polje polje) {
		Map<Zeton, Set<Set<Point>>> lastnikiTeritorijev = dobiLastnikaTeritorija(polje);
		Map<Zeton, Integer> rezultati = new HashMap<>();

		for (Entry<Zeton, Set<Set<Point>>> entry : lastnikiTeritorijev.entrySet()) {
			Zeton owner = entry.getKey();
			for (Set<Point> teritorij : entry.getValue()) {
				if (owner == Zeton.BELI) {
					int teritorijRezultat = teritorij.size();
					beliTocke += teritorijRezultat;
				}
				if (owner == Zeton.CRNI) {
					int teritorijRezultat = teritorij.size();
					crniTocke += teritorijRezultat;
				}
			}
		}
		for (int x = 1; x <= velikostPlosce; x++) {
			for (int y = 1; y <= velikostPlosce; y++) {
				if (polje.grid[x][y] == Zeton.BELI)
					beliTocke++;
				if (polje.grid[x][y] == Zeton.CRNI)
					crniTocke++;
			}
		}
		rezultati.put(Zeton.CRNI, crniTocke);
		rezultati.put(Zeton.BELI, beliTocke);

		return rezultati;
	}

	public Stanje konecIgre() {
		int zaporedniPassi = (zaporedniPassiBeli + zaporedniPassiCrni);
		Stanje stanje = Stanje.V_TEKU;
		if (zaporedniPassiBeli == 2) {
			tipZmage = 0;
			stanje = Stanje.ZMAGA_CRNI;
		} else if (zaporedniPassiCrni == 2) {
			tipZmage = 0;
			stanje = Stanje.ZMAGA_BELI;
		} else if (zaporedniPassi == 2) {
			Map<Zeton, Integer> rezultat = izracunajRezultat(polje);
			if (crniTocke > beliTocke) {
				tipZmage = 1;
				stanje = Stanje.ZMAGA_CRNI;
			} else if (crniTocke <= beliTocke) {
				tipZmage = 1;
				stanje = Stanje.ZMAGA_BELI;
			}
		}
		return stanje;

	}

}
