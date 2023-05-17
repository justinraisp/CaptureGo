package gui;



import logika.*;
import splosno.Poteza;
import vodja.Vodja;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;




@SuppressWarnings("serial")
public class IgralnoPolje extends JPanel  implements MouseListener, MouseMotionListener, KeyListener{
	
	protected Color barvaPrvega;
	protected Color barvaDrugega;
	protected Color barvaPlosce;
	protected Color barvaRoba;
	protected Color barvaBorda;
	protected Stroke debelinaRoba;
	protected double polmer;
	protected double premer;
	//public int Vodja.igra.velikostPlosce = Igra.Vodja.igra.velikostPlosce;
	//public Igralec naPotezi = Igra.naPotezi;
	//public static Stanje stanje = Stanje.V_TEKU;
	//private static int steviloPotez = Igra.steviloPotez;
	//protected Polje polje = Igra.polje;
	public static JLabel naVrsti = new JLabel("");
	
	
	
	private final static double SIRINA_CRTE = 0.08;
	
	public IgralnoPolje(int sirina, int visina) {
		super();
		
		//nastaviPolje(Vodja.igra.velikostPlosce);
		//napisNaVrsti(Vodja.igra.naPotezi);
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusable(true);
		barvaPrvega = Color.BLACK;
		barvaDrugega = Color.WHITE;
		barvaRoba = Color.BLACK;
		barvaPlosce = Color.WHITE;
		this.setPreferredSize(new Dimension(sirina,visina));
		this.setBackground(barvaPlosce);
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusable(true);
		
		
	}

	
	public static void napisNaVrsti(Igralec igralec) {
		if(Vodja.igra.stanje == Stanje.ZMAGA_BELI) { 
			naVrsti.setText("Zmagal je drugi igralec!");
		}
		else if(Vodja.igra.stanje == Stanje.ZMAGA_CRNI) { 
			naVrsti.setText("Zmagal je prvi igralec!");
		}
		else {
			if(igralec == Igralec.ČRNI) {
				naVrsti.setText("Na vrsti je prvi igralec");
			}
			else naVrsti.setText("Na vrsti je drugi igralec");
		}
	}
	

	//polovica sirine kvadrata
	private int sirinaKvadrata() {
		return (Math.min(getWidth(), getHeight())- 40) / (Vodja.igra.velikostPlosce); }
	
	
	@Override
	protected void paintComponent(Graphics g) {
		if(Vodja.igra !=  null) {
			int w = sirinaKvadrata();
			int odmikSirina = (getWidth() - (Vodja.igra.velikostPlosce * w))/2;
			int odmikVisina = (getHeight() - (Vodja.igra.velikostPlosce * w))/2;
			int polovica = sirinaKvadrata() / 2;	
			Color barvaBorda = new Color(237, 204, 153);
			polmer = w / 4;
			premer = 2*polmer;
			
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D)g;
			
			g2.setColor(barvaBorda); // Set the color of the background
		    g2.fillRect(odmikSirina, odmikVisina, (Vodja.igra.velikostPlosce* w), (int)(Vodja.igra.velikostPlosce * w));
			
			
			g2.setColor(barvaRoba);
			g2.setStroke(new BasicStroke((float) (w * SIRINA_CRTE)));
			
			
			for (int i = 0; i < Vodja.igra.velikostPlosce; i++) {
				g2.drawLine((i * w + odmikSirina + polovica ),(int)(odmikVisina + polovica),
						(int)(i * w + odmikSirina + polovica ), (int)(Vodja.igra.velikostPlosce * w + odmikVisina - polovica));
			}
			for (int j = 1; j < Vodja.igra.velikostPlosce +1 ; j++) {
				g2.drawLine((int)(odmikSirina + polovica), (int)(j * w + odmikVisina - polovica),
					   (int)((Vodja.igra.velikostPlosce-1) * w+ odmikSirina + polovica ), (int)(j * w + odmikVisina - polovica));
			}
		    for(int i = 0; i < Vodja.igra.velikostPlosce+1; i++) {
		        for(int j = 0; j < Vodja.igra.velikostPlosce+1; j++) {
		            int x = (int) ((i)*w + odmikSirina);
		            int y = (int) ((int) ((j)*w) + odmikVisina);
		            if (Vodja.igra.polje.grid[i][j] == Zeton.CRNI) {
		                g2.setColor(Color.BLACK);
		                g2.drawOval(round(x - 3*polmer), round(y - 3*polmer), (int)premer, (int) premer);
		                if(Vodja.igra.polje.isCaptured(i, j)) {g2.setColor(Color.RED); Vodja.igra.stanje = Stanje.ZMAGA_BELI; napisNaVrsti(Vodja.igra.naPotezi);}
		                g2.fillOval(round(x - 3*polmer), round(y -3* polmer), (int)premer, (int) premer);
		            } else if (Vodja.igra.polje.grid[i][j] == Zeton.BELI) {
		            	g2.setColor(Color.BLACK);
		                g2.drawOval(round(x - 3*polmer), round(y - 3*polmer), (int)premer, (int) premer);
		                g2.setColor(Color.WHITE);
		                if(Vodja.igra.polje.isCaptured(i, j)) {g2.setColor(Color.RED); Vodja.igra.stanje = Stanje.ZMAGA_CRNI; napisNaVrsti(Vodja.igra.naPotezi);}
		                g2.fillOval(round(x - 3*polmer), round(y - 3*polmer), (int)premer, (int) premer);
		            }
					}
				}
		}
	}
	
	
	public List<Point> izracunajPresecisca() {
	    List<Point> presecisca = new ArrayList<Point>();
	    double w = sirinaKvadrata();
	    double odmikSirina = (getWidth() - (Vodja.igra.velikostPlosce * w)) / 2;
	    double odmikVisina = (getHeight() - (Vodja.igra.velikostPlosce * w)) / 2;
	    double polovica = sirinaKvadrata() / 2;
	    int presecisceSirina = getWidth() / Vodja.igra.velikostPlosce;
	    int presecisceVisina = getHeight() / Vodja.igra.velikostPlosce;

	    // skozi vse vertikalne crte
	    for (int i = 0; i < Vodja.igra.velikostPlosce; i++) {
	        int x1 = (int) (i * w + odmikSirina + polovica);
	        int y1 = (int) (odmikVisina + polovica);
	        int x2 = x1;
	        int y2 = (int) (Vodja.igra.velikostPlosce * w + odmikVisina - polovica);

	        // skozi vse horizontalne crte
	        for (int j = 1; j < Vodja.igra.velikostPlosce + 1; j++) {
	            int x3 = (int) (odmikSirina + polovica);
	            int y3 = (int) (j * w + odmikVisina - polovica);
	            int x4 = (int) ((Vodja.igra.velikostPlosce - 1) * w + odmikSirina + polovica);
	            int y4 = y3;

	            // Izracunamo presecisca dveh crt
	            double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4); //ce je det = 0 se ne sekata
	            if (d != 0) {
	                double xi = ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
	                double yi = ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;
	                Point presecisce = new Point((int) xi, (int) yi);
	                presecisca.add(presecisce);
	            }
	        }
	    }
	    return presecisca;
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
		if(Vodja.igra != null && Vodja.igra.stanje == Stanje.V_TEKU ) {
			int w = sirinaKvadrata();
			int odmikSirina = (getWidth() - (Vodja.igra.velikostPlosce * w))/2;
			int odmikVisina = (getHeight() - (Vodja.igra.velikostPlosce * w))/2;
			int polovica = sirinaKvadrata() / 2;	
			List<Point> presecisca = izracunajPresecisca();
	        Point clickedPoint = e.getPoint();
	        for (Point presecisce : presecisca) {
	            double distance = clickedPoint.distance(presecisce);
	            // Draw a filled oval at the intersection point
	            if (distance < polovica) {
		            int x = (int) (1 + (presecisce.x -odmikSirina) / w);
		            int y = (int) (1 + (presecisce.y -odmikVisina) / w);
		            Poteza updated = new Poteza(x, y);
		            Vodja.igra.odigraj(updated);
		           	//steviloPotez += 1;
            }
		}
	        napisNaVrsti(Vodja.igra.naPotezi);
	        repaint();    }
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


	