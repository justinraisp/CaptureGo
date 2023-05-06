package gui;



import logika.*;


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
	protected Stroke debelinaRoba;
	protected double polmer;
	protected double premer;
	public int velikostPlosce = Igra.velikostPlosce;
	private static int stevec = 0;
	protected Polje polje;
	public static JLabel naVrsti = new JLabel("");
	
	private final static double SIRINA_CRTE = 0.08;
	
	public IgralnoPolje(int sirina, int visina) {
		super();
		
		nastaviPolje(velikostPlosce);
		napisNaVrsti();
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

	
	public void nastaviPolje(int velikostPlosce) {
		polje = new Polje(velikostPlosce);
		repaint();
	}
	
	public static void napisNaVrsti() {
		if(stevec % 2 == 0) {
			naVrsti.setText("Na vrsti je prvi igralec");
		}
		else naVrsti.setText("Na vrsti je drugi igralec");
	}
	

	//polovica sirine kvadrata
	private double sirinaKvadrata() {
		return (Math.min(getWidth(), getHeight())- 40) / (velikostPlosce); }
	
	
	@Override
	protected void paintComponent(Graphics g) {
		double w = sirinaKvadrata();
		double odmikSirina = (getWidth() - (velikostPlosce * w))/2;
		double odmikVisina = (getHeight() - (velikostPlosce * w))/2;
		double polovica = sirinaKvadrata() / 2;	
		polmer = w / 4;
		premer = 2*polmer;
		
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(barvaRoba);
		g2.setStroke(new BasicStroke((float) (w * SIRINA_CRTE)));
		
		for (int i = 0; i < velikostPlosce; i++) {
			g2.drawLine((int)(i * w + odmikSirina + polovica ),(int)(odmikVisina + polovica),
					(int)(i * w + odmikSirina + polovica ), (int)(velikostPlosce * w + odmikVisina - polovica));
		}
		for (int j = 1; j < velikostPlosce +1 ; j++) {
			g2.drawLine((int)(odmikSirina + polovica), (int)(j * w + odmikVisina - polovica),
				   (int)((velikostPlosce-1) * w+ odmikSirina + polovica ), (int)(j * w + odmikVisina - polovica));
		}
	    for(int i = 0; i < velikostPlosce+1; i++) {
	        for(int j = 0; j < velikostPlosce+1; j++) {
	            int x = (int) ((i)*w + odmikSirina);
	            int y = (int) ((int) ((j)*w) + odmikVisina);
	            if (polje.grid[i][j] == Zeton.CRNI) {
	                g2.setColor(Color.BLACK);
	                g2.drawOval(round(x - 3*polmer), round(y - 3*polmer), (int)premer, (int) premer);
	                if(polje.isCaptured(i, j)) g2.setColor(Color.RED);
	                g2.fillOval(round(x - 3*polmer), round(y -3* polmer), (int)premer, (int) premer);
	            } else if (polje.grid[i][j] == Zeton.BELI) {
	            	g2.setColor(Color.BLACK);
	                g2.drawOval(round(x - 3*polmer), round(y - 3*polmer), (int)premer, (int) premer);
	                g2.setColor(Color.WHITE);
	                if(polje.isCaptured(i, j)) g2.setColor(Color.RED);
	                g2.fillOval(round(x - 3*polmer), round(y - 3*polmer), (int)premer, (int) premer);
	            }
				}
			}

	}
	
	
	public List<Point> izracunajPresecisca() {
	    List<Point> presecisca = new ArrayList<Point>();
	    double w = sirinaKvadrata();
	    double odmikSirina = (getWidth() - (velikostPlosce * w)) / 2;
	    double odmikVisina = (getHeight() - (velikostPlosce * w)) / 2;
	    double polovica = sirinaKvadrata() / 2;
	    int presecisceSirina = getWidth() / velikostPlosce;
	    int presecisceVisina = getHeight() / velikostPlosce;

	    // skozi vse vertikalne crte
	    for (int i = 0; i < velikostPlosce; i++) {
	        int x1 = (int) (i * w + odmikSirina + polovica);
	        int y1 = (int) (odmikVisina + polovica);
	        int x2 = x1;
	        int y2 = (int) (velikostPlosce * w + odmikVisina - polovica);

	        // skozi vse horizontalne crte
	        for (int j = 1; j < velikostPlosce + 1; j++) {
	            int x3 = (int) (odmikSirina + polovica);
	            int y3 = (int) (j * w + odmikVisina - polovica);
	            int x4 = (int) ((velikostPlosce - 1) * w + odmikSirina + polovica);
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
		double w = sirinaKvadrata();
		double odmikSirina = (getWidth() - (velikostPlosce * w))/2;
		double odmikVisina = (getHeight() - (velikostPlosce * w))/2;
		double polovica = sirinaKvadrata() / 2;	
		List<Point> presecisca = izracunajPresecisca();
        Point clickedPoint = e.getPoint();
        for (Point presecisce : presecisca) {
            double distance = clickedPoint.distance(presecisce);
            // Draw a filled oval at the intersection point
            if (distance < polovica) {
	            int x = (int) (1 + (presecisce.x -odmikSirina) / w);
	            int y = (int) (1 + (presecisce.y -odmikVisina) / w);
	            if(!polje.vsebujeZeton(x,y) && (stevec % 2 == 0)) {
	            	polje.dodajZeton(x,y, Zeton.CRNI);
	            	stevec += 1;
	            }
	            if(!polje.vsebujeZeton(x,y) && (stevec % 2 != 0)) {
	            	polje.dodajZeton(x,y, Zeton.BELI);
	            	stevec += 1;
	            }
            }
        }
        napisNaVrsti();
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


	