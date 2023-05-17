package inteligenca;

import logika.*;
import splosno.KdoIgra;
import splosno.Poteza;

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
	    for (Poteza poteza : Igra.moznePoteze()) {
	        double ocenjevanje = 0;

	        // simulacije
	        
	        int[] steviloZmag = new int[2];
	        int beli = 0;
	        int crni = 0;
	        for (int i = 0; i < stSimulacij; i++) {
	        	
	        	
	        	
	            Igra kopijaIgre = new Igra(); // ustvari kopijo igre
	            Stanje stanje = kopijaIgre.dobiStanje();

	            // Igraj random poteze do konca igree
	            while (!kopijaIgre.aktivna(stanje)) {
	                kopijaIgre.odigrajNakljucnoPotezo();
	            }
	            
	            
	            if (stanje == Stanje.ZMAGA_CRNI) ++crni;
	            if (stanje == Stanje.ZMAGA_BELI) ++beli;
	            
	            
	            
	            // Evaluate the outcome of the game
	            int rezultat = steviloZmag[igra.naPotezi().nasprotnik().ordinal()];
	            ocenjevanje += rezultat;

	        }
	        
	        steviloZmag[Igralec.ČRNI.ordinal()] += crni;
	        steviloZmag[Igralec.BELI.ordinal()] += beli;


	        ocenjevanje /= stSimulacij; // Average the outcomes

	        // Choose the move with the highest average outcome
	        if (ocenjevanje > najboljseOcenjevanje) {
	            najboljseOcenjevanje = ocenjevanje;
	            najboljsaPoteza = poteza;
	        }
	    }

	    long konec = System.currentTimeMillis();
	    long trajanje = konec - zacetek;
	    System.out.println("Iščem potezo: " + trajanje + " milisekund");

	    return najboljsaPoteza;
	}
	
	public void narediPotezo(Poteza poteza) {
		// naredi najboljso mozno potezo
		
		Igra.odigraj(poteza);
		
		//updejtaj stanje igre in plosco
	}
	
	

}




