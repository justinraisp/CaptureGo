package vodja;

import java.util.Random;
import java.util.Map;
import java.util.List;

import javax.swing.SwingWorker;
import java.util.concurrent.TimeUnit;

import gui.Okno;
import inteligenca.Inteligenca;
import logika.*;
import splosno.Poteza;

public class Vodja {

	public static Map<Igralec, VrstaIgralca> vrstaIgralca;

	public static Okno okno;

	public static Igra igra = null;

	public static boolean clovekNaVrsti = false;

	static Inteligenca inteligenca = new Inteligenca();

	public static void igramoNovoIgro() {
		igra = new Igra();
		igramo();
	}

	public static void igramo() {
		// Poteza poteza = null;
		// okno.osveziGUI();

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
				// Poteza poteza = inteligenca.izberiPotezo(igra);
				Poteza poteza = inteligenca.izberiPotezo(igra);
				System.out.println(poteza);
				igrajRacunalnikovoPotezo(poteza);
				break;
			}
		}
		// okno.osveziGUI();
	}

	private static Random random = new Random();

	public static void igrajRacunalnikovoPotezo(Poteza poteza) {
		if (poteza != null && igra.odigraj(poteza)) {
			// igra.odigraj(poteza);
			clovekNaVrsti = true;
			igramo();
		}
	}

	public static void igrajClovekovoPotezo(Poteza poteza) {
		if (poteza != null && igra.odigraj(poteza)) {
			// igra.odigraj(poteza);
			clovekNaVrsti = false;
			igramo();
		}
	}

}
