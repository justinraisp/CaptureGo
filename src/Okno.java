
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

	protected Platno platno;
	
	private JMenuItem menuOdpri, menuShrani, menuKoncaj;
	private JMenuItem menuPrazen, menuCikel, menuPoln, menuPolnDvodelen;
	private JMenuItem menuBarvaPovezave, menuBarvaTocke, menuBarvaAktivneTocke, menuBarvaOznaceneTocke;
	private JMenuItem menuBarvaRoba, menuDebelinaRoba, menuDebelinaPovezave, menuPolmer;
	
	public Okno() {
		super(); // poklciemo konstruktor od jframe
		setTitle("Capture Go");
		platno = new Platno(800, 800);
		add(platno);
		
		JMenuBar menubar = new JMenuBar();
		setJMenuBar(menubar);
		
		JMenu menuDatoteka = dodajMenu(menubar, "Datoteka");
		JMenu menuGraf = dodajMenu(menubar, "Graf");
		JMenu menuNastavitve = dodajMenu(menubar, "Nastavitve");
		
		menuOdpri = dodajMenuItem(menuDatoteka, "Odpri ...");
		menuShrani = dodajMenuItem(menuDatoteka, "Shrani ...");
		menuDatoteka.addSeparator();
		menuKoncaj = dodajMenuItem(menuDatoteka, "Končaj");
		
		
		menuPrazen = dodajMenuItem(menuGraf, "Prazen ...");
		menuCikel = dodajMenuItem(menuGraf, "Cikel ...");
		menuPoln = dodajMenuItem(menuGraf, "Poln ...");
		menuPolnDvodelen = dodajMenuItem(menuGraf, "Poln dvodelen ...");
		
		menuBarvaPovezave = dodajMenuItem(menuNastavitve, "Barva povezave ...");
		menuBarvaTocke = dodajMenuItem(menuNastavitve, "Barva točke ...");
		menuBarvaAktivneTocke = dodajMenuItem(menuNastavitve, "Barva aktivne točke ...");
		menuBarvaOznaceneTocke = dodajMenuItem(menuNastavitve, "Barva označene točke ...");
		menuBarvaRoba = dodajMenuItem(menuNastavitve, "Barva roba ...");
		menuNastavitve.addSeparator();
		menuDebelinaRoba = dodajMenuItem(menuNastavitve, "Debelina roba ...");
		menuDebelinaPovezave = dodajMenuItem(menuNastavitve, "Debelina povezave ...");
		menuNastavitve.addSeparator();
		menuPolmer = dodajMenuItem(menuNastavitve, "Polmer ...");

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


		
}
