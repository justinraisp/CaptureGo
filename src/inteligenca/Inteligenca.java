package inteligenca;

import logika.*;
import splosno.KdoIgra;
import splosno.Poteza;
import vodja.Vodja;

public class Inteligenca extends KdoIgra {


	public Inteligenca() {
		super("Shusaku");
	}
	
	
	public Poteza izberiPotezo(Igra igra) {
		long zacetek = System.currentTimeMillis();
	    Poteza najboljsaPoteza = null;
	    double najboljseOcenjevanje = Double.NEGATIVE_INFINITY;
        
	    // Perform Monte Carlo simulations
	    int stSimulacij = 1000; // Number of simulations to perform

	    for (Poteza poteza : igra.moznePoteze()) {

	        double ocenjevanje = 0;

	        // simulacije
	        int beli = 0;
	        int crni = 0;
	        for (int i = 0; i < stSimulacij; i++) {
	        	Igra kopijaIgre = new Igra(); // ustvari kopijo igre
	        	kopijaIgre.odigraj(poteza);
	            Stanje stanje = kopijaIgre.dobiStanje();

	            // Igraj random poteze do konca igree
	            while (!kopijaIgre.aktivna(stanje)) {
	                kopijaIgre.odigrajNakljucnoPotezo();
	            }
	            
	            int[] steviloZmag = new int[2];
	            if (stanje == Stanje.ZMAGA_CRNI) ++crni;
	            if (stanje == Stanje.ZMAGA_BELI) ++beli;
	            
	            
	            
	            // Evaluate the outcome of the game
	            int rezultat = steviloZmag[kopijaIgre.naPotezi().ordinal()];
	            ocenjevanje += rezultat;
	            
	            steviloZmag[Igralec.ČRNI.ordinal()] += crni;
	            steviloZmag[Igralec.BELI.ordinal()] += beli;

	        }
	        
	        


	        ocenjevanje /= stSimulacij; // Average the outcomes

	        // Choose the move with the highest average outcome
	        if (ocenjevanje > najboljseOcenjevanje) {
	            najboljseOcenjevanje = ocenjevanje;
	            najboljsaPoteza = poteza;
	        }
	    }

	    long konec = System.currentTimeMillis();
	    long trajanje = konec - zacetek;
	    System.out.println("Potezo sem iskal: " + trajanje + " milisekund");

	    return najboljsaPoteza;
	}
	
}





