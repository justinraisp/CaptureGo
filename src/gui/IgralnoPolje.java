package gui;


import logika.*;

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
import java.util.Set;

import javax.swing.JPanel;




@SuppressWarnings("serial")
public class IgralnoPolje extends JPanel  implements MouseListener, MouseMotionListener, KeyListener{
	
	protected Set<Zeton> zetoni;
	
	protected Color barvaPrvega;
	protected Color barvaDrugega;
	protected Color barvaPlosce;
	protected Color barvaRoba;
	protected Stroke debelinaRoba;
	protected double polmer;
	
	private int klikX;
	private int klikY;
	
	
	public int velikostPlosce = Igra.velikostPlosce;
	
	
	private final static double SIRINA_CRTE = 0.08;
	
	public IgralnoPolje(int sirina, int visina) {
		super();
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusable(true);
		barvaPrvega = Color.BLACK;
		barvaDrugega = Color.WHITE;
		barvaRoba = Color.BLACK;
		barvaPlosce = Color.WHITE;
		polmer = 5;
		this.setPreferredSize(new Dimension(sirina,visina));
		this.setBackground(barvaPlosce);
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusable(true);

		
		
	}
	

	
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
		
		for(Zeton z: Polje.zetoni.values()) {
			g2.fillOval(round(z.x - polmer), round(z.y - polmer), premer, premer);
			g2.setColor(z.barva);
		}
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


	