package inteligenca;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import logika.*;
import splosno.KdoIgra;
import splosno.Poteza;
import vodja.Vodja;

public class Inteligenca extends KdoIgra {

	public Inteligenca() {
		super("Shusaku");
	}

	public Poteza izberiPotezo(Igra igraOg) {
		Igra igra = igraOg.kopija(); // naredimo kopijo igre
		Igralec igralec = igra.naPotezi;
		Poteza zmagovalna = null;
		long endTime = System.currentTimeMillis() + 5000; // Za potezo ima 5s
		Igralec nasprotnik = igralec.nasprotnik();
		Drevo drevo = new Drevo(new Vozel(igra)); // ustvarimo novo drevo in damo trenuten vozel za koren
		Vozel korenVozel = drevo.dobiKoren();
		korenVozel.dobiState().nastaviIgra(igra);
		korenVozel.nastaviIgralec(igralec);
		Random random = new Random();
		while (System.currentTimeMillis() < endTime) {
			Vozel promisingVozel = izberiObetavenVozel(korenVozel);
			if (promisingVozel.getStanje() == Stanje.V_TEKU) {
				podaljsajVozel(promisingVozel);
			}
			Vozel vozelZaRaziskat = promisingVozel;
			if (promisingVozel.dobiOtroke().size() > 0) {
				vozelZaRaziskat = promisingVozel.dobiOtroke().get(random.nextInt(promisingVozel.dobiOtroke().size()));
			}
			List<String> rezultati = odigrajVzporedneIgre(vozelZaRaziskat, igralec);
			for (int l = 0; l < 10; l++) {
				String rezultatIgre = rezultati.get(l);
				pogledNazaj(vozelZaRaziskat, rezultatIgre);
			}
		}
		Vozel zmagovalniVozel = korenVozel.getNajboljsiOtrok();
		drevo.nastaviKoren(zmagovalniVozel);
		Polje originalPolje = igra.dobiPolje();
		Polje updatedPolje = zmagovalniVozel.dobiPolje();
		for (int i = 1; i < igra.velikostPlosce + 1; i++) {
			for (int j = 1; j < igra.velikostPlosce + 1; j++) {
				if (originalPolje.grid[i][j] == null && updatedPolje.grid[i][j] != null) {
					zmagovalna = new Poteza(i, j);
				}
			}
		}
		return zmagovalna;
	}

	private Vozel izberiObetavenVozel(Vozel korenVozel) {
		Vozel vozel = korenVozel;
		while (!vozel.otroci.isEmpty()) {
			vozel = UCT.najdiNajboljsiVozelUCT(vozel);
		}
		return vozel;
	}

	private void podaljsajVozel(Vozel vozel) {
		List<Poteza> moznaStanja = vozel.dobiIgro().moznePoteze();
		moznaStanja.forEach(poteza -> {
			Igra novaIgra = vozel.dobiIgro().kopija();
			novaIgra.odigraj(poteza);
			Vozel newVozel = new Vozel(novaIgra);
			newVozel.stars = vozel;
			vozel.otroci.add(newVozel);
		});
	}

	private void pogledNazaj(Vozel vozelZaRaziskat, String rezultat) {
		Vozel zacasenVozel = vozelZaRaziskat;
		while (zacasenVozel != null) {
			zacasenVozel.state.steviloObiskov++;
			if (rezultat != null && rezultat.equals("zmaga")) {
				zacasenVozel.state.dodajSteviloZmag(1);
			}
			zacasenVozel = zacasenVozel.dobiStarsa();
		}
	}

	private String simulirajNakljucnoIgro(Vozel vozel, Igralec igralec) {
		Igralec nasprotnik = igralec.nasprotnik();
		Vozel zacasenVozel = vozel.kopija();
		Stanje boardStatus = zacasenVozel.getStanje();
		String rezultat = null;

		while (boardStatus == Stanje.V_TEKU) {
			zacasenVozel.igra.odigrajNakljucnoPotezo();
			boardStatus = zacasenVozel.igra.dobiStanje();
		}
		if ((boardStatus == Stanje.ZMAGA_BELI && nasprotnik == Igralec.BELI)
				|| (boardStatus == Stanje.ZMAGA_CRNI && nasprotnik == Igralec.ČRNI)) {
			rezultat = "poraz";
		} else
			rezultat = "zmaga";
		return rezultat;
	}

	public List<String> odigrajVzporedneIgre(Vozel vozel, Igralec igralec) {
		int steviloIger = 10;
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		List<Future<String>> results = new ArrayList<>();

		for (int i = 0; i < steviloIger; i++) {
			Future<String> result = (Future<String>) executor.submit(() -> simulirajNakljucnoIgro(vozel, igralec));
			results.add(result);
		}

		// Pocakamo da se igre zakljucijo
		executor.shutdown();
		while (!executor.isTerminated()) {
			// Pocakamo da se vsi taski zaklucijo
		}
		List<String> rezultatiIger = new ArrayList<>();
		for (Future<String> result : results) {
			try {
				String rezultatIgre = result.get();
				rezultatiIger.add(rezultatIgre);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rezultatiIger;
	}

	public class Vozel {
		Igra igra;
		Vozel stars;
		List<Vozel> otroci;
		State state;
		Igralec naVrsti;

		public Vozel(Igra igra) {
			this.igra = igra;
			this.otroci = new ArrayList<>();
			this.state = new State(igra);
		}

		public void odigrajNakljucnoPotezoNaVozlu() {
			igra.odigrajNakljucnoPotezo();

		}

		public void nastaviIgralec(Igralec igralec) {
			this.naVrsti = igralec;
		}

		public State dobiState() {
			return state;
		}

		public void nastaviState(State state) {
			this.state = state;
		}

		public Stanje getStanje() {
			return igra.stanje;
		}

		public void nastaviStanje(Stanje stanje) {
			this.igra.stanje = stanje;
		}

		public List<Vozel> dobiOtroke() {
			return otroci;
		}

		public Vozel dobiStarsa() {
			return stars;
		}

		public Polje dobiPolje() {
			return igra.polje;
		}

		public void nastaviPolje(Polje polje) {
			this.igra.polje = polje;
		}

		public Igra dobiIgro() {
			return igra;
		}

		public void nastaviIgra(Igra igra) {
			this.igra = igra;
		}

		public Vozel kopija() {
			Vozel kopija = new Vozel(igra.kopija());
			kopija.otroci = otroci;
			kopija.stars = stars;
			kopija.state = state;
			return kopija;
		}

		public Vozel getNajboljsiOtrok() {
			int najboljsi = 0;
			for (int i = 0; i < otroci.size(); i++) {
				if (otroci.get(i).state.steviloZmag > otroci.get(najboljsi).state.steviloZmag) {
					najboljsi = i;
				}
			}
			return otroci.get(najboljsi);
		}
	}

	public class Drevo {
		Vozel koren;

		public Drevo(Vozel koren) {
			this.koren = koren;
		}

		public Vozel dobiKoren() {
			return koren;
		}

		public void nastaviKoren(Vozel koren) {
			this.koren = koren;
		}
	}

	public class State {
		Igra igra;
		Igralec igralecNaVrsti;
		int steviloObiskov;
		double steviloZmag;

		public State(Igra igra) {
			this.igra = igra;
			this.igralecNaVrsti = igra.naPotezi;
			this.steviloObiskov = 0;
			this.steviloZmag = 0;
		}

		public State(Igra igra, Igralec igralecNaVrsti, int steviloObiskov, double steviloZmag) {
			this.igra.polje = igra.polje;
			this.igralecNaVrsti = igralecNaVrsti;
			this.steviloObiskov = steviloObiskov;
			this.steviloZmag = steviloZmag;
		}

		public Stanje getStanje() {
			return igra.stanje;
		}

		public Igra dobiIgro() {
			return igra;
		}

		public void nastaviIgra(Igra igra) {
			this.igra = igra;
		}

		public Igralec dobiIgrolecNaVrsti() {
			return igralecNaVrsti;
		}

		public void nastaviIgralecNaVrsti(Igralec igralec) {
			this.igralecNaVrsti = igralec;
		}

		public int dobiSteviloObiskov() {
			return steviloObiskov;
		}

		public void nastaviSteviloObiskov(int i) {
			this.steviloObiskov = i;
		}

		public void nastaviSteviloZmag(double i) {
			this.steviloZmag = i;
		}

		public double dobiSteviloZmag() {
			return steviloZmag;
		}

		public void dodajSteviloZmag(double i) {
			this.steviloZmag += i;
		}

		public List<State> dobiVsaMoznaStanja() {
			List<State> moznaStanja = new ArrayList<>();
			List<Poteza> praznaPolja = igra.moznePoteze();

			for (Poteza poteza : praznaPolja) {
				Igra novaIgra = igra;
				novaIgra.odigraj(poteza);
				State novoStanje = new State(novaIgra, novaIgra.naPotezi, 0, 0);
				moznaStanja.add(novoStanje);
			}

			return moznaStanja;
		}
	}

	public class UCT {
		public static double uctVrednost(int steviloVsehObiskov, double vozelSteviloZmag, int vozelSteviloObiskov) {
			if (vozelSteviloObiskov == 0) {
				return Integer.MAX_VALUE;
			}
			return ((double) vozelSteviloZmag / (double) vozelSteviloObiskov)
					+ 1.41 * Math.sqrt(Math.log(steviloVsehObiskov) / (double) vozelSteviloObiskov);
		}

		public static Vozel najdiNajboljsiVozelUCT(Vozel vozel) {
			int starsObiski = vozel.state.steviloObiskov;
			return Collections.max(vozel.otroci,
					Comparator.comparing(c -> uctVrednost(starsObiski, c.state.steviloZmag, c.state.steviloObiskov)));
		}

	}

}
