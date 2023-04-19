package gui;


import logika.Igra;
import logika.Koordinate;
import logika.Polje;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;




@SuppressWarnings("serial")
public class IgralnoPolje extends JPanel  implements MouseListener, MouseMotionListener, KeyListener{
	
	protected Graf graf;
	
	protected Color barvaPrvega;
	protected Color barvaDrugega;
	protected Color barvaPlosce;
	protected Color barvaRoba;
	protected Stroke debelinaRoba;
	protected double polmer;
	protected double premer;
	
	private int klikX;
	private int klikY;
	private int stevec = 0;
	
	
	public int velikostPlosce = Igra.velikostPlosce;
	
	int presecisceSirina = getWidth() / velikostPlosce;
	int presecisceVisina = getHeight() / velikostPlosce;
	
	private final static double SIRINA_CRTE = 0.08;
	
	public IgralnoPolje(int sirina, int visina) {
		super();
		

		nastaviGraf();
		nastaviPolje();
		
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusable(true);
		barvaPrvega = Color.BLACK;
		barvaDrugega = Color.WHITE;
		barvaRoba = Color.BLACK;
		barvaPlosce = Color.WHITE;
		polmer = 10;
		premer = 2* polmer;
		this.setPreferredSize(new Dimension(sirina,visina));
		this.setBackground(barvaPlosce);
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusable(true);

		
		
	}
	
	public void nastaviGraf() {
		Graf graf = new Graf();
	}
	
	public Polje nastaviPolje() {
		Polje polje = new Polje(velikostPlosce);
		return polje;
	}
	Polje polje = nastaviPolje();

	
	private double sirinaKvadrata() {
		return Math.min(getWidth(), getHeight()) / (velikostPlosce + 4); }
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		double w = sirinaKvadrata();
		
		g2.setColor(barvaRoba);
		g2.setStroke(new BasicStroke((float) (w * SIRINA_CRTE)));
		for (int i = 2; i < velikostPlosce + 3; i++) {
			g2.drawLine((int)(i * w),(int)(2 * w),
					(int)(i * w), (int)((velikostPlosce + 2) * w));
			g2.drawLine((int)(2 * w), (int)(i * w),
				   (int)((velikostPlosce + 2) * w), (int)(i * w));
		}
	    for(int i = 0; i <= velikostPlosce; i++) {
	        for(int j = 0; j <= velikostPlosce; j++) {
	            int x = (int) ((i+1)*w);
	            int y = (int) ((j+1)*w);
	            //nekaj ni kul, ker i=0,1,2 da prav zeton na presecisce, vec pa ne
	            if (polje.grid[i][j] == Zeton.CRNI) {
	                g2.setColor(Color.BLACK);
	                g2.fillOval(round(x - polmer), round(y - polmer), (int)premer, (int) premer);
	            } else if (polje.grid[i][j] == Zeton.BELI) {
	                g2.setColor(Color.WHITE);
	                g2.fillOval(round(x - polmer), round(y - polmer), (int)premer, (int) premer);
	            }
				}
			}
	}

	private int round(double x) {
		return(int)(x + 0.5);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseClicked(MouseEvent e) {
		int x = klikX = e.getX();
		int y = klikY = e.getY();
		int presecisceSirina = getWidth() / velikostPlosce;
		int presecisceVisina = getHeight() / velikostPlosce;
		int gridX = (klikX + presecisceSirina / 2) / presecisceSirina;
	    int gridY = (klikY + presecisceVisina / 2) / presecisceVisina;
		
		Koordinate koordinate = new Koordinate(x,y);
		Color barva = Color.BLACK;
		
//		if (polje.grid != null) {
			polje.dodajZeton(gridX,gridY, Zeton.CRNI);
			System.out.print(gridY);
//		}
//		else {
//			polje = new Polje(velikostPlosce);
//		}
		repaint();
	}



	@Override
	public void mousePressed(MouseEvent e) {
	}



	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
		
		
	
}	


	