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

	static Inteligenca inteligenca = new Inteligenca();

	public static void igramoNovoIgro(boolean captureGo) {
		igra = new Igra();
		if (captureGo)
			igra.captureGo = true;
		Igralec igralec = igra.naPotezi();
		IgralnoPolje.napisNaVrsti(igralec);
		igramo();
	}

	public static void igramo() {
		IgralnoPolje.napisNaVrsti(igra.naPotezi());
		switch (igra.stanje) {
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
			clovekNaVrsti = true;
				igramo();
				okno.repaint();
		}
		if (poteza == null) {
			igra.pass();
			igramo();
		}
	}

	public static void igrajClovekovoPotezo(Poteza poteza) {
		if (poteza != null && igra.odigraj(poteza)) {
			clovekNaVrsti = false;
				igramo();
		}
	}

	public static void pass() {
		igra.pass();
		igramo();
	}

	public static void resign() {
		igra.resign();
		igramo();
	}

}
