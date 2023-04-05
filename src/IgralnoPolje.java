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

import javax.swing.JPanel;




@SuppressWarnings("serial")
public class IgralnoPolje extends JPanel  implements MouseListener, MouseMotionListener, KeyListener{
	
	protected Igra igra;
	
	protected Color barvaPrvega;
	protected Color barvaDrugega;
	protected Color barvaPlosce;
	protected Color barvaRoba;
	protected Stroke debelinaRoba;
	
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
		this.setPreferredSize(new Dimension(sirina,visina));
		this.setBackground(barvaPlosce);
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusable(true);

		
		
	}
	
	public void pripraviIgro(Igra igra) {
		this.igra = igra;
		repaint();
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
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
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


	