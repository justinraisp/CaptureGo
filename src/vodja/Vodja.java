package vodja;

import java.util.Random;
import java.util.Map;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.util.concurrent.TimeUnit;

import gui.IgralnoPolje;
import gui.Okno;
import inteligenca.Inteligenca;
import logika.*;
import splosno.Poteza;

public class Vodja {

	public static Map<Igralec, VrstaIgralca> vrstaIgralca;

	public static Okno okno;

	public static Igra igra = null;

	public static boolean clovekNaVrsti = false;
	
	private static int consecutivePassesBeli = 0;
	
	private static int consecutivePassesCrni = 0;

	static Inteligenca inteligenca = new Inteligenca();

	public static void igramoNovoIgro() {
		igra = new Igra();
		Igralec igralec = igra.naPotezi();
		IgralnoPolje.napisNaVrsti(igralec);
		igramo();
	}

	public static void igramo() {
		IgralnoPolje.napisNaVrsti(igra.naPotezi());
	    switch (igra.dobiStanje()) {
	        case ZMAGA_BELI:
	            break;
	        case ZMAGA_CRNI:
	            break;
	        case V_TEKU:
	            Igralec igralec = igra.naPotezi();
	            VrstaIgralca vrstaNaPotezi = vrstaIgralca.get(igralec);
	            switch (vrstaNaPotezi) {
	                case C:
	                    clovekNaVrsti = true;
	                    break;
	                case R:
	                    SwingUtilities.invokeLater(() -> {
	                        Poteza poteza = inteligenca.izberiPotezo(igra);
	                        igrajRacunalnikovoPotezo(poteza);
	                    });
	                    break;
	            }
	            break;
	    }
	}


	public static void igrajRacunalnikovoPotezo(Poteza poteza) {
	    if (poteza != null && igra.odigraj(poteza)) {
	        clovekNaVrsti = false;
	        SwingUtilities.invokeLater(() -> {
	            // Delay the board refresh to allow the UI to update
	            try {
	                TimeUnit.MILLISECONDS.sleep(200); // Adjust the delay as needed
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	            igramo();
	            okno.repaint();
	        });
	    }
	}

	public static void igrajClovekovoPotezo(Poteza poteza) {
	    if (poteza != null && igra.odigraj(poteza)) {
	    	if(igra.naPotezi == Igralec.ČRNI) {
	    		consecutivePassesBeli = 0;
	    	}
	    	else consecutivePassesCrni = 0;
	        clovekNaVrsti = false;
	        SwingUtilities.invokeLater(() -> {
	            try {
	                TimeUnit.MILLISECONDS.sleep(200); // Adjust the delay as needed
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	            igramo();
	        });
	    }
	}
	public static void pass() {
	    	if(igra.naPotezi == Igralec.BELI) {
	    		consecutivePassesBeli++;
	    	}
	    	else consecutivePassesCrni++;
	        igra.pass();
	        if(consecutivePassesBeli == 2) igra.stanje = Stanje.ZMAGA_CRNI;
	        if(consecutivePassesCrni == 2) igra.stanje = Stanje.ZMAGA_BELI;
	        SwingUtilities.invokeLater(() -> {
	            try {
	                TimeUnit.MILLISECONDS.sleep(500); // Adjust the delay as needed
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	            System.out.println(consecutivePassesCrni);
	            igramo();
	        });
	}
	
}
