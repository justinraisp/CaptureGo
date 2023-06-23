import gui.Okno;
import vodja.Vodja;

public class CaptureGo {

	public static void main(String[] args) {

		Okno okno = new Okno();
		okno.pack();
		okno.setVisible(true);
		Okno glavno_okno = null;
		Vodja.okno = okno;

	}
}
