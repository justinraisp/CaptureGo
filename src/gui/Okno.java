package gui;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.EnumMap;

import javax.swing.JButton;
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

import logika.Igra;
import logika.Igralec;
import vodja.Vodja;
import vodja.VrstaIgralca;

@SuppressWarnings("serial")
public class Okno extends JFrame implements ActionListener{

	protected IgralnoPolje polje;
	
	
	private JMenuItem igraClovekRacunalnik;
	private JMenuItem igraRacunalnikClovek;
	private JMenuItem igraClovekClovek;
	private JMenuItem igraRacunalnikRacunalnik;
	
	private JMenuItem algoritemRacunalnik;
	
	private JMenuItem menuOdpri, menuShrani, menuKoncaj;
	private JMenuItem menuVelikostPlosce;
	private JMenuItem menuBarvaPrvega, menuBarvaDrugega, menuBarvaRoba;
	private JButton passGumb;
	private JLabel naVrsti;
	
	public Okno() {
		super(); // poklciemo konstruktor od jframe
		setTitle("Capture Go");
		polje = new IgralnoPolje(800, 800);
		add(polje);
		polje.add(IgralnoPolje.naVrsti);
		
		JMenuBar menubar = new JMenuBar();
		setJMenuBar(menubar);
		
		JMenu menuDatoteka = dodajMenu(menubar, "Datoteka");
		JMenu menuIgra = dodajMenu(menubar, "Igra");
		JMenu menuAlgoritemRacunalnika = dodajMenu(menubar, "Algoritem računalnika");
		JMenu menuNastavitve = dodajMenu(menubar, "Nastavitve");
		
		menuOdpri = dodajMenuItem(menuDatoteka, "Odpri ...");
		menuShrani = dodajMenuItem(menuDatoteka, "Shrani ...");
		menuDatoteka.addSeparator();
		menuKoncaj = dodajMenuItem(menuDatoteka, "Končaj");
		
		igraClovekRacunalnik = dodajMenuItem(menuIgra, "Človek vs Računalnik");
		igraRacunalnikClovek = dodajMenuItem(menuIgra, "Računalnik vs Človek");
		igraClovekClovek = dodajMenuItem(menuIgra, "Človek vs Človek");
		igraRacunalnikRacunalnik = dodajMenuItem(menuIgra, "Računalnik vs Računalnik");
		
		
		menuVelikostPlosce = dodajMenuItem(menuNastavitve, "Velikost plošče ...");
		menuBarvaRoba = dodajMenuItem(menuNastavitve, "Barva roba ...");
		menuNastavitve.addSeparator();
		menuBarvaPrvega = dodajMenuItem(menuNastavitve, "Barva prvega igralca ...");
		menuBarvaDrugega = dodajMenuItem(menuNastavitve, "Barva drugega igralca ...");
		
		naVrsti = new JLabel();
		
		passGumb = new JButton("Pass");
		passGumb.addActionListener(this);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
        add(passGumb, "North");
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

			}
		}
		else if (objekt == menuShrani) {
			JFileChooser dialog = new JFileChooser();
			int izbira = dialog.showSaveDialog(this);
			if (izbira == JFileChooser.APPROVE_OPTION) {
				String ime = dialog.getSelectedFile().getPath();
				//polje.graf.shrani(ime);
			}
		}
		else if (objekt == menuKoncaj) {
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			}
		
		else if (objekt == menuBarvaPrvega) {
			Color barva = JColorChooser.showDialog(this, "Izberi barvo prvega igralca", polje.barvaPrvega);
			if (barva != null) {
				polje.barvaPrvega = barva;
				
			}
		}
		else if (objekt == menuBarvaDrugega) {
			Color barva = JColorChooser.showDialog(this, "Izberi barvo drugega igralca", polje.barvaDrugega);
			if (barva != null) {
				polje.barvaDrugega = barva;
				repaint();
			}
		}
		else if (objekt == menuBarvaRoba) {
			Color barva = JColorChooser.showDialog(this, "Izberi barvo roba", polje.barvaRoba);
			if (barva != null) {
				polje.barvaRoba = barva;
				
			}
		}
		else if (objekt == menuVelikostPlosce) {
			String velikostPlosce = JOptionPane.showInputDialog(this, "Velikost plošče:");
			if (velikostPlosce != null && velikostPlosce.matches("([2-9]|1[0-9])")) {
				//polje.velikostPlosce =Integer.parseInt(velikostPlosce);
				//polje.nastaviPolje(Integer.parseInt(velikostPlosce));
				Igra.velikostPlosce =Integer.parseInt(velikostPlosce);
				Vodja.igramoNovoIgro();
				
			}
		}
		else if(objekt == igraClovekRacunalnik) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.ČRNI, VrstaIgralca.C); 
			Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.R);
			Vodja.igramoNovoIgro();
			
		}
		 else if (objekt == igraRacunalnikClovek) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.ČRNI, VrstaIgralca.R); 
			Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.C);
			Vodja.igramoNovoIgro();
			
		} else if (objekt == igraClovekClovek) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.ČRNI, VrstaIgralca.C); 
			Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.C);
			Vodja.igramoNovoIgro();
			
		} else if (objekt == igraRacunalnikRacunalnik) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.ČRNI, VrstaIgralca.R); 
			Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.R);
			Vodja.igramoNovoIgro();
			
		} else if(objekt == passGumb) {
			//TODO Vodja.pass();
		}
		repaint();
	}
	public void osveziGUI() {
		if (Vodja.igra == null) {
			naVrsti.setText("Igra ni v teku.");
		}
		else {
			switch(Vodja.igra.dobiStanje()) {
			case V_TEKU: 
				naVrsti.setText("Na potezi je " + Vodja.igra.naPotezi() + 
						" - " + Vodja.vrstaIgralca.get(Vodja.igra.naPotezi())); 
				break;
			case ZMAGA_BELI: 
				naVrsti.setText("Zmagal je O - " + 
						Vodja.vrstaIgralca.get(Igralec.BELI));
				break;
			case ZMAGA_CRNI: 
				naVrsti.setText("Zmagal je X - " + 
						Vodja.vrstaIgralca.get(Igralec.ČRNI));
				break;
			}
		}
		polje.repaint();
	}	
	
	}
	



