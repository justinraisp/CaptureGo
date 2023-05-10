package inteligenca;

import logika.*;
import splosno.KdoIgra;
import splosno.Poteza;

public class Inteligenca extends KdoIgra {


	public Inteligenca() {
		super("Shusaku");
	}
	
	
	private Poteza izberiPotezo(Igra igra) {
		long zacetek = System.currentTimeMillis();
	    Poteza najboljsaPoteza = null;
	    double najboljseOcenjevanje = Double.NEGATIVE_INFINITY;
        
	    // Perform Monte Carlo simulations
	    int stSimulacij = 1000; // Number of simulations to perform
	    for (Koordinate poteza : Igra.moznePoteze()) {
	        double ocenjevanje = 0;

	        // Perform simulations for each move
	        for (int i = 0; i < stSimulacij; i++) {
	        	int beli = 0;
	        	int crni = 0;
	        	
	            Igra kopijaIgre = new Igra(); // Create a copy of the game
	            Stanje stanje = kopijaIgre.dobiStanje();

	            // Play random moves until the end of the game
	            while (!kopijaIgre.aktivna(stanje)) {
	                kopijaIgre.odigrajNakljucnoPotezo();
	            }
	            
	            
	            if (stanje == Stanje.ZMAGA_CRNI) ++crni;
	            if (stanje == Stanje.ZMAGA_BELI) ++beli;
	            
	            
	           
	            
	            
	            
	            // Evaluate the outcome of the game
	            int rezultat = kopijaIgre.steviloZmag[];
	            ocenjevanje += rezultat;
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
	    System.out.println("Iščem potezo: " + trajanje + " milisekund");

	    return najboljsaPoteza;
	}
	
	private void narediPotezo(Poteza poteza) {
		// naredi najboljso mozno potezo
		
		Igra.odigraj(poteza);
		
		//updejtaj stanje igre in plosco
	}
	
	

}





