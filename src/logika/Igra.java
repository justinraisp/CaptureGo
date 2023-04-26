package logika;

import java.util.LinkedList;
import java.util.List;

public class Igra {
	
	//tu se mora ustvariti nova igra
	public final static int velikostPlosce = 19;
	
	//mozne poteze
	
	//vse vrste
	private static final List<Vrsta> VRSTE = new LinkedList<Vrsta>();

	
	//igralno polje
	private Polje[][] plosca;
	
	
	//igralec, ki je trenutno na potezi
	private Igralec naPotezi;
	
	
	public Igra() {
		final int N = velikostPlosce;
		plosca = new Polje[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				plosca[i][j] = Polje.PRAZNO;
			}
		}
		naPotezi = Igralec.ČRNI;
	}
	
	
	public Igralec naPotezi () {
		return naPotezi;
	}
	
	public Polje[][] getPlosca () {
		return plosca;
	}
	
	
	public List<Koordinate> moznePoteze() {
		final int N = velikostPlosce;
		LinkedList<Koordinate> prostaPolja = new LinkedList<Koordinate>();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (plosca[i][j] == Polje.PRAZNO) {
					prostaPolja.add(new Koordinate(i, j));
				}
			}
		}
		return prostaPolja;
	}

	//boolean odigraj(Poteza poteza). Metoda naj vrne true, če je poteza možna,
	//sicer pa false. Uporabite razred Poteza za 
	//koordinate od Poteza(0,0) (levo zgoraj) do Poteza(8,8) (desno spodaj).
	
	
	
	
}
   