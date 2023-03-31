
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Okno extends JFrame implements ActionListener{

	protected IgralnoPolje polje;
	
	
	private JMenuItem igraClovekRacunalnik;
	private JMenuItem igraRacunalnikClovek;
	private JMenuItem igraClovekClovek;
	private JMenuItem igraRacunalnikRacunalnik;
	
	private JMenuItem menuOdpri, menuShrani, menuKoncaj;
	private JMenuItem menuVelikostPlosce;
	private JMenuItem menuBarvaPrvega, menuBarvaDrugega, menuBarvaPlosce;
	
	public Okno() {
		super(); // poklciemo konstruktor od jframe
		setTitle("Capture Go");
		polje = new IgralnoPolje(800, 800);
		add(polje);
		
		JMenuBar menubar = new JMenuBar();
		setJMenuBar(menubar);
		
		JMenu menuDatoteka = dodajMenu(menubar, "Datoteka");
		JMenu menuIgra = dodajMenu(menubar, "Igra");
		JMenu menuNastavitve = dodajMenu(menubar, "Nastavitve");
		
		menuOdpri = dodajMenuItem(menuDatoteka, "Odpri ...");
		menuShrani = dodajMenuItem(menuDatoteka, "Shrani ...");
		menuDatoteka.addSeparator();
		menuKoncaj = dodajMenuItem(menuDatoteka, "Končaj");
		
		
		menuVelikostPlosce = dodajMenuItem(menuNastavitve, "Velikost plošče ...");
		menuBarvaPlosce = dodajMenuItem(menuNastavitve, "Barva plošče ...");
		menuNastavitve.addSeparator();
		menuBarvaPrvega = dodajMenuItem(menuNastavitve, "Barva prvega igralca ...");
		menuBarvaDrugega = dodajMenuItem(menuNastavitve, "Barva drugega igralca ...");
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private JMenu dodajMenu(JMenuBar menubar, String naslov) {
		JMenu menu = new JMenu(naslov);
		menubar.add(menu);
		return menu;
	}
	
	private JMenuItem dodajMenuItem(JMenu menu, String naslov) {
		JMenuItem menuitem = new JMenuItem(naslov);
		menu.add(menuitem);
		menuitem.addActionListener(this);
		return menuitem;
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object objekt = e.getSource();
		if (objekt == menuOdpri) {
			JFileChooser dialog = new JFileChooser();
			int izbira = dialog.showOpenDialog(this);
			if (izbira == JFileChooser.APPROVE_OPTION) {
				String ime = dialog.getSelectedFile().getPath();
				//Graf graf = Graf.preberi(ime);
				//platno.nastaviGraf(graf);
			}
		}
		else if (objekt == menuShrani) {
			JFileChooser dialog = new JFileChooser();
			int izbira = dialog.showSaveDialog(this);
			if (izbira == JFileChooser.APPROVE_OPTION) {
				String ime = dialog.getSelectedFile().getPath();
				//platno.graf.shrani(ime);
			}
		}
		else if (objekt == menuKoncaj) {
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			}
		}
		//else if (objekt == menuBarvaPrvega) {
			//Color barva = JColorChooser.showDialog(this, "Izberi barvo prvega igralca", platno.barvaPrvega);
			//if (barva != null) {
				//platno.barvaPrvega = barva;
				//repaint();
			//}
		//}
		//else if (objekt == menuBarvaDrugega) {
			//Color barva = JColorChooser.showDialog(this, "Izberi barvo drugega igralca", platno.barvaDrugega);
			//if (barva != null) {
				//platno.barvaDrugega = barva;
				//repaint();
			//}
		//}
		//else if (objekt == menuBarvaPlosce) {
			//Color barva = JColorChooser.showDialog(this, "Izberi barvo plošče", platno.barvaPlosce);
			//if (barva != null) {
				//platno.barvaPlosce = barva;
				//repaint();
			//}
		//}
		
	}
		

