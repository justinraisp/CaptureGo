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
import javax.swing.SwingConstants;

import logika.Igra;
import logika.Igralec;
import vodja.Vodja;
import vodja.VrstaIgralca;

@SuppressWarnings("serial")
public class Okno extends JFrame implements ActionListener {

	protected IgralnoPolje polje;

	private JMenuItem menuBarvaPrvega, menuBarvaDrugega, menuBarvaRoba;
	private JMenuItem menuNovaIgra;
	private JMenuItem menuPravilaCaptureGo;
	private JMenuItem menuPravilaGo;
	private JButton passGumb;
	private JButton resignGumb;
	private JLabel naVrsti;
	private int stevec = 0;

	public Okno() {
		super(); // poklciemo konstruktor od jframe
		setTitle("Capture Go");
		polje = new IgralnoPolje(800, 800);
		add(polje);
		polje.add(IgralnoPolje.naVrsti);

		JMenuBar menubar = new JMenuBar();
		setJMenuBar(menubar);
		JMenu menuIgra = dodajMenu(menubar, "Igra");
		JMenu menuNastavitve = dodajMenu(menubar, "Nastavitve");
		menuNovaIgra = dodajMenuItem(menuIgra, "Nova igra");
		menuPravilaCaptureGo = dodajMenuItem(menuIgra, "Pravila CaptureGo");
		menuPravilaGo = dodajMenuItem(menuIgra, "Pravila Go");
		menuBarvaRoba = dodajMenuItem(menuNastavitve, "Barva roba ...");
		menuNastavitve.addSeparator();
		menuBarvaPrvega = dodajMenuItem(menuNastavitve, "Barva prvega igralca ...");
		menuBarvaDrugega = dodajMenuItem(menuNastavitve, "Barva drugega igralca ...");
		naVrsti = new JLabel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void createPassAndResignButton() {
		if (stevec == 0) {
			passGumb = new JButton("Preskoči");
			passGumb.addActionListener(this);
			passGumb.setPreferredSize(new Dimension(85, 15));
			polje.add(passGumb);
			passGumb.setLocation(0, 0);
			resignGumb = new JButton("Predaja");
			resignGumb.addActionListener(this);
			resignGumb.setPreferredSize(new Dimension(85, 15));
			polje.add(resignGumb);
			stevec++;
		}
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

		if (objekt == menuBarvaPrvega) {
			Color barva = JColorChooser.showDialog(this, "Izberi barvo prvega igralca", polje.barvaPrvega);
			if (barva != null) {
				polje.barvaPrvega = barva;

			}
		} else if (objekt == menuBarvaDrugega) {
			Color barva = JColorChooser.showDialog(this, "Izberi barvo drugega igralca", polje.barvaDrugega);
			if (barva != null) {
				polje.barvaDrugega = barva;
			}
		} else if (objekt == menuBarvaRoba) {
			Color barva = JColorChooser.showDialog(this, "Izberi barvo roba", polje.barvaRoba);
			if (barva != null) {
				polje.barvaRoba = barva;
			}
		} else if (objekt == menuNovaIgra) {
			novaIgra();
		} else if (objekt == menuPravilaCaptureGo) {
			String pravila = "<html>Kamne se postavlja na enak način kot pri go, razlike pa so sledeče: <ul><li> Ni preskakovanja potez, </li><li> Ni predaje, </li><li> Zmagovalec je igralec, ki prvi ujame kak nasprotnikov kamen.</li></ul></html>";
			JOptionPane.showMessageDialog(this, pravila, "Pravila CaptureGo", JOptionPane.INFORMATION_MESSAGE);
		} else if (objekt == menuPravilaGo) {
			String pravila = "<html>Veljajo standardna pravila za go. Točkovanje pa je sledeče: <ul><li> Štejeta število zasedenih polj in število kamnov posameznega igralca. </li><li>Beli ima kompenzacijo glede na velikost plošče in zmaga v primeru izenačenega rezultata.</li></ul></html>";
			JOptionPane.showMessageDialog(this, pravila, "Pravila Go", JOptionPane.INFORMATION_MESSAGE);
		} else if (objekt == passGumb) {
			Vodja.pass();
			IgralnoPolje.napisNaVrsti(Vodja.igra.naPotezi);
		} else if (objekt == resignGumb) {
			Vodja.resign();
			IgralnoPolje.napisNaVrsti(Vodja.igra.naPotezi);
		}
		repaint();
	}

	private void novaIgra() {
		Object[] options = { "Go", "CaptureGo" };
		int tipIgre = JOptionPane.showOptionDialog(this, "Izberi vrsto igre:", "Nova igra", JOptionPane.DEFAULT_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		if (tipIgre == 0) {
			Object[] playerOptions = { "Igralec vs Igralec", "Igralec vs Računalnik", "Računalnik vs Igralec",
					"Računalnik vs Računalnik" };
			int tipIgralca = JOptionPane.showOptionDialog(this, "Izberi vrsto igralcev:", "Nova igra - Go",
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, playerOptions, playerOptions[0]);

			if (tipIgralca == 0) {
				// Igralec vs Igralec
				String boardSize = null;
				int size = 0;
				while (true) {
					boardSize = JOptionPane.showInputDialog(this, "Velikost plošče (2-19):");
					if (boardSize == null) {
						break;
					}
					if (boardSize.matches("([2-9]|1[0-9])")) {
						size = Integer.parseInt(boardSize);
						break;
					} else {
						JOptionPane.showMessageDialog(this, "Neveljavna velikost plošče! Vnesite veljavno število.",
								"Napaka", JOptionPane.ERROR_MESSAGE);
					}
				}
				if (boardSize != null) {
					Vodja.vrstaIgralca = new EnumMap<Igralec, VrstaIgralca>(Igralec.class);
					Vodja.vrstaIgralca.put(Igralec.ČRNI, VrstaIgralca.C);
					Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.C);
					Igra.velikostPlosce = size;
					Vodja.igramoNovoIgro(false);
					createPassAndResignButton();
				}
			} else if (tipIgralca == 1) {
				// Igralec vs Računalnik
				String boardSize = null;
				int size = 0;
				while (true) {
					boardSize = JOptionPane.showInputDialog(this, "Velikost plošče (2-19):");
					if (boardSize == null) {
						break;
					}
					if (boardSize.matches("([2-9]|1[0-9])")) {
						size = Integer.parseInt(boardSize);
						break;
					} else {
						JOptionPane.showMessageDialog(this, "Neveljavna velikost plošče! Vnesite veljavno število.",
								"Napaka", JOptionPane.ERROR_MESSAGE);
					}
				}
				if (boardSize != null) {
					Vodja.vrstaIgralca = new EnumMap<Igralec, VrstaIgralca>(Igralec.class);
					Vodja.vrstaIgralca.put(Igralec.ČRNI, VrstaIgralca.C);
					Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.R);
					Igra.velikostPlosce = size;
					Vodja.igramoNovoIgro(false);
					createPassAndResignButton();
				}
			} else if (tipIgralca == 2) {
				// Računalnik vs Igralec
				String boardSize = null;
				int size = 0;
				while (true) {
					boardSize = JOptionPane.showInputDialog(this, "Velikost plošče (2-19):");
					if (boardSize == null) {
						break;
					}
					if (boardSize.matches("([2-9]|1[0-9])")) {
						size = Integer.parseInt(boardSize);
						break;
					} else {
						JOptionPane.showMessageDialog(this, "Neveljavna velikost plošče! Vnesite veljavno število.",
								"Napaka", JOptionPane.ERROR_MESSAGE);
					}
				}
				if (boardSize != null) {
					Vodja.vrstaIgralca = new EnumMap<Igralec, VrstaIgralca>(Igralec.class);
					Vodja.vrstaIgralca.put(Igralec.ČRNI, VrstaIgralca.R);
					Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.C);
					Igra.velikostPlosce = size;
					Vodja.igramoNovoIgro(false);
					createPassAndResignButton();
				}
			} else if (tipIgralca == 3) {
				// Računalnik vs Računalnik
				String boardSize = null;
				int size = 0;
				while (true) {
					boardSize = JOptionPane.showInputDialog(this, "Velikost plošče (2-19):");
					if (boardSize == null) {
						break;
					}
					if (boardSize.matches("([2-9]|1[0-9])")) {
						size = Integer.parseInt(boardSize);
						break;
					} else {
						JOptionPane.showMessageDialog(this, "Neveljavna velikost plošče! Vnesite veljavno število.",
								"Napaka", JOptionPane.ERROR_MESSAGE);
					}
				}
				if (boardSize != null) {
					Vodja.vrstaIgralca = new EnumMap<Igralec, VrstaIgralca>(Igralec.class);
					Vodja.vrstaIgralca.put(Igralec.ČRNI, VrstaIgralca.R);
					Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.R);
					Igra.velikostPlosce = size;
					Vodja.igramoNovoIgro(false);
				}
			}
		} else if (tipIgre == 1) {
			Object[] playerOptions = { "Igralec vs Igralec", "Igralec vs Računalnik", "Računalnik vs Igralec",
					"Računalnik vs Računalnik" };
			int tipIgralca = JOptionPane.showOptionDialog(this, "Izberi vrsto igralcev:", "Nova igra - CaptureGo",
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, playerOptions, playerOptions[0]);

			if (tipIgralca == 0) {
				// Igralec vs Igralec
				String boardSize = null;
				int size = 0;
				while (true) {
					boardSize = JOptionPane.showInputDialog(this, "Velikost plošče (2-19):");
					if (boardSize == null) {
						break;
					}
					if (boardSize.matches("([2-9]|1[0-9])")) {
						size = Integer.parseInt(boardSize);
						break;
					} else {
						JOptionPane.showMessageDialog(this, "Neveljavna velikost plošče! Vnesite veljavno število.",
								"Napaka", JOptionPane.ERROR_MESSAGE);
					}
				}
				if (boardSize != null) {
					Vodja.vrstaIgralca = new EnumMap<Igralec, VrstaIgralca>(Igralec.class);
					Vodja.vrstaIgralca.put(Igralec.ČRNI, VrstaIgralca.C);
					Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.C);
					Igra.velikostPlosce = size;
					Vodja.igramoNovoIgro(true);
				}
			} else if (tipIgralca == 1) {
				// Igralec vs Računalnik
				String boardSize = null;
				int size = 0;
				while (true) {
					boardSize = JOptionPane.showInputDialog(this, "Velikost plošče (2-19):");
					if (boardSize == null) {
						break;
					}
					if (boardSize.matches("([2-9]|1[0-9])")) {
						size = Integer.parseInt(boardSize);
						break;
					} else {
						JOptionPane.showMessageDialog(this, "Neveljavna velikost plošče! Vnesite veljavno število.",
								"Napaka", JOptionPane.ERROR_MESSAGE);
					}
				}
				if (boardSize != null) {
					Vodja.vrstaIgralca = new EnumMap<Igralec, VrstaIgralca>(Igralec.class);
					Vodja.vrstaIgralca.put(Igralec.ČRNI, VrstaIgralca.C);
					Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.R);
					Igra.velikostPlosce = size;
					Vodja.igramoNovoIgro(true);
				}
			} else if (tipIgralca == 2) {
				// Računalnik vs Igralec
				String boardSize = null;
				int size = 0;
				while (true) {
					boardSize = JOptionPane.showInputDialog(this, "Velikost plošče (2-19):");
					if (boardSize == null) {
						break;
					}
					if (boardSize.matches("([2-9]|1[0-9])")) {
						size = Integer.parseInt(boardSize);
						break;
					} else {
						JOptionPane.showMessageDialog(this, "Neveljavna velikost plošče! Vnesite veljavno število.",
								"Napaka", JOptionPane.ERROR_MESSAGE);
					}
				}
				if (boardSize != null) {
					Vodja.vrstaIgralca = new EnumMap<Igralec, VrstaIgralca>(Igralec.class);
					Vodja.vrstaIgralca.put(Igralec.ČRNI, VrstaIgralca.R);
					Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.C);
					Igra.velikostPlosce = size;
					Vodja.igramoNovoIgro(true);
				}
			} else if (tipIgralca == 3) {
				// Računalnik vs Računalnik
				String boardSize = null;
				int size = 0;
				while (true) {
					boardSize = JOptionPane.showInputDialog(this, "Velikost plošče (2-19):");
					if (boardSize == null) {
						break;
					}
					if (boardSize.matches("([2-9]|1[0-9])")) {
						size = Integer.parseInt(boardSize);
						break;
					} else {
						JOptionPane.showMessageDialog(this, "Neveljavna velikost plošče! Vnesite veljavno število.",
								"Napaka", JOptionPane.ERROR_MESSAGE);
					}
				}
				if (boardSize != null) {
					Vodja.vrstaIgralca = new EnumMap<Igralec, VrstaIgralca>(Igralec.class);
					Vodja.vrstaIgralca.put(Igralec.ČRNI, VrstaIgralca.R);
					Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.R);
					Igra.velikostPlosce = size;
					Vodja.igramoNovoIgro(true);
				}
			}
		}
	}

}
