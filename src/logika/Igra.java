package logika;

import java.util.LinkedList;
import java.util.List;

public class Igra {
	
	//tu se mora ustvariti nova igra
	public static int velikostPlosce = 9;
	

	
	//mozne poteze
	
	//vse vrste
	private static final List<Vrsta> VRSTE = new LinkedList<Vrsta>();

	
	//igralno polje
	private Polje plosca;
	
	
	//igralec, ki je trenutno na potezi
	private Igralec naPotezi;
	
	
	public Igra() {
		int N = velikostPlosce;
		plosca = new Polje(N);
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				plosca.grid[i][j] = null;
			}
		}
		naPotezi = Igralec.ČRNI;
	}
	
	
	public Igralec naPotezi () {
		return naPotezi;
	}
	
	public Polje getPlosca () {
		return plosca;
	}
	
	
	public List<Koordinate> poteze() {
		int N = velikostPlosce;
		LinkedList<Koordinate> ps = new LinkedList<Koordinate>();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (plosca.grid[i][j] == null) {
					ps.add(new Koordinate(i, j));
				}
			}
		}
		return ps;
	}

	//boolean odigraj(Poteza poteza). Metoda naj vrne true, če je poteza možna,
	//sicer pa false. Uporabite razred Poteza za 
	//koordinate od Poteza(0,0) (levo zgoraj) do Poteza(8,8) (desno spodaj).
	
	
}
   