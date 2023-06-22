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

	private JMenuItem menuBarvaPrvega, menuBarvaDrugega, menuBarvaRoba;
	private JMenuItem menuNovaIgra;
	private JMenuItem menuKoncajIgra;
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
		
		menuBarvaRoba = dodajMenuItem(menuNastavitve, "Barva roba ...");
		menuNastavitve.addSeparator();
		menuBarvaPrvega = dodajMenuItem(menuNastavitve, "Barva prvega igralca ...");
		menuBarvaDrugega = dodajMenuItem(menuNastavitve, "Barva drugega igralca ...");
		naVrsti = new JLabel();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
    private void createPassAndResignButton() {
    	if(stevec == 0) {
        passGumb = new JButton("Pass");
        passGumb.addActionListener(this);
        passGumb.setPreferredSize(new Dimension(75,15));
        polje.add(passGumb);
        passGumb.setLocation(0,0);
        resignGumb = new JButton("Resign");
        resignGumb.addActionListener(this);
        resignGumb.setPreferredSize(new Dimension(75,15));
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
		}
		else if (objekt == menuBarvaDrugega) {
			Color barva = JColorChooser.showDialog(this, "Izberi barvo drugega igralca", polje.barvaDrugega);
			if (barva != null) {
				polje.barvaDrugega = barva;
			}
		}
		else if (objekt == menuBarvaRoba) {
			Color barva = JColorChooser.showDialog(this, "Izberi barvo roba", polje.barvaRoba);
			if (barva != null) {
				polje.barvaRoba = barva;
			}
		}

		else if (objekt == menuNovaIgra) {
		    novaIgra();
		} 
		 else if(objekt == passGumb) {
			Vodja.pass();
			IgralnoPolje.napisNaVrsti(Vodja.igra.naPotezi);
		} 
		 else if(objekt == resignGumb) {
			Vodja.resign();
			IgralnoPolje.napisNaVrsti(Vodja.igra.naPotezi);
		 }
		repaint();
	}
	
	private void novaIgra() {
	    Object[] options = {"Go", "CaptureGo"};
	    int gameType = JOptionPane.showOptionDialog(this, "Izberi vrsto igre:", "Nova igra", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

	    if (gameType == 0) {
	        Object[] playerOptions = {"Igralec vs Igralec", "Igralec vs Računalnik", "Računalnik vs Igralec", "Računalnik vs Računalnik"};
	        int playerType = JOptionPane.showOptionDialog(this, "Izberi vrsto igralcev:", "Nova igra - Go", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, playerOptions, playerOptions[0]);

	        if (playerType == 0) {
	            // Igralec vs Igralec
	            String boardSize = JOptionPane.showInputDialog(this, "Velikost plošče (2-19):");
	            if (boardSize != null && boardSize.matches("([2-9]|1[0-9])")) {
	    			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
	    			Vodja.vrstaIgralca.put(Igralec.ČRNI, VrstaIgralca.C); 
	    			Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.C);
	                int size = Integer.parseInt(boardSize);
					Igra.velikostPlosce = size;
					Vodja.igramoNovoIgro(false);
					createPassAndResignButton();
	            }
	        } 
	        else if (playerType == 1) {
	            // Igralec vs Računalnik
	            String boardSize = JOptionPane.showInputDialog(this, "Velikost plošče (2-19):");
	            if (boardSize != null && boardSize.matches("([2-9]|1[0-9])")) {
	    			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
	    			Vodja.vrstaIgralca.put(Igralec.ČRNI, VrstaIgralca.C); 
	    			Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.R);
	                int size = Integer.parseInt(boardSize);
					Igra.velikostPlosce = size;
					Vodja.igramoNovoIgro(false);
					createPassAndResignButton();
	            }  
	        }
	        else if (playerType == 2) {
	            // Računalnik vs Igralec
	            String boardSize = JOptionPane.showInputDialog(this, "Velikost plošče (2-19):");
	            if (boardSize != null && boardSize.matches("([2-9]|1[0-9])")) {
	    			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
	    			Vodja.vrstaIgralca.put(Igralec.ČRNI, VrstaIgralca.R); 
	    			Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.C);
	                int size = Integer.parseInt(boardSize);
					Igra.velikostPlosce = size;
					Vodja.igramoNovoIgro(false);
					createPassAndResignButton();
	            }
	        }
	        else if (playerType == 3) {
	            // Računalnik vs Računalnik
	            String boardSize = JOptionPane.showInputDialog(this, "Velikost plošče (2-19):");
	            if (boardSize != null && boardSize.matches("([2-9]|1[0-9])")) {
	    			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
	    			Vodja.vrstaIgralca.put(Igralec.ČRNI, VrstaIgralca.R); 
	    			Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.R);
	                int size = Integer.parseInt(boardSize);
					Igra.velikostPlosce = size;
					Vodja.igramoNovoIgro(false);
					createPassAndResignButton();
	            }
	        }
	    } else if (gameType == 1) {
	        Object[] playerOptions = {"Igralec vs Igralec", "Igralec vs Računalnik", "Računalnik vs Igralec", "Računalnik vs Računalnik"};
	        int playerType = JOptionPane.showOptionDialog(this, "Izberi vrsto igralcev:", "Nova igra - CaptureGo", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, playerOptions, playerOptions[0]);

	        if (playerType == 0) {
	            // Igralec vs Igralec
	            String boardSize = JOptionPane.showInputDialog(this, "Velikost plošče (2-19):");
	            if (boardSize != null && boardSize.matches("([2-9]|1[0-9])")) {
	    			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
	    			Vodja.vrstaIgralca.put(Igralec.ČRNI, VrstaIgralca.C); 
	    			Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.C);
	                int size = Integer.parseInt(boardSize);
					Igra.velikostPlosce = size;
					Vodja.igramoNovoIgro(true);
	            }
	        } 
	        else if (playerType == 1) {
	            // Igralec vs Računalnik
	            String boardSize = JOptionPane.showInputDialog(this, "Velikost plošče (2-19):");
	            if (boardSize != null && boardSize.matches("([2-9]|1[0-9])")) {
	    			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
	    			Vodja.vrstaIgralca.put(Igralec.ČRNI, VrstaIgralca.C); 
	    			Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.R);
	                int size = Integer.parseInt(boardSize);
					Igra.velikostPlosce = size;
					Vodja.igramoNovoIgro(true);
	            }  
	        }
	        else if (playerType == 2) {
	            // Računalnik vs Igralec
	            String boardSize = JOptionPane.showInputDialog(this, "Velikost plošče (2-19):");
	            if (boardSize != null && boardSize.matches("([2-9]|1[0-9])")) {
	    			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
	    			Vodja.vrstaIgralca.put(Igralec.ČRNI, VrstaIgralca.R); 
	    			Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.C);
	                int size = Integer.parseInt(boardSize);
					Igra.velikostPlosce = size;
					Vodja.igramoNovoIgro(true);
	            }
	        }
	        else if (playerType == 3) {
	            // Računalnik vs Računalnik
	            String boardSize = JOptionPane.showInputDialog(this, "Velikost plošče (2-19):");
	            if (boardSize != null && boardSize.matches("([2-9]|1[0-9])")) {
	    			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
	    			Vodja.vrstaIgralca.put(Igralec.ČRNI, VrstaIgralca.R); 
	    			Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.R);
	                int size = Integer.parseInt(boardSize);
					Igra.velikostPlosce = size;
					Vodja.igramoNovoIgro(true);
	            }
	        }  
	    }
	}
	
}
	
	
	
	



